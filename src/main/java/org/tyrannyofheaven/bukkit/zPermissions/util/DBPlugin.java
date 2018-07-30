package org.tyrannyofheaven.bukkit.zPermissions.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;

public class DBPlugin extends JavaPlugin implements IDBPlugin {
    public List<Class<?>> getDatabaseClasses() {
        return new ArrayList<>();
    }

    public EbeanServer getDatabase() {
        return null;
    }
}
