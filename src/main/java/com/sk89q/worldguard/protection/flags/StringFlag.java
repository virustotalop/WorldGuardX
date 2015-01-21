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

package com.sk89q.worldguard.protection.flags;

import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.util.command.CommandSource;

import javax.annotation.Nullable;

/**
 * Stores a string.
 */
public class StringFlag extends Flag<Text> {

    private final Text defaultValue;

    public StringFlag(String name) {
        super(name);
        this.defaultValue = null;
    }

    public StringFlag(String name, Text defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
    }

    public StringFlag(String name, RegionGroup defaultGroup) {
        super(name, defaultGroup);
        this.defaultValue = null;
    }

    public StringFlag(String name, RegionGroup defaultGroup, Text defaultValue) {
        super(name, defaultGroup);
        this.defaultValue = defaultValue;
    }

    @Nullable
    @Override
    public Text getDefault() {
        return defaultValue;
    }

    @Override
    public Text parseInput(WorldGuardPlugin plugin, CommandSource sender, String input) throws InvalidFlagFormat {
        return Texts.of(input.replaceAll("(?!\\\\)\\\\n", "\n").replaceAll("\\\\\\\\n", "\\n"));
    }

    @Override
    public Text unmarshal(Object o) {
        if (o instanceof Text) {
            return (Text) o;
        } else if (o instanceof String) {
            try {
                return Texts.json().from((String) o);
            } catch (TextMessageException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Object marshal(Text o) {
        return Texts.json().to(o);
    }

}
