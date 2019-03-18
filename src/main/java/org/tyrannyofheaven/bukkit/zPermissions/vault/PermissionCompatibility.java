package org.tyrannyofheaven.bukkit.zPermissions.vault;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.tyrannyofheaven.bukkit.zPermissions.PermissionsResolver;

// Implementation of all non-OfflinePlayer-based player methods
public abstract class PermissionCompatibility extends Permission {

    private final PermissionsResolver resolver;

    public PermissionCompatibility(PermissionsResolver resolver) {
        this.resolver = resolver;
    }

    @Deprecated
    @Override
    public boolean playerHas(String world, String player, String permission) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return playerHas(world, offlinePlayer, permission);
    }

    @Deprecated
    @Override
    public boolean playerAdd(String world, String player, String permission) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return playerAdd(world, offlinePlayer, permission);
    }

    @Deprecated
    @Override
    public boolean playerRemove(String world, String player, String permission) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return playerRemove(world, offlinePlayer, permission);
    }

    @Deprecated
    @Override
    public boolean playerInGroup(String world, String player, String group) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return playerInGroup(world, offlinePlayer, group);
    }

    @Deprecated
    @Override
    public boolean playerAddGroup(String world, String player, String group) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return playerAddGroup(world, offlinePlayer, group);
    }

    @Deprecated
    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return playerRemoveGroup(world, offlinePlayer, group);
    }

    @Deprecated
    @Override
    public String[] getPlayerGroups(String world, String player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPlayerGroups(world, offlinePlayer);
    }

    @Deprecated
    @Override
    public String getPrimaryGroup(String world, String player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return getPrimaryGroup(world, offlinePlayer);
    }

}
