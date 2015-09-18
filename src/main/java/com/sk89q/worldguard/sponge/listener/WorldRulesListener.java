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

import com.sk89q.worldguard.sponge.WorldConfiguration;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.sponge.event.entity.SpawnEntityEvent;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;

public class WorldRulesListener extends AbstractListener {

    /**
     * Construct the listener.
     *
     * @param plugin an instance of WorldGuardPlugin
     */
    public WorldRulesListener(WorldGuardPlugin plugin) {
        super(plugin);
    }

    @Listener
    public void onSpawnEntity(final SpawnEntityEvent event) {
        WorldConfiguration config = getWorldConfig(event.getWorld());

        // ================================================================
        // EXP_DROPS flag
        // ================================================================

        if (event.getEffectiveType().equals(EntityTypes.EXPERIENCE_ORB)) {
            if (config.disableExpDrops) {
                event.setCancelled(true);
            }
        }
    }

}
