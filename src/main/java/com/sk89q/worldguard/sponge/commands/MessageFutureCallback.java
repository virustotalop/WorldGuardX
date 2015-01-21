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

package com.sk89q.worldguard.sponge.commands;

import com.google.common.util.concurrent.FutureCallback;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandSource;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class MessageFutureCallback<V> implements FutureCallback<V> {

    private final WorldGuardPlugin plugin;
    private final CommandSource sender;
    @Nullable
    private final String success;
    @Nullable
    private final String failure;

    private MessageFutureCallback(WorldGuardPlugin plugin, CommandSource sender, @Nullable String success, @Nullable String failure) {
        this.plugin = plugin;
        this.sender = sender;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public void onSuccess(@Nullable V v) {
        if (success != null) {
            sender.sendMessage(Texts.of(TextColors.YELLOW, success));
        }
    }

    @Override
    public void onFailure(@Nullable Throwable throwable) {
        String failure = this.failure != null ? this.failure : "An error occurred";
        sender.sendMessage(Texts.of(TextColors.RED, failure + ": " + plugin.convertThrowable(throwable)));
    }

    public static class Builder {
        private final WorldGuardPlugin plugin;
        private final CommandSource sender;
        @Nullable
        private String success;
        @Nullable
        private String failure;

        public Builder(WorldGuardPlugin plugin, CommandSource sender) {
            checkNotNull(plugin);
            checkNotNull(sender);

            this.plugin = plugin;
            this.sender = sender;
        }

        public Builder onSuccess(@Nullable String message) {
            this.success = message;
            return this;
        }

        public Builder onFailure(@Nullable String message) {
            this.failure = message;
            return this;
        }

        public <V> MessageFutureCallback<V> build() {
            return new MessageFutureCallback<V>(plugin, sender, success, failure);
        }
    }

    public static <V> MessageFutureCallback<V> createRegionLoadCallback(WorldGuardPlugin plugin, CommandSource sender) {
        return new Builder(plugin, sender)
                .onSuccess("Successfully load the region data.")
                .build();
    }

    public static <V> MessageFutureCallback<V> createRegionSaveCallback(WorldGuardPlugin plugin, CommandSource sender) {
        return new Builder(plugin, sender)
                .onSuccess("Successfully saved the region data.")
                .build();
    }

}
