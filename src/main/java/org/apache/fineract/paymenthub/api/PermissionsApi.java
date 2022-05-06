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
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value=OperationsConstants.API_VERSION_PATH, produces=MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Profile(OperationsConstants.API_STANDALONE_LOCAL)
public class PermissionsApi {

    private final PermissionRepository permissionRepository;

    @GetMapping(path = OperationsConstants.API_PERMISSIONS_PATH, produces = MediaType.APPLICATION_JSON)
    public List<Permission> retrieveAll() {
        return this.permissionRepository.findAll();
    }

    @GetMapping(path = OperationsConstants.API_PERMISSIONS_PATH + "/{permissionId}", produces = MediaType.APPLICATION_JSON)
    public Permission retrieveOne(@PathVariable("permissionId") Long permissionId, HttpServletResponse response) {        
        Optional<Permission> optEntity = permissionRepository.findById(permissionId);
        if (optEntity.isPresent()) {
            return optEntity.get();
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    @PostMapping(path = OperationsConstants.API_PERMISSIONS_PATH, consumes = MediaType.APPLICATION_JSON)
    public void create(@RequestBody Permission permission, HttpServletResponse response) {
        Permission existing = permissionRepository.findOneByCode(permission.getCode());
        if (existing == null) {
            permission.setId(null);
            permissionRepository.saveAndFlush(permission);
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @PutMapping(path = OperationsConstants.API_PERMISSIONS_PATH + "/{permissionId}", consumes = MediaType.APPLICATION_JSON)
    public void update(@PathVariable("permissionId") Long permissionId, @RequestBody Permission permission, HttpServletResponse response) {
        Optional<Permission> optEntity = permissionRepository.findById(permissionId);
        if (optEntity.isPresent()) {
            Permission existing = optEntity.get();
            permission.setId(permissionId);
            permission.setRoles(existing.getRoles());
            permissionRepository.saveAndFlush(permission);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping(path = OperationsConstants.API_PERMISSIONS_PATH + "/{permissionId}")
    public void delete(@PathVariable("permissionId") Long permissionId, HttpServletResponse response) {
        Optional<Permission> optEntity = permissionRepository.findById(permissionId);
        if (optEntity.isPresent()) {
            permissionRepository.delete(optEntity.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}