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

package com.sk89q.worldguard.sponge.event.debug;

import com.google.common.base.Optional;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import org.spongepowered.api.Game;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.AbstractEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.player.PlayerPlaceBlockEvent;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.List;

public class LoggingBlockPlaceEvent extends AbstractEvent implements PlayerPlaceBlockEvent, CancelLogging {

    private final CancelLogger logger = new CancelLogger();

    public LoggingBlockPlaceEvent(Location placedBlock, Direction placed, BlockSnapshot replacedBlockState, Player player) {
        this.replacement = replacedBlockState;
        this.direction = placed;
        this.player = player;
        this.block = placedBlock;
        this.state = placedBlock.getBlock();
        this.cause = Optional.of(new Cause(null, player, null));
        this.game = WorldGuardPlugin.inst().getGame();
    }

    private boolean cancelled;
    private BlockSnapshot replacement;
    private Location block;
    private BlockState state;
    private Optional<Cause> cause;
    private Game game;
    private Direction direction;
    private Player player;

    public List<CancelAttempt> getCancels() {
        return logger.getCancels();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.logger.log(isCancelled(), cancel, new Exception().getStackTrace());
        this.cancelled = cancel;
    }

    @Override
    public BlockSnapshot getReplacementBlock() {
        return replacement;
    }

    @Override
    public Optional<Cause> getCause() {
        return cause;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Location getLocation() {
        return block;
    }

    @Override
    public BlockState getBlock() {
        return state;
    }

    @Override
    public Direction getBlockFace() {
        return direction;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    @Override
    public Player getUser() {
        return player;
    }
}
