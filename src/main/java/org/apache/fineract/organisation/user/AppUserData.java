/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.organisation.user;

import org.apache.fineract.organisation.office.OfficeData;
import org.apache.fineract.organisation.role.RoleData;
import org.apache.fineract.organisation.staff.StaffData;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
public class AppUserData {

    private Long id;
    private String username;
    private Long officeId;
    private String officeName;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean passwordNeverExpires;
    private Collection<OfficeData> allowedOffices;
    private Collection<RoleData> availableRoles;
    private Collection<RoleData> selectedRoles;
    private StaffData staff;

    public AppUserData() {}
    
    public AppUserData(Long id, String username, Long officeId, String officeName, String firstname, String lastname, String email, Boolean passwordNeverExpires, Collection<OfficeData> allowedOffices, Collection<RoleData> availableRoles, Collection<RoleData> selectedRoles, StaffData staff) {
        this.id = id;
        this.username = username;
        this.officeId = officeId;
        this.officeName = officeName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passwordNeverExpires = passwordNeverExpires;
        this.allowedOffices = allowedOffices;
        this.availableRoles = availableRoles;
        this.selectedRoles = selectedRoles;
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserData that = (AppUserData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(officeId, that.officeId) &&
                Objects.equals(officeName, that.officeName) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(passwordNeverExpires, that.passwordNeverExpires) &&
                Objects.equals(allowedOffices, that.allowedOffices) &&
                Objects.equals(availableRoles, that.availableRoles) &&
                Objects.equals(selectedRoles, that.selectedRoles) &&
                Objects.equals(staff, that.staff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, officeId, officeName, firstname, lastname, email, passwordNeverExpires, allowedOffices, availableRoles, selectedRoles, staff);
    }
}