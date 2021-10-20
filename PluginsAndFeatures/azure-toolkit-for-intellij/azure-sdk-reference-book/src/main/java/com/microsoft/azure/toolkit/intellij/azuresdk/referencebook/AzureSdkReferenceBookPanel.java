/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.azuresdk.referencebook;

import com.intellij.ui.components.JBScrollPane;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class AzureSdkReferenceBookPanel {
    @Getter
    private JPanel contentPanel;
    private AzureSdkTreePanel servicesTreePanel;
    private JBScrollPane rightPane;
    private AzureSdkFeatureDetailPanel featureDetailPanel;
    private JPanel leftPanel;

    public AzureSdkReferenceBookPanel() {
        this.contentPanel.setPreferredSize(new Dimension(840, 600));
        this.initListeners();
        this.servicesTreePanel.refresh();
    }

    private void initListeners() {
        this.servicesTreePanel.setOnSdkFeatureNodeSelected(feature -> this.featureDetailPanel.setData(feature));
    }
}
