package com.originsrebirth.spigot.boilerplate;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author admin[at]flyn.codes
 * @OriginalPackageName com.originsrebirth.spigot.boilerplate
 * @OriginalProjectName OriginsBoilerplate
 * @ClassFileCreated 1 May 2026 08:09:31
 **/

@Getter
@Accessors(fluent = true)
public class Boilerplate extends JavaPlugin {

    private static Boilerplate plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Boilerplate plugin() {
        return plugin;
    }

}
