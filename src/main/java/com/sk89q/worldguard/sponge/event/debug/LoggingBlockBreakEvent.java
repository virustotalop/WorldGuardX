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
import org.spongepowered.api.event.entity.player.PlayerBreakBlockEvent;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.List;

public class LoggingBlockBreakEvent extends AbstractEvent implements PlayerBreakBlockEvent, CancelLogging {

    private final CancelLogger logger = new CancelLogger();

    public LoggingBlockBreakEvent(Location block, BlockSnapshot replacedBlockState, Player player) {
        this.block = block;
        this.cause = Optional.of(new Cause(null, player, null));
        this.player = player;
        this.state = block.getBlock();
        this.replacement = replacedBlockState;
        this.game = WorldGuardPlugin.inst().getGame();
    }

    private Optional<Cause> cause;
    private Player player;
    private BlockSnapshot replacement;
    private boolean cancelled;
    private Location block;
    private BlockState state;
    private Game game;

    public List<CancelAttempt> getCancels() {
        return logger.getCancels();
    }

    @Override
    public boolean isCancelled() {
        return false;
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
    public Location getLocation() {
        return block;
    }

    @Override
    public BlockState getBlock() {
        return state;
    }

    @Override
    public Optional<Cause> getCause() {
        return cause;
    }

    @Override
    public int getExp() {
        return 0;
    }

    @Override
    public void setExp(int exp) {
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Direction getBlockFace() {
        return null;
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
