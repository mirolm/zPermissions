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

import static org.tyrannyofheaven.bukkit.zPermissions.util.uuid.UuidUtils.canonicalizeUuid;
import static org.tyrannyofheaven.bukkit.zPermissions.util.uuid.UuidUtils.uncanonicalizeUuid;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class UUIDTypeAdapter extends TypeAdapter<UUID> {
    public void write(JsonWriter out, UUID value) throws IOException {
        TypeAdapters.STRING.write(out, canonicalizeUuid(value));
    }

    public UUID read(JsonReader in) throws IOException {
        return uncanonicalizeUuid(TypeAdapters.STRING.read(in));
    }
}