package com.sk89q.worldguard.sponge.util;

import com.flowpowered.math.vector.Vector3i;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

public class WorldEditTransforms {
    public static Vector toVector(Location<? extends Extent> loc) {
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    public static Vector toVector(Vector3i vec) {
        return new Vector(vec.getX(), vec.getY(), vec.getZ());
    }

    public static BlockVector toBlockVector(Vector3i vec) {
        return new BlockVector(vec.getX(), vec.getY(), vec.getZ());
    }
}
