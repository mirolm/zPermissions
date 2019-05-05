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

    private final LoadingCache<String, UuidDisplayName> cache;

    public MojangUuidResolver(int cacheMaxSize, long cacheTtl, TimeUnit cacheTtlUnits) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheTtl, cacheTtlUnits)
                .build(new CacheLoader<String, UuidDisplayName>() {
                    @Override
                    public UuidDisplayName load(String key) throws Exception {
                        return _resolve(key);
                    }
                });
    }

    @Override
    public UuidDisplayName resolve(String username) {
        if (!hasText(username))
            throw new IllegalArgumentException("username must have a value");

        try {
            return cache.get(username.toLowerCase());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UuidDisplayName resolve(String username, boolean cacheOnly) {
        if (!hasText(username))
            throw new IllegalArgumentException("username must have a value");

        if (cacheOnly) {
            return cache.asMap().get(username.toLowerCase());
        } else {
            return resolve(username); // Same as normal version
        }
    }

    @Override
    public Map<String, UuidDisplayName> resolve(Collection<String> usernames) throws Exception {
        if (usernames == null)
            throw new IllegalArgumentException("usernames cannot be null");

        Map<String, UuidDisplayName> result = new LinkedHashMap<>();

        final int BATCH_SIZE = 97; // Should be <= Mojang's AccountsClient's PROFILES_PER_REQUEST (100)

        for (List<String> sublist : Lists.partition(new ArrayList<>(usernames), BATCH_SIZE)) {
            result.putAll(searchProfiles(sublist));
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

        Map<String, UuidDisplayName> result = new LinkedHashMap<>(searchProfiles(Collections.singletonList(username)));

        return result.get(username.toLowerCase());
    }

    private Map<String, UuidDisplayName> searchProfiles(List<String> usernames) throws IOException, JsonIOException, JsonSyntaxException {
        Gson gson = new Gson();

        URL url = new URL("https://api.mojang.com/profiles/minecraft");
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

        Map<String, UuidDisplayName> searchResult = new LinkedHashMap<>();
        for (JsonElement element : profiles) {
            JsonObject profile = element.getAsJsonObject();
            UUID uuid = uncanonicalizeUuid(profile.get("id").getAsString());
            String username = profile.get("name").getAsString();
            searchResult.put(username.toLowerCase(), new UuidDisplayName(uuid, username));
        }

        return searchResult;
    }

}
