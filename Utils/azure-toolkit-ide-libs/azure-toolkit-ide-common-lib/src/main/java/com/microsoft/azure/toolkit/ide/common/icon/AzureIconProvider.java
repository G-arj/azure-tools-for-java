/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
package com.microsoft.azure.toolkit.ide.common.icon;

public interface AzureIconProvider<T> {
    AzureIcon getIcon(T resource);
}
