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

package com.clemdrive.file.office.services;

import com.clemdrive.file.office.entities.Group;
import com.clemdrive.file.office.entities.Permission;
import com.clemdrive.file.office.repositories.PermissionRepository;

import java.util.List;

//@Service
public class PermissionServices {

    //    @Autowired
    private PermissionRepository permissionRepository;

    // create permissions with the specified parameters
    public Permission createPermission(List<Group> reviewGroups,
                                       List<Group> commentViewGroups,
                                       List<Group> commentEditGroups,
                                       List<Group> commentRemoveGroups,
                                       List<Group> userInfoGroups,
                                       Boolean chat) {

        Permission permission = new Permission();
        permission.setReviewGroups(reviewGroups);  // define the groups whose changes the user can accept/reject
        permission.setCommentsViewGroups(commentViewGroups);  // defines the groups whose comments the user can view
        permission.setCommentsEditGroups(commentEditGroups);  // defines the groups whose comments the user can edit
        permission.setCommentsRemoveGroups(commentRemoveGroups);  // defines the groups whose comments the user can remove
        permission.setUserInfoGroups(userInfoGroups);
        permission.setChat(chat);

//        permissionRepository.save(permission);  // save new permissions

        return permission;
    }

    // update permissions
    public Permission updatePermission(Permission newPermission) {
//        permissionRepository.save(newPermission);  // save new permissions

        return newPermission;
    }
}
