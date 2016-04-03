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

package com.sk89q.worldguard.bukkit.listener;

import com.sk89q.worldguard.bukkit.ConfigurationManager;
import com.sk89q.worldguard.bukkit.WorldConfiguration;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.util.Locations;
import com.sk89q.worldguard.session.MoveType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class WorldGuardVehicleListener extends AbstractListener {

    /**
     * Construct the object;
     *
     * @param plugin
     */
    public WorldGuardVehicleListener(WorldGuardPlugin plugin) {
        super(plugin);
    }

    /**
     * Register events.
     */
    @Override
    public void registerEvents() 
    {
       if(this.getPlugin().getGlobalStateManager().useWorldGuardVehicleListener)
       {
    	   super.registerEvents();
       }
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle.getPassenger() == null || !(vehicle.getPassenger() instanceof Player)) return;
        Player player = (Player) vehicle.getPassenger();
        World world = vehicle.getWorld();
        ConfigurationManager cfg = this.getPlugin().getGlobalStateManager();
        WorldConfiguration wcfg = cfg.get(world);

        if (wcfg.useRegions) {
            // Did we move a block?
            if (Locations.isDifferentBlock(event.getFrom(), event.getTo())) {
                if (null != this.getPlugin().getSessionManager().get(player).testMoveTo(player, event.getTo(), MoveType.RIDE)) {
                    vehicle.setVelocity(new Vector(0,0,0));
                    vehicle.teleport(event.getFrom());
                }
            }
        }
    }
}
