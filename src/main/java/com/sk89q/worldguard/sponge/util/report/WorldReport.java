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
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.ArrayList;
import java.util.List;

public class WorldReport extends DataReport {

    public WorldReport() {
        super("Worlds");

        List<World> worlds = new ArrayList<World>(WorldGuardPlugin.inst().getGame().getServer().getWorlds());

        append("World Count", worlds.size());

        for (World world : worlds) {
            WorldProperties prop = world.getProperties();
            DataReport report = new DataReport("World: " + world.getName());
            report.append("UUID", world.getUniqueId());
            report.append("World Type", prop.getGeneratorType());
            report.append("Environment", prop.getDimensionType());
            WorldGenerator generator = world.getWorldGenerator();
            report.append("World Generator", generator != null ? generator.getClass().getName() : "<Default>");

            DataReport spawning = new DataReport("Spawning");
            spawning.append("Animals?", "");
            spawning.append("Monsters?", "");
            report.append(spawning.getTitle(), spawning);

            DataReport config = new DataReport("Configuration");
            config.append("Difficulty", world.getDifficulty());
            config.append("Max Height", "");
            config.append("Sea Level", "");
            report.append(config.getTitle(), config);

            DataReport state = new DataReport("State");
            state.append("Spawn Location", world.getSpawnLocation());
            state.append("Full Time", "");
            state.append("Weather Duration", world.getRemainingDuration());
            report.append(state.getTitle(), state);

            DataReport protection = new DataReport("Protection");
            protection.append("PVP?", "");
            protection.append("Game Rules", world.getGameRules());
            report.append(protection.getTitle(), protection);

            append(report.getTitle(), report);
        }
    }
}
