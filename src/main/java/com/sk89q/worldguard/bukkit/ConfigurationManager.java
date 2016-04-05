/*
 * WorldGuard, a suite of tools for Minecraft
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldGuard team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldguard.bukkit;

import com.google.common.collect.ImmutableMap;
import com.sk89q.util.yaml.YAMLFormat;
import com.sk89q.util.yaml.YAMLProcessor;
import com.sk89q.worldguard.protection.managers.storage.DriverType;
import com.sk89q.worldguard.protection.managers.storage.RegionDriver;
import com.sk89q.worldguard.protection.managers.storage.file.DirectoryYamlDriver;
import com.sk89q.worldguard.protection.managers.storage.sql.SQLDriver;
import com.sk89q.worldguard.session.handler.WaterBreathing;
import com.sk89q.worldguard.util.report.Unreported;
import com.sk89q.worldguard.util.sql.DataSourceConfig;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Represents the global configuration and also delegates configuration
 * for individual worlds.
 *
 * @author sk89q
 * @author Michael
 */
public class ConfigurationManager {

    private static final Logger log = Logger.getLogger(ConfigurationManager.class.getCanonicalName());

    private static final String CONFIG_HEADER = "#\r\n" +
            "# WorldGuard's main configuration file\r\n" +
            "#\r\n" +
            "# This is the global configuration file. Anything placed into here will\r\n" +
            "# be applied to all worlds. However, each world has its own configuration\r\n" +
            "# file to allow you to replace most settings in here for that world only.\r\n" +
            "#\r\n" +
            "# About editing this file:\r\n" +
            "# - DO NOT USE TABS. You MUST use spaces or Bukkit will complain. If\r\n" +
            "#   you use an editor like Notepad++ (recommended for Windows users), you\r\n" +
            "#   must configure it to \"replace tabs with spaces.\" In Notepad++, this can\r\n" +
            "#   be changed in Settings > Preferences > Language Menu.\r\n" +
            "# - Don't get rid of the indents. They are indented so some entries are\r\n" +
            "#   in categories (like \"enforce-single-session\" is in the \"protection\"\r\n" +
            "#   category.\r\n" +
            "# - If you want to check the format of this file before putting it\r\n" +
            "#   into WorldGuard, paste it into http://yaml-online-parser.appspot.com/\r\n" +
            "#   and see if it gives \"ERROR:\".\r\n" +
            "# - Lines starting with # are comments and so they are ignored.\r\n" +
            "#\r\n";

    @Unreported private WorldGuardPlugin plugin;
    @Unreported private ConcurrentMap<String, WorldConfiguration> worlds;
    @Unreported private YAMLProcessor config;

    public boolean useRegionsCreatureSpawnEvent;
    public boolean activityHaltToggle = false;
    public boolean useGodPermission;
    public boolean useGodGroup;
    public boolean useAmphibiousGroup;
    
    public boolean deopOnJoin;
    public boolean blockInGameOp;
    public boolean migrateRegionsToUuid;
    public boolean keepUnresolvedNames;
    
    public boolean useWorldGuardPlayerListener;
    public boolean useWorldGuardBlockListener;
    public boolean useWorldGuardEntityListener;
    public boolean useWorldGuardWeatherListener;
    public boolean useWorldGuardVehicleListener;
    public boolean useWorldGuardServerListener;
    public boolean useWorldGuardHangingListener;
    
    public boolean usePlayerMoveListener;
    public boolean useBlacklistListener;
    public boolean useChestProtectionListener;
    public boolean useRegionProtectionListener;
    public boolean useRegionFlagsListener;
    public boolean useWorldRulesListener;
    public boolean useBlockedPotionsListener;
    public boolean useEventAbstractionListener;
    public boolean useInventoryMoveItemListener;
    
    public boolean usePlayerModesListener;
    public boolean useBuildPermissionListener;
    public boolean useInvincibilityListener;
    
    public boolean useWorldGuardWorldListener;
    
    public boolean usePlayerTeleports;
    
    @Unreported public Map<String, String> hostKeys = new HashMap<String, String>();

    /**
     * Region Storage Configuration method, and config values
     */
    @Unreported public RegionDriver selectedRegionStoreDriver;
    @Unreported public Map<DriverType, RegionDriver> regionStoreDriverMap;

    /**
     * Construct the object.
     *
     * @param plugin The plugin instance
     */
    public ConfigurationManager(WorldGuardPlugin plugin) {
        this.plugin = plugin;
        this.worlds = new ConcurrentHashMap<String, WorldConfiguration>();
    }

    /**
     * Get the folder for storing data files and configuration.
     *
     * @return the data folder
     */
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    /**
     * Get the folder for storing data files and configuration for each
     * world.
     *
     * @return the data folder
     */
    public File getWorldsDataFolder() {
        return new File(getDataFolder(), "worlds");
    }

    /**
     * Load the configuration.
     */
    @SuppressWarnings("unchecked")
    public void load() {
        // Create the default configuration file
        plugin.createDefaultConfiguration(
                new File(plugin.getDataFolder(), "config.yml"), "config.yml");

        config = new YAMLProcessor(new File(plugin.getDataFolder(), "config.yml"), true, YAMLFormat.EXTENDED);
        try {
            config.load();
        } catch (IOException e) {
            log.severe("Error reading configuration for global config: ");
            e.printStackTrace();
        }

        this.config.removeProperty("suppress-tick-sync-warnings");
        this.migrateRegionsToUuid = this.config.getBoolean("regions.uuid-migration.perform-on-next-start", true);
        this.keepUnresolvedNames = this.config.getBoolean("regions.uuid-migration.keep-names-that-lack-uuids", true);
        this.useRegionsCreatureSpawnEvent = this.config.getBoolean("regions.use-creature-spawn-event", true);
        this.useGodPermission = this.config.getBoolean("auto-invincible", config.getBoolean("auto-invincible-permission", false));
        this.useGodGroup = this.config.getBoolean("auto-invincible-group", false);
        this.useAmphibiousGroup = this.config.getBoolean("auto-no-drowning-group", false);
        this.config.removeProperty("auto-invincible-permission");
        
        this.useWorldGuardPlayerListener = this.config.getBoolean("use-worldguard-player-listener", true);
        this.useWorldGuardBlockListener = this.config.getBoolean("use-worldguard-block-listener", true);
        this.useWorldGuardEntityListener = this.config.getBoolean("use-worldguard-entity-listener", true);
        this.useWorldGuardWeatherListener = this.config.getBoolean("use-worldguard-weather-listener", true);
        this.useWorldGuardVehicleListener = this.config.getBoolean("use-worldguard-vehicle-listener", true);
        this.useWorldGuardServerListener = this.config.getBoolean("use-worldguard-server-listener", false);
        this.useWorldGuardHangingListener = this.config.getBoolean("use-worldguard-hanging-listener", true);
        this.useWorldGuardWorldListener = this.config.getBoolean("use-worldguard-world-listener", true);
        
        this.usePlayerMoveListener = this.config.getBoolean("use-player-move-listener", true);
        this.useBlacklistListener = this.config.getBoolean("use-blacklist-listener", true);
        this.useChestProtectionListener = this.config.getBoolean("use-chest-protection-listener", true);
        this.useRegionProtectionListener = this.config.getBoolean("use-region-protection-listener", true);
        this.useRegionFlagsListener = this.config.getBoolean("use-region-flags-listener", true);
        this.useWorldRulesListener = this.config.getBoolean("use-world-rules-listener", true);
        this.useBlockedPotionsListener = this.config.getBoolean("use-blocked-potions-listener", true);
        this.useEventAbstractionListener = this.config.getBoolean("use-event-abstraction-listener", true);
        this.useInventoryMoveItemListener = this.config.getBoolean("use-inventory-move-listener", false);
        
        this.usePlayerModesListener = this.config.getBoolean("use-player-modes-listener", true);
        this.useBuildPermissionListener = this.config.getBoolean("use-build-permission-listener", true);
        this.useInvincibilityListener = this.config.getBoolean("use-invincibility-listener", true);
        
        
        
        this.usePlayerTeleports = this.config.getBoolean("use-player-teleports", true);
       
        
        this.deopOnJoin = this.config.getBoolean("security.deop-everyone-on-join", false);
        this.blockInGameOp = this.config.getBoolean("security.block-in-game-op-command", false);

        this.hostKeys = new HashMap<String, String>();
        Object hostKeysRaw = config.getProperty("host-keys");
        if (hostKeysRaw == null || !(hostKeysRaw instanceof Map)) {
        	this.config.setProperty("host-keys", new HashMap<String, String>());
        } else {
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) hostKeysRaw).entrySet()) {
                String key = String.valueOf(entry.getKey());
                String value = String.valueOf(entry.getValue());
                this.hostKeys.put(key.toLowerCase(), value);
            }
        }

        // ====================================================================
        // Region store drivers
        // ====================================================================

        boolean useSqlDatabase = this.config.getBoolean("regions.sql.use", false);
        String sqlDsn = this.config.getString("regions.sql.dsn", "jdbc:mysql://localhost/worldguard");
        String sqlUsername = this.config.getString("regions.sql.username", "worldguard");
        String sqlPassword = this.config.getString("regions.sql.password", "worldguard");
        String sqlTablePrefix = this.config.getString("regions.sql.table-prefix", "");

        DataSourceConfig dataSourceConfig = new DataSourceConfig(sqlDsn, sqlUsername, sqlPassword, sqlTablePrefix);
        SQLDriver sqlDriver = new SQLDriver(dataSourceConfig);
        DirectoryYamlDriver yamlDriver = new DirectoryYamlDriver(getWorldsDataFolder(), "regions.yml");

        this.regionStoreDriverMap = ImmutableMap.<DriverType, RegionDriver>builder()
                .put(DriverType.MYSQL, sqlDriver)
                .put(DriverType.YAML, yamlDriver)
                .build();
        this.selectedRegionStoreDriver = useSqlDatabase ? sqlDriver : yamlDriver;

        // Load configurations for each world
        for (World world : plugin.getServer().getWorlds()) {
            get(world);
        }

        config.setHeader(CONFIG_HEADER);
        config.save();
    }

    /**
     * Unload the configuration.
     */
    public void unload() {
    	this.worlds.clear();
    }

    public void disableUuidMigration() {
    	this.config.setProperty("regions.uuid-migration.perform-on-next-start", false);
        if (!config.save()) {
            log.severe("Error saving configuration!");
        }
    }

    /**
     * Get the configuration for a world.
     *
     * @param world The world to get the configuration for
     * @return {@code world}'s configuration
     */
    public WorldConfiguration get(World world) {
        String worldName = world.getName();
        WorldConfiguration config = this.worlds.get(worldName);
        WorldConfiguration newConfig = null;

        while (config == null) {
            if (newConfig == null) {
                newConfig = new WorldConfiguration(this.plugin, worldName, this.config);
            }
            this.worlds.putIfAbsent(world.getName(), newConfig);
            config = this.worlds.get(world.getName());
        }

        return config;
    }

    /**
     * Check to see if god mode is enabled for a player.
     *
     * @param player The player to check
     * @return Whether the player has godmode through WorldGuard or CommandBook
     */
    public boolean hasGodMode(Player player) {
        return this.plugin.getSessionManager().get(player).isInvincible(player);
    }

    /**
     * Enable amphibious mode for a player.
     *
     * @param player The player to enable amphibious mode for
     */
    public void enableAmphibiousMode(Player player) {
        WaterBreathing handler = this.plugin.getSessionManager().get(player).getHandler(WaterBreathing.class);
        if (handler != null) {
            handler.setWaterBreathing(true);
        }
    }

    /**
     * Disable amphibious mode  for a player.
     *
     * @param player The player to disable amphibious mode for
     */
    public void disableAmphibiousMode(Player player) {
        WaterBreathing handler = this.plugin.getSessionManager().get(player).getHandler(WaterBreathing.class);
        if (handler != null) {
            handler.setWaterBreathing(false);
        }
    }

    /**
     * Check to see if amphibious mode is enabled for a player.
     *
     * @param player The player to check
     * @return Whether {@code player} has amphibious mode
     */
    public boolean hasAmphibiousMode(Player player) {
        WaterBreathing handler = this.plugin.getSessionManager().get(player).getHandler(WaterBreathing.class);
        return handler != null && handler.hasWaterBreathing();
    }
}