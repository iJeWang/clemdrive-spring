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

package com.clemdrive.file.office.documentserver.callbacks.implementations;

import com.clemdrive.file.office.documentserver.models.enums.Action;
import com.clemdrive.file.office.documentserver.callbacks.Callback;
import com.clemdrive.file.office.documentserver.callbacks.Status;
import com.clemdrive.file.office.documentserver.managers.callback.CallbackManager;
import com.clemdrive.file.office.dto.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditCallback implements Callback {
    @Autowired
    private CallbackManager callbackManager;

    @Override
    public int handle(Track body, String fileName) {  // handle the callback when the document is being edited
        int result = 0;
        com.clemdrive.file.office.dto.Action action = body.getActions().get(0);  // get the user ID who is editing the document
        if (action.getType().equals(Action.edit)) {  // if this value is not equal to the user ID
            String user = action.getUserid();  // get user ID
            if (!body.getUsers().contains(user)) {  // if this user is not specified in the body
                String key = body.getKey();  // get document key
                try {
                    callbackManager.commandRequest("forcesave", key, null);  // create a command request to forcibly save the document being edited without closing it
                } catch (Exception e) {
                    e.printStackTrace();
                    result = 1;
                }
            }
        }
        return result;
    }

    @Override
    public int getStatus() {  // get document status
        return Status.EDITING.getCode();  // return status 1 - document is being edited
    }
}
