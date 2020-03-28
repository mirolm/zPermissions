/*
 * Copyright 2012 ZerothAngel <zerothangel@tyrannyofheaven.org>
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
package org.tyrannyofheaven.bukkit.zPermissions.util;

import static org.tyrannyofheaven.bukkit.zPermissions.util.ToHMessageUtils.colorize;
import static org.tyrannyofheaven.bukkit.zPermissions.util.ToHUtils.assertFalse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.tyrannyofheaven.bukkit.zPermissions.util.command.ParseException;
import org.tyrannyofheaven.bukkit.zPermissions.util.uuid.UuidUtils;
import org.tyrannyofheaven.bukkit.zPermissions.dao.PermissionService;
import org.tyrannyofheaven.bukkit.zPermissions.model.EntityMetadata;
import org.tyrannyofheaven.bukkit.zPermissions.model.Entry;
import org.tyrannyofheaven.bukkit.zPermissions.model.Membership;
import org.tyrannyofheaven.bukkit.zPermissions.model.PermissionEntity;

/**
 * Collection of static utils, constants, etc.
 *
 * @author zerothangel
 */
public class Utils {

    private final static Comparator<PermissionEntity> PERMISSION_ENTITY_ALPHA_COMPARATOR = new Comparator<PermissionEntity>() {
        @Override
        public int compare(PermissionEntity a, PermissionEntity b) {
            return a.getName().compareTo(b.getName()); // In the case of players, sort by UUID
        }
    };

    private final static Pattern DURATION_PATTERN = Pattern.compile("^(\\d+)\\s*(min(?:ute)?s?|h(?:ours?)?|d(?:ays?)?|m(?:onths?)?|y(?:ears?)?)?$", Pattern.CASE_INSENSITIVE);

    public final static Comparator<Entry> ENTRY_COMPARATOR = new Comparator<Entry>() {
        @Override
        public int compare(Entry a, Entry b) {
            if (a.getRegion() != null && b.getRegion() == null)
                return 1;
            else if (a.getRegion() == null && b.getRegion() != null)
                return -1;
            else if (a.getRegion() != null && b.getRegion() != null) {
                int regions = a.getRegion().getName().compareTo(b.getRegion().getName());
                if (regions != 0) return regions;
            }

            if (a.getWorld() != null && b.getWorld() == null)
                return 1;
            else if (a.getWorld() == null && b.getWorld() != null)
                return -1;
            else if (a.getWorld() != null && b.getWorld() != null) {
                int worlds = a.getWorld().getName().compareTo(b.getWorld().getName());
                if (worlds != 0) return worlds;
            }

            return a.getPermission().compareTo(b.getPermission());
        }
    };

    private final static Comparator<PermissionInfo> PERMISSION_INFO_COMPARATOR = new Comparator<PermissionInfo>() {
        @Override
        public int compare(PermissionInfo a, PermissionInfo b) {
            return a.getPermission().compareTo(b.getPermission());
        }
    };

    private static final Comparator<EntityMetadata> METADATA_COMPARATOR = new Comparator<EntityMetadata>() {
        @Override
        public int compare(EntityMetadata a, EntityMetadata b) {
            return a.getName().compareToIgnoreCase(b.getName());
        }
    };

    private static final Comparator<Membership> MEMBERSHIP_COMPARATOR = new Comparator<Membership>() {
        @Override
        public int compare(Membership a, Membership b) {
            return a.getMember().compareToIgnoreCase(b.getMember()); // NB By UUID
        }
    };

    public static List<PermissionEntity> sortPlayers(Collection<PermissionEntity> players) {
        List<PermissionEntity> result = new ArrayList<>(players);
        // Just sort alphabetically
        result.sort(PERMISSION_ENTITY_ALPHA_COMPARATOR);
        return result;
    }

    public static List<PermissionEntity> sortGroups(Collection<PermissionEntity> groups) {
        LinkedList<PermissionEntity> scanList = new LinkedList<>();

        // Seed with parent-less groups
        for (PermissionEntity group : groups) {
            if (group.getParents().isEmpty())
                scanList.add(group);
        }
        scanList.sort(PERMISSION_ENTITY_ALPHA_COMPARATOR);

        Set<PermissionEntity> result = new LinkedHashSet<>(groups.size());

        // BFS from queue to get total ordering
        while (!scanList.isEmpty()) {
            PermissionEntity group = scanList.remove();

            // Add to result
            result.add(group);

            // Grab children and add to end of scanList
            List<PermissionEntity> children = new ArrayList<>(group.getChildrenNew());

            // Sort children alphabetically
            children.sort(PERMISSION_ENTITY_ALPHA_COMPARATOR);

            scanList.addAll(children);
        }

        return new ArrayList<>(result);
    }

    public static List<Entry> sortPermissions(Collection<Entry> entries) {
        List<Entry> result = new ArrayList<>(entries);
        result.sort(ENTRY_COMPARATOR);
        return result;
    }

    public static List<EntityMetadata> sortMetadata(Collection<EntityMetadata> metadata) {
        List<EntityMetadata> result = new ArrayList<>(metadata);
        result.sort(METADATA_COMPARATOR);
        return result;
    }

    public static List<Membership> sortMemberships(Collection<Membership> memberships) {
        List<Membership> result = new ArrayList<>(memberships);
        result.sort(MEMBERSHIP_COMPARATOR);
        return result;
    }

    public static void displayPermissions(Plugin plugin, CommandSender sender, List<String> header, Map<String, Boolean> permissions, String filter) {
        displayPermissions(plugin, sender, null, header, permissions, filter);
    }

    public static void displayPermissions(Plugin plugin, CommandSender sender, List<String> lines, List<String> header, Map<String, Boolean> permissions, String filter) {
        List<PermissionInfo> permList = new ArrayList<>(permissions.size());
        for (Map.Entry<String, Boolean> me : permissions.entrySet()) {
            permList.add(new PermissionInfo(me.getKey(), me.getValue(), null));
        }
        displayPermissions(plugin, sender, lines, header, permList, filter, false);
    }

    public static void displayPermissions(Plugin plugin, CommandSender sender, List<String> header, List<PermissionInfo> permissions, String filter, boolean verbose) {
        displayPermissions(plugin, sender, null, header, permissions, filter, verbose);
    }

    public static void displayPermissions(Plugin plugin, CommandSender sender, List<String> lines, List<String> header, List<PermissionInfo> permissions, String filter, boolean verbose) {
        if (header == null)
            header = Collections.emptyList();

        // Sort for display
        permissions = new ArrayList<>(permissions); // make copy
        permissions.sort(PERMISSION_INFO_COMPARATOR);

        // Convert to lines and filter
        boolean display = false;
        if (lines == null) {
            lines = new ArrayList<>(header.size() + permissions.size());
            display = true;
        }
        lines.addAll(header);
        if (filter != null) {
            filter = filter.toLowerCase().trim();
            if (filter.isEmpty())
                filter = null;
        }
        boolean found = false;
        for (PermissionInfo pi : permissions) {
            String key = pi.getPermission();
            if (filter != null && !key.contains(filter)) continue;
            String source;
            if (verbose) {
                source = pi.getSource() != null ? (ChatColor.RED + " [" + pi.getSource() + "]") : "";
            } else {
                boolean notMine = pi.getSource() != null &&
                        !plugin.getName().equals(pi.getSource());
                source = notMine ? (ChatColor.RED + " *") : "";
            }
            lines.add(String.format(colorize("{DARK_GREEN}- {GOLD}%s{DARK_GREEN}: {GREEN}%s%s"), key, pi.getValue(), source));
            found = true;
        }

        if (!found) {
            lines.add(String.format(colorize("{RED}No %spermissions found."), filter == null ? "" : "matching "));
        }

        if (display) {
            ToHMessageUtils.displayLines(plugin, sender, lines);
        }
    }

    public static String displayGroups(String defaultGroup, List<Membership> memberships) {
        boolean gotGroup = false;

        Date now = new Date();
        StringBuilder sb = new StringBuilder();
        for (Iterator<Membership> i = memberships.iterator(); i.hasNext(); ) {
            Membership membership = i.next();
            if (membership.getExpiration() == null || membership.getExpiration().after(now)) {
                sb.append(ChatColor.DARK_GREEN);
                gotGroup = true;
            } else
                sb.append(ChatColor.GRAY);

            sb.append(membership.getGroup().getDisplayName());

            if (membership.getExpiration() != null) {
                sb.append('[');
                sb.append(Utils.dateToString(membership.getExpiration()));
                sb.append(']');
            }

            if (i.hasNext()) {
                sb.append(ChatColor.YELLOW);
                sb.append(", ");
            }
        }

        // Add default group if we got nothing
        if (!gotGroup) {
            if (sb.length() > 0) {
                sb.append(ChatColor.YELLOW);
                sb.append(", ");
            }
            sb.append(ChatColor.DARK_GREEN);
            sb.append(defaultGroup);
        }

        return sb.toString();
    }

    public static List<String> toMembers(Collection<Membership> memberships, boolean showUuid) {
        List<String> result = new ArrayList<>(memberships.size());
        for (Membership membership : memberships) {
            result.add(formatPlayerName(membership, showUuid));
        }
        return result;
    }

    public static List<String> toGroupNames(Collection<Membership> memberships) {
        List<String> result = new ArrayList<>(memberships.size());
        for (Membership membership : memberships) {
            result.add(membership.getGroup().getDisplayName());
        }
        return result;
    }

    public static List<Membership> filterExpired(Collection<Membership> memberships) {
        List<Membership> result = new ArrayList<>(memberships.size());
        Date now = new Date();
        for (Membership membership : memberships) {
            if (membership.getExpiration() == null || membership.getExpiration().after(now))
                result.add(membership);
        }
        return result;
    }

    public static Date parseDurationTimestamp(String duration, String[] args) {
        if (!ToHStringUtils.hasText(duration))
            return null;

        // Append args, if present
        if (args.length > 0)
            duration = duration + " " + ToHStringUtils.delimitedString(" ", args);

        duration = duration.trim();

        Matcher match = DURATION_PATTERN.matcher(duration);
        if (match.matches()) {
            int unitsInt = Calendar.DAY_OF_MONTH;

            int durationInt = Integer.parseInt(match.group(1));
            if (durationInt < 1)
                throw new ParseException("Invalid value: duration/timestamp"); // NB Should match option name

            String units = match.group(2);

            if (units != null) {
                units = units.toLowerCase();

                switch (units) {
                    case "minutes":
                    case "minute":
                    case "mins":
                    case "min":
                        unitsInt = Calendar.MINUTE;
                        break;
                    case "hours":
                    case "hour":
                    case "h":
                        unitsInt = Calendar.HOUR;
                        break;
                    case "days":
                    case "day":
                    case "d":
                        unitsInt = Calendar.DAY_OF_MONTH;
                        break;
                    case "months":
                    case "month":
                    case "m":
                        unitsInt = Calendar.MONTH;
                        break;
                    case "years":
                    case "year":
                    case "y":
                        unitsInt = Calendar.YEAR;
                        break;
                    default:
                        throw new ParseException("units must be minutes, hours, days, months, years");
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.add(unitsInt, durationInt);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        } else {
            // Try ISO 8601 date
            duration = duration.toUpperCase(); // Make sure that 'T' is capitalized
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                return sdf.parse(duration);
            } catch (Exception e) {
                throw new ParseException("Invalid value: duration/timestamp"); // NB Should match option name
            }
        }
    }

    // Suitable for user viewing (e.g. not dumps)
    public static String dateToString(Date date) {
        if (date == null)
            throw new IllegalArgumentException("date cannot be null");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return sdf.format(date);
    }

    /**
     * Display a diff between two sets of permissions.
     *
     * @param plugin           the plugin
     * @param sender           the CommandSender to output results to
     * @param permissions      the first set of permissions
     * @param otherPermissions the second set of permissions
     * @param header           a header to display, if any. May be null.
     * @param addedHeader      Header to display for added entries
     * @param removedHeader    Header to display for removed entries
     * @param changedHeader    Header to display for modified entries
     * @param sameMessage      Message to display if permission sets are identical
     * @param filter           TODO
     */
    public static void displayPermissionsDiff(Plugin plugin, CommandSender sender, Map<String, Boolean> permissions, Map<String, Boolean> otherPermissions, List<String> header,
                                              String addedHeader, String removedHeader, String changedHeader, String sameMessage, String filter) {
        if (header == null)
            header = Collections.emptyList();

        // Make copy of header since we modify it
        List<String> header0 = new ArrayList<>(header);

        // Now we diff
        Set<String> added = new HashSet<>(otherPermissions.keySet());
        added.removeAll(permissions.keySet());

        Set<String> removed = new HashSet<>(permissions.keySet());
        removed.removeAll(otherPermissions.keySet());

        Set<String> changed = new HashSet<>(permissions.keySet());
        changed.retainAll(otherPermissions.keySet());
        // Now we know what's common, actually determine what's different
        // Same thing, so remove from set
        changed.removeIf(key -> permissions.get(key).equals(otherPermissions.get(key)));

        List<String> lines = new ArrayList<>();

        if (!added.isEmpty()) {
            header0.add(addedHeader);
            displayPermissions(plugin, sender, lines, header0, getPermissionsSubset(otherPermissions, added), filter);
            header0.clear();
        }

        if (!removed.isEmpty()) {
            header0.add(removedHeader);
            displayPermissions(plugin, sender, lines, header0, getPermissionsSubset(permissions, removed), filter);
            header0.clear();
        }

        if (!changed.isEmpty()) {
            header0.add(changedHeader);
            displayPermissions(plugin, sender, lines, header0, getPermissionsSubset(otherPermissions, changed), filter);
        }

        if (added.isEmpty() && removed.isEmpty() && changed.isEmpty()) {
            lines.addAll(header0);
            lines.add(sameMessage);
        }

        ToHMessageUtils.displayLines(plugin, sender, lines);
    }

    // Given a permissions map and a set of keys, extract a subset of that map
    private static Map<String, Boolean> getPermissionsSubset(Map<String, Boolean> permissions, Set<String> keys) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        for (Map.Entry<String, Boolean> me : permissions.entrySet()) {
            if (keys.contains(me.getKey()))
                result.put(me.getKey(), me.getValue());
        }
        return result;
    }

    public static void calculateChildPermissions(Map<String, Boolean> permissions, Map<String, Boolean> children, boolean invert) {
        for (Map.Entry<String, Boolean> me : children.entrySet()) {
            String key = me.getKey().toLowerCase();
            Permission perm = Bukkit.getPluginManager().getPermission(key);
            boolean value = me.getValue() ^ invert;

            permissions.put(key, value);

            if (perm != null) {
                calculateChildPermissions(permissions, perm.getChildren(), !value);
            }
        }
    }

    public static void validatePlayer(PermissionService permissionService, String defaultGroup, UUID uuid, String playerName, List<String> header) {
        if (permissionService.getGroups(uuid).isEmpty() &&
                permissionService.getEntity(playerName, uuid, false) == null) {
            // Doesn't exist in the system
            header.add(String.format(colorize("{GRAY}(Player \"%s\" not in system. Assuming member of group \"%s\")"), playerName, defaultGroup));
        }
    }

    public static String formatPlayerName(PermissionEntity player, boolean showUuid) {
        assertFalse(player.isGroup());
        return UuidUtils.formatPlayerName(player.getUuid(), player.getDisplayName(), showUuid);
    }

    public static String formatPlayerName(Membership membership, boolean showUuid) {
        return UuidUtils.formatPlayerName(membership.getUuid(), membership.getDisplayName(), showUuid);
    }

    public static class PermissionInfo {

        private final String permission;

        private final boolean value;

        private final String source;

        public PermissionInfo(String permission, boolean value, String source) {
            if (!ToHStringUtils.hasText(permission))
                throw new IllegalArgumentException("permission must have a value");
            this.permission = permission.toLowerCase();
            this.value = value;
            this.source = source;
        }

        public String getPermission() {
            return permission;
        }

        public boolean getValue() {
            return value;
        }

        public String getSource() {
            return source;
        }

    }

}
