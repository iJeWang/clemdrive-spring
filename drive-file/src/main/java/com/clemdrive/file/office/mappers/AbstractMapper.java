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

import com.clemdrive.file.office.documentserver.models.AbstractModel;
import com.clemdrive.file.office.entities.AbstractEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class AbstractMapper<E extends AbstractEntity, M extends AbstractModel> implements Mapper<E, M> {
    @Autowired
    ModelMapper mapper;

    private Class<M> modelClass;

    AbstractMapper(Class<M> modelClass) {
        this.modelClass = modelClass;
    }

    @Override
    public M toModel(E entity) {  // convert the entity to the model
        return Objects.isNull(entity)  // check if an entity is not empty
                ? null
                : mapper.map(entity, modelClass);  // and add it to the model mapper
    }

    Converter<E, M> modelConverter() {  // specify the model converter
        return context -> {
            E source = context.getSource();  // get the source entity
            M destination = context.getDestination();  // get the destination model
            handleSpecificFields(source, destination);  // map the entity to the model
            return context.getDestination();
        };
    }


    void handleSpecificFields(E source, M destination) {
    }
}
