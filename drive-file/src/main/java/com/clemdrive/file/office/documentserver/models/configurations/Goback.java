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

package com.clemdrive.file.office.documentserver.models.configurations;

import com.clemdrive.file.office.documentserver.storage.FileStoragePathBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("prototype")
public class Goback {  // the settings for the Open file location menu button and upper right corner button

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;


    @Getter
    private String url;  // the absolute URL to the website address which will be opened when clicking the Open file location menu button

    @PostConstruct
    private void init() {
        this.url = storagePathBuilder.getServerUrl(false);
    }
}
