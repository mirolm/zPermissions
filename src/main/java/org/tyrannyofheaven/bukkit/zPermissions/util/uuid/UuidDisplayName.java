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
package org.tyrannyofheaven.bukkit.zPermissions.util.uuid;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class UuidDisplayName {

    @SerializedName("id")
    private final UUID uuid;

    @SerializedName("name")
    private final String displayName;

    public UuidDisplayName(UUID uuid, String displayName) {
        if (uuid == null)
            throw new IllegalArgumentException("uuid cannot be null");

        this.uuid = uuid;
        this.displayName = displayName;
    }

    public UuidDisplayName(UuidDisplayName source) {
        if (source == null)
            throw new IllegalArgumentException("source cannot be null");

        this.uuid = source.uuid;
        this.displayName = source.displayName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

}
