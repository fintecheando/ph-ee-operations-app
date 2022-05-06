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

import org.apache.fineract.organisation.role.Role;
import org.apache.fineract.organisation.role.RoleRepository;
import org.apache.fineract.organisation.user.AppUser;
import org.apache.fineract.organisation.user.AppUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UsersApi {

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appuserRepository;
    private final RoleRepository roleRepository;

    @GetMapping(path = OperationsConstants.API_USERS_PATH, produces = MediaType.APPLICATION_JSON)
    public List<AppUser> retrieveAll() {
        return this.appuserRepository.findAll();
    }

    @GetMapping(path = OperationsConstants.API_USERS_PATH + "/{userId}", produces = MediaType.APPLICATION_JSON)
    public AppUser retrieveOne(@PathVariable("userId") Long userId, HttpServletResponse response) {
        Optional<AppUser> optEntity = appuserRepository.findById(userId);
        if (optEntity.isPresent()) {
            return optEntity.get();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @GetMapping(path = OperationsConstants.API_USERS_PATH + "/{userId}/roles", produces = MediaType.APPLICATION_JSON)
    public Collection<Role> retrieveRoles(@PathVariable("userId") Long userId, HttpServletResponse response) {
        Optional<AppUser> optEntity = appuserRepository.findById(userId);
        if (optEntity.isPresent()) {
            return optEntity.get().getRoles();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @PostMapping(path = OperationsConstants.API_USERS_PATH, consumes = MediaType.APPLICATION_JSON)
    public void create(@RequestBody AppUser appUser, HttpServletResponse response) {
        AppUser existing = appuserRepository.findAppUserByName(appUser.getUsername());
        if (existing == null) {
            // TODO enforce password policy
            appUser.setId(null);
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            appuserRepository.saveAndFlush(appUser);
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @PutMapping(path = OperationsConstants.API_USERS_PATH + "/{userId}", consumes = MediaType.APPLICATION_JSON)
    public void update(@PathVariable("userId") Long userId, @RequestBody AppUser appUser, HttpServletResponse response) {
        Optional<AppUser> optEntity = appuserRepository.findById(userId);
        if (optEntity.isPresent()) {
            AppUser existing = optEntity.get();
            appUser.setId(userId);
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            appUser.setRoles(existing.getRoles());
            appuserRepository.saveAndFlush(appUser);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping(path = OperationsConstants.API_USERS_PATH + "/{userId}", produces = MediaType.TEXT_HTML)
    public void delete(@PathVariable("userId") Long userId, HttpServletResponse response) {
        Optional<AppUser> optEntity = appuserRepository.findById(userId);
        if (optEntity.isPresent()) {
            appuserRepository.delete(optEntity.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @PutMapping(path = OperationsConstants.API_USERS_PATH + "/{userId}/roles", consumes = MediaType.APPLICATION_JSON)
    public void userAssignment(@PathVariable("userId") Long userId, @RequestParam("action") AssignmentAction action,
                               @RequestBody EntityAssignments assignments, HttpServletResponse response) {
        Optional<AppUser> optEntity = appuserRepository.findById(userId);
        if (optEntity.isPresent()) {
            AppUser existingUser = optEntity.get();
            Collection<Role> rolesToAssign = existingUser.getRoles();
            List<Long> existingRoleIds = rolesToAssign.stream()
                    .map(Role::getId)
                    .collect(toList());
            List<Role> deltaRoles = assignments.getEntityIds().stream()
                    .filter(id -> {
                        if (ASSIGN.equals(action)) {
                            return !existingRoleIds.contains(id);
                        } else { // revoke
                            return existingRoleIds.contains(id);
                        }
                    })
                    .map(id -> {
                        Optional<Role> optRole = roleRepository.findById(id);
                        if (!optRole.isPresent()) {
                            throw new RuntimeException("Invalid role id: " + id + " can not continue assignment!");
                        } else {
                            return optRole.get();
                        }
                    }).collect(toList());

            if (!deltaRoles.isEmpty()) {
                if (ASSIGN.equals(action)) {
                    rolesToAssign.addAll(deltaRoles);
                } else { // revoke
                    rolesToAssign.removeAll(deltaRoles);
                }
                existingUser.setRoles(rolesToAssign);
                appuserRepository.saveAndFlush(existingUser);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}