/**
 * (c) Copyright Ascensio System SIA 2021
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clemdrive.file.office.documentserver.managers.template;

import com.clemdrive.file.office.documentserver.managers.document.DocumentManager;
import com.clemdrive.file.office.documentserver.models.enums.DocumentType;
import com.clemdrive.file.office.documentserver.models.filemodel.Template;
import com.clemdrive.file.office.documentserver.storage.FileStoragePathBuilder;
import com.clemdrive.file.office.documentserver.util.file.FileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("sample")
public class SampleTemplateManager implements TemplateManager {
    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;

    @Autowired
    private FileUtility fileUtility;

    // create a template document with the specified name
    public List<Template> createTemplates(String fileName) {
        List<Template> templates = new ArrayList<>();
        templates.add(new Template("", "Blank", documentManager.getCreateUrl(fileName, false)));
        templates.add(new Template(getTemplateImageUrl(fileName), "With sample content", documentManager.getCreateUrl(fileName, true)));

        return templates;
    }

    // get the template image URL for the specified file
    public String getTemplateImageUrl(String fileName) {
        DocumentType fileType = fileUtility.getDocumentType(fileName);  // get the file type
        String path = storagePathBuilder.getServerUrl(true);  // get server URL
        if (fileType.equals(DocumentType.word)) {  // get URL to the template image for the word document type
            return path + "/css/img/file_docx.svg";
        } else if (fileType.equals(DocumentType.slide)) {  // get URL to the template image for the slide document type
            return path + "/css/img/file_pptx.svg";
        } else if (fileType.equals(DocumentType.cell)) {  // get URL to the template image for the cell document type
            return path + "/css/img/file_xlsx.svg";
        }
        return path + "/css/img/file_docx.svg";  // get URL to the template image for the default document type (word)
    }
}
