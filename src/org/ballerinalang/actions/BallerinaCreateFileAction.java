/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.ballerinalang.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.ballerinalang.BallerinaIcons;
import org.ballerinalang.psi.BallerinaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BallerinaCreateFileAction extends CreateFileFromTemplateAction implements DumbAware {
    public static final String FILE_TEMPLATE = "Ballerina File";
    public static final String APPLICATION_TEMPLATE = "Ballerina Application";

    private static final String NEW_BALLERINA_FILE = "New Ballerina File";
    private static final String DEFAULT_BALLERINA_TEMPLATE_PROPERTY = FILE_TEMPLATE;

    public BallerinaCreateFileAction() {
        super(NEW_BALLERINA_FILE, "", BallerinaIcons.FILE);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory directory, @NotNull CreateFileFromTemplateDialog.Builder
            builder) {
        builder.setTitle(NEW_BALLERINA_FILE).addKind("Simple Application", BallerinaIcons.FILE, APPLICATION_TEMPLATE)
                .addKind("Empty file", BallerinaIcons.FILE, FILE_TEMPLATE);
    }

    @Nullable
    @Override
    protected String getDefaultTemplateProperty() {
        return DEFAULT_BALLERINA_TEMPLATE_PROPERTY;
    }

    @NotNull
    @Override
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return NEW_BALLERINA_FILE;
    }


    @Override
    protected void postProcess(PsiFile createdElement, String templateName, Map<String, String> customProperties) {
        if (createdElement instanceof BallerinaFile) {
            // Moving the caret to the end of the file.
            Project project = createdElement.getProject();
            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            if (editor == null) {
                return;
            }
            VirtualFile virtualFile = createdElement.getContainingFile().getVirtualFile();
            if (virtualFile == null) {
                return;
            }
            PsiElement lastChild = createdElement.getLastChild();
            if (FileDocumentManager.getInstance().getDocument(virtualFile) == editor.getDocument()) {
                editor.getCaretModel().moveToOffset(lastChild.getTextRange().getEndOffset());
            }
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BallerinaCreateFileAction;
    }
}
