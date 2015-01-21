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

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.TameableData;
import org.spongepowered.api.data.manipulator.mutable.entity.VehicleData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.ExperienceOrb;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.hanging.Hanging;
import org.spongepowered.api.entity.living.Ambient;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.Villager;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.entity.vehicle.minecart.MinecartTNT;

import javax.annotation.Nullable;

public final class Entities {

    private Entities() {
    }

    /**
     * Test whether the given entity is tameable and tamed.
     *
     * @param entity the entity, or null
     * @return true if tamed
     */
    public static boolean isTamed(@Nullable Entity entity) {
        return entity != null && entity.get(Keys.TAMED_OWNER).get().isPresent();
    }

    /**
     * Return if the given entity type is TNT-based.
     *
     * @param entity the entity
     * @return true if TNT based
     */
    public static boolean isTNTBased(Entity entity) {
        return entity instanceof PrimedTNT || entity instanceof MinecartTNT;
    }

    /**
     * Return if the given entity type is a fireball
     * (not including wither skulls).
     *
     * @param type the type
     * @return true if a fireball
     */
    public static boolean isFireball(EntityType type) {
        return type.equals(EntityTypes.SMALL_FIREBALL) || type.equals(EntityTypes.FIREBALL);
    }

    /**
     * Test whether the given entity can be ridden if it is right clicked.
     *
     * @param entity the entity
     * @return true if the entity can be ridden
     */
    public static boolean isRiddenOnUse(Entity entity) {
        return entity.get(VehicleData.class).isPresent();
    }

    /**
     * Test whether the given entity type is a vehicle type.
     *
     * @param type the type
     * @return true if the type is a vehicle type
     */
    public static boolean isVehicle(EntityType type) {
        return type.equals(EntityTypes.BOAT)
                || isMinecart(type);
    }

    /**
     * Test whether the given entity type is a Minecart type.
     *
     * @param type the type
     * @return true if the type is a Minecart type
     */
    public static boolean isMinecart(EntityType type) {
        return type.equals(EntityTypes.RIDEABLE_MINECART)
                || type.equals(EntityTypes.CHESTED_MINECART)
                || type.equals(EntityTypes.COMMANDBLOCK_MINECART)
                || type.equals(EntityTypes.FURNACE_MINECART)
                || type.equals(EntityTypes.HOPPER_MINECART)
                || type.equals(EntityTypes.MOB_SPAWNER_MINECART)
                || type.equals(EntityTypes.TNT_MINECART);
    }

    /**
     * Get the underlying shooter of a projectile if one exists.
     *
     * @param entity the entity
     * @return the shooter
     */
    public static Entity getShooter(Entity entity) {

        while (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            ProjectileSource remover = projectile.getShooter();
            if (remover instanceof Entity && remover != entity) {
                entity = (Entity) remover;
            } else {
                return entity;
            }
        }

        return entity;
    }

    /**
     * Test whether an entity is hostile.
     *
     * @param entity the entity
     * @return true if hostile
     */
    public static boolean isHostile(Entity entity) {
        return entity instanceof Monster;
    }

    /**
     * Test whether an entity is ambient.
     *
     * @param entity the entity
     * @return true if ambient
     */
    public static boolean isAmbient(Entity entity) {
        return entity instanceof Ambient;
    }

    /**
     * Test whether an entity is an NPC.
     *
     * @param entity the entity
     * @return true if an NPC
     */
    public static boolean isNPC(Entity entity) {
        return entity instanceof Villager;
    }

    /**
     * Test whether an entity is a creature (a living thing) that is
     * not a player.
     *
     * @param entity the entity
     * @return true if a non-player creature
     */
    public static boolean isNonPlayerCreature(Entity entity) {
        return entity instanceof Living && !(entity instanceof Player);
    }

    /**
     * Test whether using the given entity should be considered "building"
     * rather than merely using an entity.
     *
     * @param entity the entity
     * @return true if considered building
     */
    public static boolean isConsideredBuildingIfUsed(Entity entity) {
        return entity instanceof Hanging;
    }

    public static boolean isIntensiveEntity(Entity entity) {
        return entity instanceof Item
                || entity instanceof PrimedTNT
                || entity instanceof ExperienceOrb
                || entity instanceof FallingBlock
                || entity instanceof Living
                    && !(entity.get(TameableData.class).isPresent())
                    && !(entity instanceof Player);
    }
}
