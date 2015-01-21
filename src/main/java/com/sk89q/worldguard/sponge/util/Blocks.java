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

package com.sk89q.worldguard.sponge.util;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

import java.util.Collections;
import java.util.List;

/**
 * Utility methods to deal with blocks.
 */
public final class Blocks {

    private Blocks() {
    }

    /**
     * Get a list of connected blocks to the given block, not including
     * the given block.
     *
     * @param block the block
     * @return a list of connected blocks, not including the given block
     */
    public static List<Location<Extent>> getConnected(Location<Extent> block) {
        if (block.getBlock().getType().equals(BlockTypes.BED)) {
            // TODO sponge block.get(Keys.);
            return null;
        } else {
            return Collections.emptyList();
        }
    }

}
