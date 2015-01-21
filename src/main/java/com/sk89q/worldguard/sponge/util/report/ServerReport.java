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

package com.sk89q.worldguard.sponge.util.report;

import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.util.report.DataReport;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;

public class ServerReport extends DataReport {

    public ServerReport() {
        super("Server Information");

        Game game = WorldGuardPlugin.inst().getGame();
        Server server = game.getServer();

        append("Sponge Version", "");
        append("Implementation", "");
        append("Player Count", "%d/%d", server.getOnlinePlayers().size(), server.getMaxPlayers());

        append("Server Class Source", server.getClass().getProtectionDomain().getCodeSource().getLocation());

        // bukkit specific stuff
//        DataReport spawning = new DataReport("Spawning");
//        spawning.append("Ambient Spawn Limit", "");
//        spawning.append("Animal Spawn Limit", "");
//        spawning.append("Monster Spawn Limit", "");
//        spawning.append("Ticks per Animal Spawn", "");
//        spawning.append("Ticks per Monster Spawn", "");
//        append(spawning.getTitle(), spawning);

        DataReport config = new DataReport("Configuration");
        config.append("Nether Enabled?", "");
        config.append("The End Enabled?", "");
        config.append("Generate Structures?", "");
        config.append("Flight Allowed?", "");
//        config.append("Connection Throttle", "");
//        config.append("Idle Timeout", "");
        config.append("Shutdown Message", "");
        config.append("Default Game Mode", "");
        config.append("Main World Type", "");
        config.append("View Distance", "");
        append(config.getTitle(), config);

        DataReport protection = new DataReport("Protection");
        protection.append("Spawn Radius", "");
        append(protection.getTitle(), protection);
    }

}