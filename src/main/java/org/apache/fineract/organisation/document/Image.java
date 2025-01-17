/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.organisation.document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.fineract.organisation.parent.AbstractPersistableCustom;

@Entity
@Table(name = "m_image")
public final class Image extends AbstractPersistableCustom {

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "storage_type_enum")
    private Integer storageType;

    public Image(final String location, final StorageType storageType) {
        this.location = location;
        this.storageType = storageType.getValue();
    }

    protected Image() {

    }

    public String getLocation() {
        return this.location;
    }

    public Integer getStorageType() {
        return this.storageType;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public void setStorageType(final Integer storageType) {
        this.storageType = storageType;
    }

}