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

package com.clemdrive.file.office.services.configurers.implementations;

import com.clemdrive.file.office.documentserver.managers.document.DocumentManager;
import com.clemdrive.file.office.documentserver.models.configurations.Embedded;
import com.clemdrive.file.office.documentserver.models.enums.ToolbarDocked;
import com.clemdrive.file.office.documentserver.models.enums.Type;
import com.clemdrive.file.office.services.configurers.EmbeddedConfigurer;
import com.clemdrive.file.office.services.configurers.wrappers.DefaultEmbeddedWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DefaultEmbeddedConfigurer implements EmbeddedConfigurer<DefaultEmbeddedWrapper> {

    @Autowired
    private DocumentManager documentManager;

    public void configure(Embedded embedded, DefaultEmbeddedWrapper wrapper) {  // define the embedded configurer
        if (wrapper.getType().equals(Type.embedded)) {  // check if the type from the embedded wrapper is embedded
            String url = documentManager.getDownloadUrl(wrapper.getFileName(), false);  // get file URL of the specified file
            embedded.setEmbedUrl(url);  // set the embedURL parameter to the embedded config (the absolute URL to the document serving as a source file for the document embedded into the web page)
            embedded.setSaveUrl(url);  // set the saveURL parameter to the embedded config (the absolute URL that will allow the document to be saved onto the user personal computer)
            embedded.setShareUrl(url);  // set the shareURL parameter to the embedded config (the absolute URL that will allow other users to share this document)
            embedded.setToolbarDocked(ToolbarDocked.top);  // set the top toolbarDocked parameter to the embedded config (the place for the embedded viewer toolbar, can be either top or bottom)
        }
        ;
    }
}
