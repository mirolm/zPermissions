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
 * Represents a world that permission entries may be associated with.
 *
 * @author zerothangel
 */
public class PermissionWorld {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PermissionWorld)) return false;
        PermissionWorld o = (PermissionWorld) obj;
        return getName() == null ? o.getName() == null : getName().equals(o.getName());
    }

    @Override
    public int hashCode() {
        return getName() == null ? 0 : getName().hashCode();
    }

    @Override
    public String toString() {
        return String.format("World[%s]", getName());
    }

}
