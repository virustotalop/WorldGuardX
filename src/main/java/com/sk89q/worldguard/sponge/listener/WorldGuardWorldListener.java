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

package com.sk89q.worldguard.sponge.listener;

import com.sk89q.worldguard.sponge.ConfigurationManager;
import com.sk89q.worldguard.sponge.WorldConfiguration;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.sponge.util.Entities;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.weather.Weathers;

import java.util.logging.Logger;

public class WorldGuardWorldListener {

    private static final Logger log = Logger.getLogger(WorldGuardWorldListener.class.getCanonicalName());
    private WorldGuardPlugin plugin;

    /**
     * Construct the object;
     *
     * @param plugin The plugin instance
     */
    public WorldGuardWorldListener(WorldGuardPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register events.
     */
    public void registerEvents() {
        plugin.getGame().getEventManager().registerListeners(plugin, this);
    }

    @Listener
    public void onChunkLoad(LoadChunkEvent event) {
        ConfigurationManager cfg = plugin.getGlobalStateManager();

        if (cfg.activityHaltToggle) {
            int removed = 0;

            for (Entity entity : event.getTargetChunk().getEntities()) {
                if (Entities.isIntensiveEntity(entity)) {
                    entity.remove();
                    removed++;
                }
            }

            if (removed > 50) {
                log.info("Halt-Act: " + removed + " entities (>50) auto-removed from " + event.getTargetChunk());
            }
        }
    }

    @Listener
    public void onWorldLoad(LoadWorldEvent event) {
        initWorld(event.getTargetWorld());
    }

    /**
     * Initialize the settings for the specified world
     * @see WorldConfiguration#alwaysRaining
     * @see WorldConfiguration#disableWeather
     * @see WorldConfiguration#alwaysThundering
     * @see WorldConfiguration#disableThunder
     * @param world The specified world
     */
    public void initWorld(World world) {
        ConfigurationManager cfg = plugin.getGlobalStateManager();
        WorldConfiguration wcfg = cfg.get(world);
        if (wcfg.alwaysRaining && !wcfg.disableWeather) {
            world.forecast(Weathers.RAIN);
        } else if (wcfg.disableWeather && !wcfg.alwaysRaining) {
            world.forecast(Weathers.CLEAR);
        }
        if (wcfg.alwaysThundering && !wcfg.disableThunder) {
            world.forecast(Weathers.THUNDER_STORM);
        } else if (wcfg.disableThunder && !wcfg.alwaysThundering) {
            world.forecast(Weathers.CLEAR);
        }
    }
}
