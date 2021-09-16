/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.connector.spring;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.lang.properties.psi.impl.PropertyImpl;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiElement;
import com.microsoft.azure.toolkit.intellij.common.AzureIcons;
import com.microsoft.azure.toolkit.intellij.connector.Connection;
import com.microsoft.azure.toolkit.intellij.connector.ConnectionManager;
import com.microsoft.azure.toolkit.intellij.connector.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class SpringPropertiesLineMarkerProvider implements LineMarkerProvider {

    @Override
    @Nullable
    public LineMarkerInfo<PsiElement> getLineMarkerInfo(@Nonnull PsiElement element) {
        if (!(element instanceof PropertyImpl)) {
            return null;
        }
        final String propKey = ((PropertyImpl) element).getKey();
        final String propVal = ((PropertyImpl) element).getValue();
        final Module module = ModuleUtil.findModuleForFile(element.getContainingFile().getVirtualFile(), element.getProject());
        if (Objects.isNull(module)) {
            return null;
        }
        return element.getProject().getService(ConnectionManager.class)
                .getConnectionsByConsumerId(module.getName()).stream()
                .filter(c -> c.getConsumer().getName().equals(module.getName()))
                .filter(c -> c instanceof SpringSupportedConnection)
                .filter(c -> StringUtils.equals(propKey, ((SpringSupportedConnection) c).getSpringProperties().get(0).getKey()))
                .filter(c -> StringUtils.equals(propVal, ((SpringSupportedConnection) c).getSpringProperties().get(0).getValue()))
                .findFirst()
                .map(Connection::getResource)
                .map(r -> new LineMarkerInfo<>(
                        element, element.getTextRange(),
                        AzureIcons.getIcon("/icons/connector/connect.svg"),
                        element2 -> String.format("%s (%s)", r.getName(), r.getDefinition().getTitle()),
                        new SpringDatasourceNavigationHandler(r),
                        GutterIconRenderer.Alignment.LEFT, () -> "")).orElse(null);
    }

    @RequiredArgsConstructor
    public static class SpringDatasourceNavigationHandler implements GutterIconNavigationHandler<PsiElement> {
        private final Resource<?> resource;

        @Override
        public void navigate(MouseEvent mouseEvent, PsiElement psiElement) {
            this.resource.navigate(psiElement.getProject());
        }
    }
}
