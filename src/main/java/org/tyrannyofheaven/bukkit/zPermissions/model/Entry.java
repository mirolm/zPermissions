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
package org.tyrannyofheaven.bukkit.zPermissions.model;

/**
 * A permission entry.
 *
 * @author zerothangel
 */
public class Entry {

    private Long id;

    private PermissionEntity entity;

    private PermissionRegion region;

    private PermissionWorld world;

    private String permission;

    private boolean value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionEntity getEntity() {
        return entity;
    }

    public void setEntity(PermissionEntity owner) {
        this.entity = owner;
    }

    public PermissionRegion getRegion() {
        return region;
    }

    public void setRegion(PermissionRegion region) {
        this.region = region;
    }

    public PermissionWorld getWorld() {
        return world;
    }

    public void setWorld(PermissionWorld world) {
        this.world = world;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Entry)) return false;
        Entry o = (Entry) obj;
        return getEntity().equals(o.getEntity()) &&
                (getRegion() == null ? o.getRegion() == null : getRegion().equals(o.getRegion())) &&
                (getWorld() == null ? o.getWorld() == null : getWorld().equals(o.getWorld())) &&
                getPermission().equals(o.getPermission());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getEntity().hashCode();
        result = 37 * result + (getRegion() == null ? 0 : getRegion().hashCode());
        result = 37 * result + (getWorld() == null ? 0 : getWorld().hashCode());
        result = 37 * result + getPermission().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s -> %s",
                (getRegion() == null ? "" : getRegion().getName() + "/"),
                (getWorld() == null ? "" : getWorld().getName() + ":"),
                getPermission(),
                isValue());
    }

}
