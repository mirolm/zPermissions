/*
 * Copyright 2011 ZerothAngel <zerothangel@tyrannyofheaven.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tyrannyofheaven.bukkit.zPermissions.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.tyrannyofheaven.bukkit.zPermissions.model.EntityMetadata;
import org.tyrannyofheaven.bukkit.zPermissions.model.Entry;
import org.tyrannyofheaven.bukkit.zPermissions.model.Membership;
import org.tyrannyofheaven.bukkit.zPermissions.model.PermissionEntity;

/**
 * Mid-tier service interface for zPermissions permissions store.
 *
 * @author zerothangel
 */
public interface PermissionService {

    Boolean getPermission(String name, UUID uuid, boolean group, String region, String world, String permission);

    void setPermission(String name, UUID uuid, boolean group, String region, String world, String permission, boolean value);

    boolean unsetPermission(String name, UUID uuid, boolean group, String region, String world, String permission);

    void addMember(String groupName, UUID memberUuid, String memberName, Date expiration);

    boolean removeMember(String groupName, UUID memberUuid);

    // NB: Resolver critical path
    List<Membership> getGroups(UUID memberUuid);

    List<Membership> getMembers(String group);

    PermissionEntity getEntity(String name, UUID uuid, boolean group);

    List<PermissionEntity> getEntities(boolean group);

    void setGroup(UUID playerUuid, String playerName, String groupName, Date expiration);

    // Technically deprecated
    void setParent(String groupName, String parentName);

    void setParents(String groupName, List<String> parentNames);

    void setPriority(String groupName, int priority);

    boolean deleteEntity(String name, UUID uuid, boolean group);

    // NB: Resolver critical path
    List<String> getAncestry(String groupName);

    // NB: Resolver critical path
    List<Entry> getEntries(String name, UUID uuid, boolean group);

    boolean createGroup(String name);

    List<String> getEntityNames(boolean group);

    Object getMetadata(String name, UUID uuid, boolean group, String metadataName);

    List<EntityMetadata> getAllMetadata(String name, UUID uuid, boolean group);

    void setMetadata(String name, UUID uuid, boolean group, String metadataName, Object value);

    boolean unsetMetadata(String name, UUID uuid, boolean group, String metadataName);

    void updateDisplayName(UUID uuid, String displayName);

}
