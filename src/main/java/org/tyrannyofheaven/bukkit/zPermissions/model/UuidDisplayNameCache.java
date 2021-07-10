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
package org.tyrannyofheaven.bukkit.zPermissions.model;

import static org.tyrannyofheaven.bukkit.zPermissions.util.uuid.UuidUtils.uncanonicalizeUuid;

import java.util.Date;
import java.util.UUID;

import org.tyrannyofheaven.bukkit.zPermissions.util.uuid.UuidUtils;

public class UuidDisplayNameCache {

    private String name;

    private String displayName;

    private String uuidString;

    private Date timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUuidString() {
        return uuidString;
    }

    public void setUuidString(String uuid) {
        this.uuidString = uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getUuid() {
        return uncanonicalizeUuid(getUuidString());
    }

    public void setUuid(UUID uuid) {
        setUuidString(UuidUtils.canonicalizeUuid(uuid));
    }

}
