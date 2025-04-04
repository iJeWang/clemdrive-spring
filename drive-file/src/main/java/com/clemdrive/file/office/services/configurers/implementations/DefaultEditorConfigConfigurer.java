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

import com.clemdrive.file.office.documentserver.models.filemodel.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.clemdrive.file.domain.UserFile;
import com.clemdrive.file.office.documentserver.managers.document.DocumentManager;
import com.clemdrive.file.office.documentserver.managers.template.TemplateManager;
import com.clemdrive.file.office.documentserver.models.enums.Action;
import com.clemdrive.file.office.documentserver.models.enums.Mode;
import com.clemdrive.file.office.documentserver.models.filemodel.EditorConfig;
import com.clemdrive.file.office.documentserver.util.file.FileUtility;
import com.clemdrive.file.office.mappers.Mapper;
import com.clemdrive.file.office.services.configurers.EditorConfigConfigurer;
import com.clemdrive.file.office.services.configurers.wrappers.DefaultCustomizationWrapper;
import com.clemdrive.file.office.services.configurers.wrappers.DefaultEmbeddedWrapper;
import com.clemdrive.file.office.services.configurers.wrappers.DefaultFileWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Primary
public class DefaultEditorConfigConfigurer implements EditorConfigConfigurer<DefaultFileWrapper> {

    @Autowired
    private Mapper<com.clemdrive.file.office.entities.User, User> mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    @Qualifier("sample")
    private TemplateManager templateManager;

    @Autowired
    private DefaultCustomizationConfigurer defaultCustomizationConfigurer;

    @Autowired
    private DefaultEmbeddedConfigurer defaultEmbeddedConfigurer;

    @Autowired
    private FileUtility fileUtility;

    @SneakyThrows
    public void configure(EditorConfig config, DefaultFileWrapper wrapper) {  // define the editorConfig configurer
//        if (wrapper.getActionData() != null) {  // check if the actionData is not empty in the editorConfig wrapper
//            config.setActionLink(objectMapper.readValue(wrapper.getActionData(), (JavaType) new TypeToken<HashMap<String, Object>>() { }.getType()));  // set actionLink to the editorConfig
//        }
//        String fileName = wrapper.getFileName();  // set the fileName parameter from the editorConfig wrapper
        UserFile userFile = wrapper.getUserFile();
//        String fileExt = fileUtility.getFileExtension(fileName);
        String fileName = userFile.getFileName() + "." + userFile.getExtendName();
        boolean userIsAnon = wrapper.getUser().getName().equals("Anonymous");  // check if the user from the editorConfig wrapper is anonymous or not

        config.setTemplates(userIsAnon ? null : templateManager.createTemplates(fileName));  // set a template to the editorConfig if the user is not anonymous
        config.setCallbackUrl(documentManager.getCallback(userFile.getUserFileId()));  // set the callback URL to the editorConfig
        config.setCreateUrl(userIsAnon ? null : documentManager.getCreateUrl(fileName, false));  // set the document URL where it will be created to the editorConfig if the user is not anonymous
        config.setLang(wrapper.getLang());  // set the language to the editorConfig
        Boolean canEdit = wrapper.getCanEdit();  // check if the file of the specified type can be edited or not
        Action action = wrapper.getAction();  // get the action parameter from the editorConfig wrapper
        config.setCoEditing(action.equals(Action.view) && userIsAnon ? new HashMap<String, Object>() {{
            put("mode", "strict");
            put("change", false);
        }} : new HashMap<String, Object>() {{
            put("mode", "fast");
            put("change", true);
        }});

        defaultCustomizationConfigurer.configure(config.getCustomization(), DefaultCustomizationWrapper.builder()  // define the customization configurer
                .action(action)
                .user(userIsAnon ? null : wrapper.getUser())
                .build());
        config.setMode(canEdit && !action.equals(Action.view) ? Mode.edit : Mode.view);
        config.setUser(mapper.toModel(wrapper.getUser()));
        defaultEmbeddedConfigurer.configure(config.getEmbedded(), DefaultEmbeddedWrapper.builder()
                .type(wrapper.getType())
                .fileName(fileName)
                .build());
    }
}
