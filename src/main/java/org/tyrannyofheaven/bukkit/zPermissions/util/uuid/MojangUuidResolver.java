/*
 * Largely based off Mojang's AccountsClient code.
 * https://github.com/Mojang/AccountsClient
 */
package org.tyrannyofheaven.bukkit.zPermissions.util.uuid;

import static org.tyrannyofheaven.bukkit.zPermissions.util.ToHStringUtils.hasText;
import static org.tyrannyofheaven.bukkit.zPermissions.util.uuid.UuidUtils.uncanonicalizeUuid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

public class MojangUuidResolver implements UuidResolver {

    private static final String AGENT = "minecraft";

    private static final UuidDisplayName NULL_UDN = new UuidDisplayName(UUID.randomUUID(), "NOT FOUND");

    private final LoadingCache<String, UuidDisplayName> cache;

    public MojangUuidResolver(int cacheMaxSize, long cacheTtl, TimeUnit cacheTtlUnits) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheTtl, cacheTtlUnits)
                .build(new CacheLoader<String, UuidDisplayName>() {
                    @Override
                    public UuidDisplayName load(String key) throws Exception {
                        UuidDisplayName udn = _resolve(key);
                        return udn != null ? udn : NULL_UDN; // Doesn't like nulls, so we use a marker object instead
                    }
                });
    }

    @Override
    public UuidDisplayName resolve(String username) {
        if (!hasText(username))
            throw new IllegalArgumentException("username must have a value");

        try {
            UuidDisplayName udn = cache.get(username.toLowerCase());
            return udn != NULL_UDN ? udn : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UuidDisplayName resolve(String username, boolean cacheOnly) {
        if (!hasText(username))
            throw new IllegalArgumentException("username must have a value");

        if (cacheOnly) {
            UuidDisplayName udn = cache.asMap().get(username.toLowerCase());
            if (udn == null) return null;
            return udn != NULL_UDN ? udn : null; // NB Can't tell between "not cached" and "maps to null"
        } else return resolve(username); // Same as normal version
    }

    @Override
    public Map<String, UuidDisplayName> resolve(Collection<String> usernames) throws Exception {
        if (usernames == null)
            throw new IllegalArgumentException("usernames cannot be null");

        Map<String, UuidDisplayName> result = new LinkedHashMap<>();

        final int BATCH_SIZE = 97; // Should be <= Mojang's AccountsClient's PROFILES_PER_REQUEST (100)

        for (List<String> sublist : Lists.partition(new ArrayList<>(usernames), BATCH_SIZE)) {
            List<Profile> searchResult = searchProfiles(sublist);
            for (Profile profile : searchResult) {
                String username = profile.getName();
                UUID uuid = uncanonicalizeUuid(profile.getId());
                result.put(username.toLowerCase(), new UuidDisplayName(uuid, username));
            }
        }

        return result;
    }

    @Override
    public void preload(String username, UUID uuid) {
        if (!hasText(username))
            throw new IllegalArgumentException("username must have a value");
        if (uuid == null)
            throw new IllegalArgumentException("uuid cannot be null");

        cache.asMap().put(username.toLowerCase(), new UuidDisplayName(uuid, username));
    }

    @Override
    public void invalidate(String username) {
        if (!hasText(username))
            throw new IllegalArgumentException("username must have a value");

        cache.invalidate(username.toLowerCase());
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    private UuidDisplayName _resolve(String username) throws Exception {
        if (!hasText(username))
            throw new IllegalArgumentException("username must have a value");

        List<Profile> result = searchProfiles(Collections.singletonList(username));

        if (result.size() < 1) return null;

        // TODO what to do if there are >1?
        Profile p = result.get(0);

        String uuidString = p.getId();
        UUID uuid;
        try {
            uuid = uncanonicalizeUuid(uuidString);
        } catch (IllegalArgumentException e) {
            return null;
        }

        String displayName = hasText(p.getName()) ? p.getName() : username;

        return new UuidDisplayName(uuid, displayName);
    }

    private List<Profile> searchProfiles(List<String> usernames) throws IOException, JsonIOException, JsonSyntaxException {
        Gson gson = new Gson();

        URL url = new URL("https://api.mojang.com/profiles/" + AGENT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(connection.getOutputStream(), Charsets.UTF_8))) {
            gson.toJson(usernames, writer);
        }

        JsonArray profiles;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), Charsets.UTF_8))) {
            profiles = gson.fromJson(reader, JsonArray.class);
        }

        return convertResponse(profiles);
    }

    private List<Profile> convertResponse(JsonArray profiles) {
        List<Profile> result = new ArrayList<>();
        for (JsonElement element : profiles) {
            JsonObject profile = element.getAsJsonObject();
            String id = profile.get("id").getAsString();
            String name = profile.get("name").getAsString();
            result.add(new Profile(id, name));
        }
        return result;
    }

    private static class Profile {

        private final String id;

        private final String name;

        private Profile(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}
