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

import com.google.common.base.Predicate;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.sponge.RegionQuery;
import com.sk89q.worldguard.sponge.WorldConfiguration;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.sponge.event.block.BreakBlockEvent;
import com.sk89q.worldguard.sponge.event.block.PlaceBlockEvent;
import com.sk89q.worldguard.sponge.util.Materials;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.complex.EnderDragon;
import org.spongepowered.api.entity.living.monster.Creeper;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class RegionFlagsListener extends AbstractListener {

    /**
     * Construct the listener.
     *
     * @param plugin an instance of WorldGuardPlugin
     */
    public RegionFlagsListener(WorldGuardPlugin plugin) {
        super(plugin);
    }

    @Listener
    public void onPlaceBlock(final PlaceBlockEvent event) {
        if (!isRegionSupportEnabled(event.getWorld())) return; // Region support disabled

        RegionQuery query = getPlugin().getRegionContainer().createQuery();

        Location block;
        if ((block = event.getCause().getFirstBlock()) != null) {
            // ================================================================
            // PISTONS flag
            // ================================================================

            if (Materials.isPistonBlock(block.getBlockType())) {
                event.filter(testState(query, DefaultFlag.PISTONS), false);
            }
        }
    }

    @Listener
    public void onBreakBlock(final BreakBlockEvent event) {
        if (!isRegionSupportEnabled(event.getWorld())) return; // Region support disabled

        WorldConfiguration config = getWorldConfig(event.getWorld());
        RegionQuery query = getPlugin().getRegionContainer().createQuery();

        Location block;
        if ((block = event.getCause().getFirstBlock()) != null) {
            // ================================================================
            // PISTONS flag
            // ================================================================

            if (Materials.isPistonBlock(block.getBlockType())) {
                event.filter(testState(query, DefaultFlag.PISTONS), false);
            }
        }

        Entity entity;
        if ((entity = event.getCause().getFirstEntity()) != null) {
            // ================================================================
            // CREEPER_EXPLOSION flag
            // ================================================================

            if (entity instanceof Creeper) { // Creeper
                event.filter(testState(query, DefaultFlag.CREEPER_EXPLOSION), config.explosionFlagCancellation);
            }

            // ================================================================
            // ENDERDRAGON_BLOCK_DAMAGE flag
            // ================================================================

            if (entity instanceof EnderDragon) { // Enderdragon
                event.filter(testState(query, DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE), config.explosionFlagCancellation);
            }

        }
    }

    /**
     * Create a new predicate to test a state flag for each location.
     *
     * @param query the query
     * @param flag the flag
     * @return a predicate
     */
    private Predicate<Location<World>> testState(final RegionQuery query, final StateFlag flag) {
        return location -> query.testState(location, (RegionAssociable) null, flag);
    }


}
