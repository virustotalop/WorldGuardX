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

import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.util.paste.EngineHubPaste;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command-related utility methods.
 */
public final class CommandUtils {

    private static final Logger log = Logger.getLogger(CommandUtils.class.getCanonicalName());

    private CommandUtils() {
    }

    /**
     * Replace color macros in a string.
     *
     * @param str the string
     * @return the new string
     */
    public static Text replaceColorMacros(String str) {
        Text sk = SKTextRepresentation.INSTANCE.fromUnchecked(str);
        // uuuuhmmmm
        if (str.equalsIgnoreCase(Texts.toPlain(sk))) {
            sk = Texts.legacy().fromUnchecked(str);
        }
        return sk;
    }


    /**
     * Get the name of the given owner object.
     *
     * @param owner the owner object
     * @return a name
     */
    public static String getOwnerName(@Nullable Object owner) {
        if (owner == null) {
            return "?";
        } else if (owner instanceof Player) {
            return ((Player) owner).getName();
        } else if (owner instanceof ConsoleSource) {
            return "*CONSOLE*";
        } else if (owner instanceof CommandBlockSource) {
            return ((CommandBlockSource) owner).getName() + ((CommandBlockSource) owner).getLocation();
        } else {
            return "?";
        }
    }

    /**
     * Return a function that accepts a string to send a message to the
     * given sender.
     *
     * @param sender the sender
     * @return a function
     */
    public static Function<String, ?> messageFunction(final CommandSource sender) {
        return new Function<String, Object>() {
            @Override
            public Object apply(@Nullable String s) {
                sender.sendMessage(Texts.of(s));
                return null;
            }
        };
    }

    /**
     * Submit data to a pastebin service and inform the sender of
     * success or failure.
     *
     * @param plugin The plugin
     * @param sender The sender
     * @param content The content
     * @param successMessage The message, formatted with {@link String#format(String, Object...)} on success
     */
    public static void pastebin(WorldGuardPlugin plugin, final CommandSource sender, String content, final String successMessage) {
        ListenableFuture<URL> future = new EngineHubPaste().paste(content);

        AsyncCommandHelper.wrap(future, plugin, sender)
                .registerWithSupervisor("Submitting content to a pastebin service...")
                .sendMessageAfterDelay("(Please wait... sending output to pastebin...)");

        Futures.addCallback(future, new FutureCallback<URL>() {
            @Override
            public void onSuccess(URL url) {
                sender.sendMessage(Texts.of(TextColors.YELLOW, String.format(successMessage, url)));
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.log(Level.WARNING, "Failed to submit pastebin", throwable);
                sender.sendMessage(Texts.of(TextColors.RED, "Failed to submit to a pastebin. Please see console for the error."));
            }
        });
    }

}
