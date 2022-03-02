/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.common.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.microsoft.azure.toolkit.lib.common.action.Action;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AnCommandAction extends AnAction implements IActionCommand {
    private final List<Pair<BiFunction<Object, AnActionEvent, ?>, BiConsumer<?, AnActionEvent>>> handlers = new ArrayList<>();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Object source = e.getDataContext().getData(Action.SOURCE);
    }

    @Nullable
    public BiConsumer<?, AnActionEvent> getHandler(Object source, AnActionEvent e) {
        for (int i = this.handlers.size() - 1; i >= 0; i--) {
            final Pair<BiFunction<Object, AnActionEvent, ?>, BiConsumer<?, AnActionEvent>> p = this.handlers.get(i);
            final BiFunction<Object, AnActionEvent, ?> condition = p.getKey();
            final BiConsumer<?, AnActionEvent> handler = p.getValue();
            final Object result = condition.apply(source, e);
            if (Objects.nonNull(result)) {
                return handler;
            }
        }
        return null;
    }

    public <D> void registerHandler(@Nonnull Function<Object, D> condition, @Nonnull Consumer<D> handler) {
        this.handlers.add(Pair.of((d, e) -> condition.apply(d), (d, e) -> handler.accept((D) d)));
    }

    public <D> void registerHandler(@Nonnull BiFunction<Object, AnActionEvent, D> condition, @Nonnull BiConsumer<D, AnActionEvent> handler) {
        this.handlers.add(Pair.of(condition, handler));
    }
}
