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

import com.clemdrive.file.office.documentserver.callbacks.Callback;
import com.clemdrive.file.office.documentserver.callbacks.Status;
import com.clemdrive.file.office.documentserver.managers.callback.CallbackManager;
import com.clemdrive.file.office.dto.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForcesaveCallback implements Callback {
    @Autowired
    private CallbackManager callbackManager;

    @Override
    public int handle(Track body, String fileName) {  // handle the callback when the force saving request is performed
        int result = 0;
        try {
            callbackManager.processForceSave(body, fileName);  // file force saving process
        } catch (Exception ex) {
            ex.printStackTrace();
            result = 1;
        }
        return result;
    }

    @Override
    public int getStatus() {  // get document status
        return Status.MUST_FORCE_SAVE.getCode();  // return status 6 - document is being edited, but the current document state is saved
    }
}
