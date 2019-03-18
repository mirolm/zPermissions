package org.tyrannyofheaven.bukkit.zPermissions.vault;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

// Implementation of all non-OfflinePlayer-based player methods
public abstract class ChatCompatibility extends Chat {

    public ChatCompatibility(Permission perms) {
        super(perms);
    }

    @Deprecated
    @Override
    public String getPlayerPrefix(String world, String player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPlayerPrefix(world, offlinePlayer);
    }

    @Deprecated
    @Override
    public void setPlayerPrefix(String world, String player, String prefix) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        setPlayerPrefix(world, offlinePlayer, prefix);
    }

    @Deprecated
    @Override
    public String getPlayerSuffix(String world, String player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPlayerSuffix(world, offlinePlayer);
    }

    @Deprecated
    @Override
    public void setPlayerSuffix(String world, String player, String suffix) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        setPlayerSuffix(world, offlinePlayer, suffix);
    }

    @Deprecated
    @Override
    public int getPlayerInfoInteger(String world, String player, String node, int defaultValue) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPlayerInfoInteger(world, offlinePlayer, node, defaultValue);
    }

    @Deprecated
    @Override
    public void setPlayerInfoInteger(String world, String player, String node, int value) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        setPlayerInfoInteger(world, offlinePlayer, node, value);
    }

    @Deprecated
    @Override
    public double getPlayerInfoDouble(String world, String player, String node, double defaultValue) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPlayerInfoDouble(world, offlinePlayer, node, defaultValue);
    }

    @Deprecated
    @Override
    public void setPlayerInfoDouble(String world, String player, String node, double value) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        setPlayerInfoDouble(world, offlinePlayer, node, value);
    }

    @Deprecated
    @Override
    public boolean getPlayerInfoBoolean(String world, String player, String node, boolean defaultValue) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPlayerInfoBoolean(world, offlinePlayer, node, defaultValue);
    }

    @Deprecated
    @Override
    public void setPlayerInfoBoolean(String world, String player, String node, boolean value) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        setPlayerInfoBoolean(world, offlinePlayer, node, value);
    }

    @Deprecated
    @Override
    public String getPlayerInfoString(String world, String player, String node, String defaultValue) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPlayerInfoString(world, offlinePlayer, node, defaultValue);
    }

    @Deprecated
    @Override
    public void setPlayerInfoString(String world, String player, String node, String value) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        setPlayerInfoString(world, offlinePlayer, node, value);
    }

}
