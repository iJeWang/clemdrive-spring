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

package com.clemdrive.file.office.mappers;

import com.clemdrive.file.office.documentserver.models.filemodel.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Primary
public class UsersMapper extends AbstractMapper<com.clemdrive.file.office.entities.User, User> {
    @Autowired
    private ModelMapper mapper;

    public UsersMapper() {
        super(User.class);
    }

    @PostConstruct
    public void configure() {  // configure the users mapper
        mapper.createTypeMap(com.clemdrive.file.office.entities.User.class, User.class)  // create the type map
                .setPostConverter(modelConverter());  // and apply the post converter to it
    }

    @Override
    public void handleSpecificFields(com.clemdrive.file.office.entities.User source, User destination) {  // handle specific users fields
//        destination.setGroup(source.getGroup() != null ? source.getGroup().getName() : null);  // set the Group parameter
    }
}
