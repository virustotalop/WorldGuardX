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

package com.sk89q.worldguard.protection.flags;

import com.flowpowered.math.vector.Vector3d;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;

public class LocationFlag extends Flag<LazyLocation> {

    public LocationFlag(String name, RegionGroup defaultGroup) {
        super(name, defaultGroup);
    }

    public LocationFlag(String name) {
        super(name);
    }

    @Override
    public LazyLocation parseInput(WorldGuardPlugin plugin, CommandSource sender, String input) throws InvalidFlagFormat {
        input = input.trim();

        final Player player;
        try {
            player = plugin.checkPlayer(sender);
        } catch (CommandException e) {
            throw new InvalidFlagFormat(e.getMessage());
        }

        if ("here".equalsIgnoreCase(input)) {
            return toLazyLocation(player.getTransform());
        } else if ("none".equalsIgnoreCase(input)) {
            return null;
        } else {
            String[] split = input.split(",");
            if (split.length >= 3) {
                try {
                    final World world = player.getWorld();
                    final double x = Double.parseDouble(split[0]);
                    final double y = Double.parseDouble(split[1]);
                    final double z = Double.parseDouble(split[2]);
                    final double yaw = split.length < 4 ? 0 : Float.parseFloat(split[3]);
                    final double pitch = split.length < 5 ? 0 : Float.parseFloat(split[4]);
                    final double roll = 0.0D;

                    return new LazyLocation(world.getName(), new Vector3d(x, y, z), new Vector3d(pitch, yaw, roll));
                } catch (NumberFormatException ignored) {
                }
            }

            throw new InvalidFlagFormat("Expected 'here' or x,y,z.");
        }
    }

    private LazyLocation toLazyLocation(Transform transform) {
        return new LazyLocation(((World) transform.getExtent()).getName(), transform.getPosition(), transform.getRotation());
    }

    @Override
    public LazyLocation unmarshal(Object o) {
        if (o instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) o;

            Object rawWorld = map.get("world");
            if (rawWorld == null) return null;

            Object rawX = map.get("x");
            if (rawX == null) return null;

            Object rawY = map.get("y");
            if (rawY == null) return null;

            Object rawZ = map.get("z");
            if (rawZ == null) return null;

            Object rawYaw = map.get("yaw");
            if (rawYaw == null) return null;

            Object rawPitch = map.get("pitch");
            if (rawPitch == null) return null;

            Object rawRoll = map.get("roll");
            if (rawRoll == null) return null;

            Vector3d position = new Vector3d(toNumber(rawX), toNumber(rawY), toNumber(rawZ));
            double yaw = toNumber(rawYaw);
            double pitch = toNumber(rawPitch);
            Vector3d rotation = new Vector3d(pitch, yaw, 0.0D);

            return new LazyLocation(String.valueOf(rawWorld), position, rotation);
        }

        return null;
    }

    @Override
    public Object marshal(LazyLocation o) {
        Vector3d position = o.getPosition();
        Vector3d rotation = o.getRotation();
        Map<String, Object> vec = new HashMap<String, Object>();
        vec.put("world", o.getWorldName());
        vec.put("x", position.getX());
        vec.put("y", position.getY());
        vec.put("z", position.getZ());
        vec.put("pitch", rotation.getX());
        vec.put("yaw", rotation.getY());
        vec.put("roll", rotation.getZ());
        return vec;
    }

    private double toNumber(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else {
            return 0;
        }

    }
}
