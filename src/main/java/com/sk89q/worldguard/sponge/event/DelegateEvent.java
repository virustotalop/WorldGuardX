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

package com.sk89q.worldguard.sponge.event;

import com.google.common.collect.Lists;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.sponge.cause.Cause;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.impl.AbstractEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This event is an internal event. We do not recommend handling or throwing
 * this event or its subclasses as the interface is highly subject to change.
 */
public abstract class DelegateEvent extends AbstractEvent implements Cancellable {

    @Nullable
    private final Event originalEvent;
    private final Cause cause;
    private final List<StateFlag> relevantFlags = Lists.newArrayList();
    private boolean result;
    private boolean silent;

    /**
     * Create a new instance
     *
     * @param originalEvent the original event
     * @param cause the cause
     */
    protected DelegateEvent(@Nullable Event originalEvent, Cause cause) {
        checkNotNull(cause);
        this.originalEvent = originalEvent;
        this.cause = cause;
    }

    /**
     * Get the original event.
     *
     * @return the original event, which may be {@code null} if unavailable
     */
    @Nullable
    public Event getOriginalEvent() {
        return originalEvent;
    }

    /**
     * Return the cause.
     *
     * @return the cause
     */
    public Cause getCause() {
        return cause;
    }

    /**
     * Get a list of relevant flags to consider for this event.
     *
     * @return A list of relevant flags
     */
    public List<StateFlag> getRelevantFlags() {
        return relevantFlags;
    }

    @Override
    public boolean isCancelled() {
        return result;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.result = cancel;
    }

    /**
     * Get whether this should be a silent check.
     *
     * @return true if a silent check
     */
    public boolean isSilent() {
        return silent;
    }

    /**
     * Set whether this should be a silent check.
     *
     * @param silent true if silent
     * @return the same event
     */
    public DelegateEvent setSilent(boolean silent) {
        this.silent = silent;
        return this;
    }

    /**
     * Set the event to if {@code allowed} is true.
     *
     * @param allowed true to set the result
     * @return the same event
     */
    public DelegateEvent setAllowed(boolean allowed) {
        this.result = allowed;

        return this;
    }

}
