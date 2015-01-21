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

package com.sk89q.worldguard.sponge.event.block;

import com.google.common.base.Predicate;
import com.sk89q.worldguard.sponge.cause.Cause;
import com.sk89q.worldguard.sponge.event.BulkEvent;
import com.sk89q.worldguard.sponge.event.DelegateEvent;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This event is an internal event. We do not recommend handling or throwing
 * this event or its subclasses as the interface is highly subject to change.
 */
abstract class AbstractBlockEvent extends DelegateEvent implements BulkEvent {

    private final Extent world;
    private final List<Location> blocks;
    private final BlockType effectiveMaterial;

    protected AbstractBlockEvent(@Nullable Event originalEvent, Cause cause, Extent world, List<Location> blocks, BlockType effectiveMaterial) {
        super(originalEvent, cause);
        checkNotNull(world);
        checkNotNull(blocks);
        checkNotNull(effectiveMaterial);
        this.world = world;
        this.blocks = blocks;
        this.effectiveMaterial = effectiveMaterial;
    }

    protected AbstractBlockEvent(@Nullable Event originalEvent, Cause cause, Location block) {
        this(originalEvent, cause, block.getExtent(), createList(checkNotNull(block)), block.getBlockType());
    }

    protected AbstractBlockEvent(@Nullable Event originalEvent, Cause cause, Location target, BlockType effectiveMaterial) {
        this(originalEvent, cause, target.getExtent(), createList(target), effectiveMaterial);
    }

    private static List<Location> createList(Location block) {
        List<Location> blocks = new ArrayList<Location>();
        blocks.add(block);
        return blocks;
    }

    /**
     * Get the world.
     *
     * @return the world
     */
    public Extent getWorld() {
        return world;
    }

    /**
     * Get the affected blocks.
     *
     * @return a list of affected block
     */
    public List<Location> getBlocks() {
        return blocks;
    }

    /**
     * Filter the list of affected blocks with the given predicate. If the
     * predicate returns {@code false}, then the block is removed.
     *
     * @param predicate the predicate
     * @param cancelEventOnFalse true to cancel the event and clear the block
     *                           list once the predicate returns {@code false}
     * @return true if one or more blocks were filtered out
     */
    public boolean filter(Predicate<Location> predicate, boolean cancelEventOnFalse) {
        boolean hasRemoval = false;

        Iterator<Location> it = blocks.iterator();
        while (it.hasNext()) {
            if (!predicate.apply(it.next())) {
                hasRemoval = true;

                if (cancelEventOnFalse) {
                    getBlocks().clear();
                    setCancelled(true);
                    break;
                } else {
                    it.remove();
                }
            }
        }

        return hasRemoval;
    }

    /**
     * Filter the list of affected blocks with the given predicate. If the
     * predicate returns {@code false}, then the block is removed.
     *
     * <p>This method will <strong>not</strong> fail fast and
     * cancel the event the first instance that the predicate returns
     * {@code false}. See {@link #filter(Predicate, boolean)} to adjust
     * this behavior.</p>
     *
     * @param predicate the predicate
     * @return true if one or more blocks were filtered out
     */
    public boolean filter(Predicate<Location> predicate) {
        return filter(predicate, false);
    }

    /**
     * Get the effective material of the block, regardless of what the block
     * currently is.
     *
     * @return the effective material
     */
    public BlockType getEffectiveMaterial() {
        return effectiveMaterial;
    }

    @Override
    public boolean isCancelled() {
        return blocks.isEmpty() || super.isCancelled();
    }

    @Override
    public boolean getExplicitResult() {
        return super.isCancelled();
    }

}
