/*
 * Copyright 2014 ZerothAngel <zerothangel@tyrannyofheaven.org>
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

import java.util.Collection;

import org.tyrannyofheaven.bukkit.zPermissions.model.EntityMetadata;
import org.tyrannyofheaven.bukkit.zPermissions.model.Entry;
import org.tyrannyofheaven.bukkit.zPermissions.model.Inheritance;
import org.tyrannyofheaven.bukkit.zPermissions.model.Membership;
import org.tyrannyofheaven.bukkit.zPermissions.model.PermissionEntity;
import org.tyrannyofheaven.bukkit.zPermissions.model.PermissionRegion;
import org.tyrannyofheaven.bukkit.zPermissions.model.PermissionWorld;

/**
 * Data-access interface for zPermissions. Mainly concerned with CUD operations
 * (that is, CRUD without the R).
 *
 * @author zerothangel
 */
public interface PermissionDao {

    void createRegion(PermissionRegion region);

    void createWorld(PermissionWorld world);

    void createEntity(PermissionEntity entity);

    void createOrUpdateEntry(Entry entry);

    void deleteEntry(Entry entry);

    void createOrUpdateMembership(Membership membership);

    void setEntityParent(PermissionEntity entity, PermissionEntity parent);

    void createOrUpdateInheritance(Inheritance inheritance);

    void deleteInheritance(Inheritance inheritance);

    void setEntityPriority(PermissionEntity entity, int priority);

    void deleteRegions(Collection<PermissionRegion> regions);

    void deleteWorlds(Collection<PermissionWorld> worlds);

    void deleteEntity(PermissionEntity entity);

    void deleteMembership(Membership membership);

    void createOrUpdateMetadata(EntityMetadata metadata);

    void deleteMetadata(EntityMetadata metadata);

    void updateDisplayName(PermissionEntity entity);

    void updateDisplayName(Membership membership);

}