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
package org.apache.fineract.paymenthub.api;


import org.apache.fineract.organisation.permission.Permission;
import org.apache.fineract.organisation.permission.PermissionRepository;
import org.apache.fineract.organisation.role.Role;
import org.apache.fineract.organisation.role.RoleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.apache.fineract.paymenthub.api.AssignmentAction.ASSIGN;

@RestController
@RequestMapping(value=OperationsConstants.API_VERSION_PATH, produces=MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Profile(OperationsConstants.API_STANDALONE_LOCAL)
public class RolesApi {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @GetMapping(path = OperationsConstants.API_ROLES_PATH, produces = MediaType.APPLICATION_JSON)
    public List<Role> retrieveAll() {
        return this.roleRepository.findAll();
    }

    @GetMapping(path = OperationsConstants.API_ROLES_PATH + "/{roleId}", produces = MediaType.APPLICATION_JSON)
    public Role retrieveOne(@PathVariable("roleId") Long roleId, HttpServletResponse response) {
        Optional<Role> optEntity = roleRepository.findById(roleId);
        if(optEntity.isPresent()) {
            return optEntity.get();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @GetMapping(path = OperationsConstants.API_ROLES_PATH + "/{roleId}/permissions", produces = MediaType.APPLICATION_JSON)
    public Collection<Permission> retrievePermissions(@PathVariable("roleId") Long roleId, HttpServletResponse response) {
        Optional<Role> optEntity = roleRepository.findById(roleId);
        if(optEntity.isPresent()) {
            return optEntity.get().getPermissions();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @PostMapping(path = OperationsConstants.API_ROLES_PATH, consumes = MediaType.APPLICATION_JSON)
    public void create(@RequestBody Role role, HttpServletResponse response) {
        Role existing = roleRepository.getRoleByName(role.getName());
        if (existing == null) {
            role.setId(null);
            roleRepository.saveAndFlush(role);
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @PutMapping(path = OperationsConstants.API_ROLES_PATH + "/{roleId}", consumes = MediaType.APPLICATION_JSON)
    public void update(@PathVariable("roleId") Long roleId, @RequestBody Role role, HttpServletResponse response) {
        Optional<Role> optEntity = roleRepository.findById(roleId);
        if(optEntity.isPresent()) {
            Role existing = optEntity.get();
            role.setId(roleId);
            role.setAppUsers(existing.getAppusers());
            role.setPermissions(existing.getPermissions());
            roleRepository.saveAndFlush(role);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping(path = OperationsConstants.API_ROLES_PATH + "/{roleId}")
    public void delete(@PathVariable("roleId") Long roleId, HttpServletResponse response) {
        Optional<Role> optEntity = roleRepository.findById(roleId);
        if(optEntity.isPresent()) {
            roleRepository.delete(optEntity.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @PutMapping(path = OperationsConstants.API_ROLES_PATH + "/{roleId}/permissions", consumes = MediaType.APPLICATION_JSON)
    public void permissionAssignment(@PathVariable("roleId") Long roleId, @RequestParam("action") AssignmentAction action,
                                     @RequestBody EntityAssignments assignments, HttpServletResponse response) {
        Optional<Role> optEntity = roleRepository.findById(roleId);
        if(optEntity.isPresent()) {
            Role existingRole = optEntity.get();
            Collection<Permission> permissionToAssign = existingRole.getPermissions();
            List<Long> existingPermissionIds = permissionToAssign.stream()
                    .map(Permission::getId)
                    .collect(toList());
            List<Permission> deltaPermissions = assignments.getEntityIds().stream()
                    .filter(id -> {
                        if (ASSIGN.equals(action)) {
                            return !existingPermissionIds.contains(id);
                        } else { // revoke
                            return existingPermissionIds.contains(id);
                        }
                    })
                    .map(id -> {
                        Optional<Permission> optPermission = permissionRepository.findById(id);
                        if (!optPermission.isPresent()) {
                            throw new RuntimeException("Invalid permission id: " + id + " can not continue assignment!");
                        } else {
                            return optPermission.get();
                        }
                    }).collect(toList());

            if (!deltaPermissions.isEmpty()) {
                if (ASSIGN.equals(action)) {
                    permissionToAssign.addAll(deltaPermissions);
                } else { // revoke
                    permissionToAssign.removeAll(deltaPermissions);
                }
                existingRole.setPermissions(permissionToAssign);
                roleRepository.saveAndFlush(existingRole);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}