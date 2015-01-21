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

import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.sponge.event.BulkEvent;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility methods to deal with events.
 */
public final class Events {

    private Events() {
    }

    /**
     * Fire an event.
     *
     * @param event the event
     */
    public static void fire(Event event) {
        checkNotNull(event);
        WorldGuardPlugin.inst().getGame().getEventManager().post(event);
    }

    /**
     * Fire the {@code eventToFire} and return whether the event was cancelled.
     *
     * @param eventToFire the event to fire
     * @param <T> an event that can be fired and is cancellable
     * @return true if the event was cancelled
     */
    public static <T extends Event & Cancellable> boolean fireAndTestCancel(T eventToFire) {
        return WorldGuardPlugin.inst().getGame().getEventManager().post(eventToFire);
    }

    /**
     * Fire the {@code eventToFire} and cancel the original if the fired event
     * is cancelled.
     *
     * @param original the original event to potentially cancel
     * @param eventToFire the event to fire to consider cancelling the original event
     * @param <T> an event that can be fired and is cancellable
     * @return true if the event was fired and it caused the original event to be cancelled
     */
    public static <T extends Event & Cancellable> boolean fireToCancel(Cancellable original, T eventToFire) {
        boolean cancel = WorldGuardPlugin.inst().getGame().getEventManager().post(eventToFire);
        original.setCancelled(cancel);
        return cancel;
    }

    /**
     * Fire the {@code eventToFire} and cancel the original if the fired event
     * is <strong>explicitly</strong> cancelled.
     *
     * @param original the original event to potentially cancel
     * @param eventToFire the event to fire to consider cancelling the original event
     * @param <T> an event that can be fired and is cancellable
     * @return true if the event was fired and it caused the original event to be cancelled
     */
    public static <T extends Event & Cancellable & BulkEvent> boolean fireBulkEventToCancel(Cancellable original, T eventToFire) {
        WorldGuardPlugin.inst().getGame().getEventManager().post(eventToFire);
        if (!eventToFire.getExplicitResult()) {
            original.setCancelled(true);
            return true;
        }

        return false;
    }

    /**
     * Return whether the given damage cause is fire-related.
     *
     * @param cause the cause
     * @return true if fire related
     */
    public static boolean isFireCause(Cause cause) {
        return Causes.isFire(cause);
    }

    /**
     * Return whether the given cause is an explosion.
     *
     * @param cause the cause
     * @return true if it is an explosion cuase
     */
    public static boolean isExplosionCause(Cause cause) {
        return Causes.isExplosion(cause);
    }

    /**
     * Restore the statistic associated with the given cause.
     *
     * @param entity the entity
     * @param cause the cause
     */
    public static void restoreStatistic(Entity entity, Cause cause) {
        // TODO waiting on sponge
    }
}
