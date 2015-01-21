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

import com.flowpowered.math.vector.Vector3d;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.sponge.WorldConfiguration;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.sponge.util.Locations;
import org.spongepowered.api.data.manipulator.mutable.entity.VehicleData;
import org.spongepowered.api.data.manipulator.mutable.entity.VelocityData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.EntityMoveEvent;

public class WorldGuardVehicleListener extends AbstractListener {

    /**
     * Construct the object;
     *
     * @param plugin
     */
    public WorldGuardVehicleListener(WorldGuardPlugin plugin) {
        super(plugin);
    }

    @Listener
    public void onVehicleMove(EntityMoveEvent event) {
        // TODO vehicle -> passenger when sponge does it
        if (!event.getEntity().get(VehicleData.class).isPresent()) return;
        Entity vehicle = event.getEntity();
        VehicleData data = vehicle.get(VehicleData.class).get();
        if (!data.passenger().exists() || !(data.passenger().get() instanceof Player)) return;
        Player player = (Player) data.passenger().get();
        WorldConfiguration wcfg = getWorldConfig(vehicle.getWorld());

        if (wcfg.useRegions) {
            // Did we move a block?
            if (Locations.isDifferentBlock(event.getOldLocation(), event.getNewLocation())) {
                if (null != getPlugin().getSessionManager().get(player).testMoveTo(
                        player, Locations.toTransform(event.getNewLocation()), MoveType.RIDE)) {
                    vehicle.offer(vehicle.get(VelocityData.class).get().velocity().set(new Vector3d(0, 0, 0)));
                    vehicle.setLocation(event.getOldLocation());
                }
            }
        }
    }
}
