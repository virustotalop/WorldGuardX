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

import com.google.common.collect.Lists;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventHandler;
import org.spongepowered.api.plugin.PluginContainer;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Traces the owner of a handler.
 */
public class HandlerTracer {

    private final List<Handler> handlers;

    public HandlerTracer(Event event) {
        this.handlers = getHandlers(event);
    }

    /**
     * Attempt to detect the cause of an event that was fired.
     *
     * @param elements The stack trace
     * @return The plugin, if found
     */
    @Nullable
    public PluginContainer detectPlugin(StackTraceElement[] elements) {
        for (int i = elements.length - 1; i >= 0; i--) {
            StackTraceElement element = elements[i];

            for (Handler handler : handlers) {
                if (element.getClassName().equals(handler.className)) {
                    return handler.plugin;
                }
            }
        }

        return null;
    }

    /**
     * Build a cache of listeners registered for an event.
     *
     * @param event The event
     * @return A list of handlers
     */
    private static List<Handler> getHandlers(Event event) {
        List<Handler> handlers = Lists.newArrayList();

        for (EventHandler listener : new EventHandler[]{}) { // TODO sponge pls expose
            //handlers.add(new Handler(listener.getListener().getClass().getName(), listener.getPlugin()));
        }

        return handlers;
    }

    private static class Handler {
        private final String className;
        private final PluginContainer plugin;

        private Handler(String className, PluginContainer plugin) {
            this.className = className;
            this.plugin = plugin;
        }
    }
}
