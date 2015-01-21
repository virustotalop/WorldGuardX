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

package com.sk89q.worldguard.sponge.commands.region;

import com.sk89q.squirrelid.cache.ProfileCache;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;

/**
 * Create a region printout, as used in /region info to show information about
 * a region.
 */
public class RegionPrintoutBuilder implements Callable<String> {
    
    private final ProtectedRegion region;
    @Nullable
    private final ProfileCache cache;
    private final TextBuilder builder = Texts.builder();

    /**
     * Create a new instance with a region to report on.
     *
     * @param region the region
     * @param cache a profile cache, or {@code null}
     */
    public RegionPrintoutBuilder(ProtectedRegion region, @Nullable ProfileCache cache) {
        this.region = region;
        this.cache = cache;
    }

    /**
     * Add a new line.
     */
    private void newLine() {
        builder.append(Texts.of("\n"));
    }
    
    /**
     * Add region name, type, and priority.
     */
    public void appendBasics() {
        builder.append(Texts.of(TextColors.BLUE, "Region: "));
        builder.append(Texts.of(TextColors.YELLOW, region.getId()));

        builder.append(Texts.of(TextColors.GRAY,
                " (type=" + region.getType().getName()
                + ", priority=" + region.getPriority()
                + ")"));

        newLine();
    }
    
    /**
     * Add information about flags.
     */
    public void appendFlags() {
        builder.append(Texts.of(TextColors.BLUE, "Flags: "));
        
        appendFlagsList(true);
        
        newLine();
    }
    
    /**
     * Append just the list of flags (without "Flags:"), including colors.
     *
     * @param useColors true to use colors
     */
    public void appendFlagsList(boolean useColors) {
        boolean hasFlags = false;
        
        for (Flag<?> flag : DefaultFlag.getFlags()) {
            Object val = region.getFlag(flag), group = null;
            
            // No value
            if (val == null) {
                continue;
            }

            if (hasFlags) {
                builder.append(Texts.of(", "));
            }

            RegionGroupFlag groupFlag = flag.getRegionGroupFlag();
            if (groupFlag != null) {
                group = region.getFlag(groupFlag);
            }

            if (group == null) {
                builder.append(Texts.of(flag.getName() + ": " + val));
            } else {
                builder.append(Texts.of(flag.getName() + " -g " + group + ": " + val));
            }

            hasFlags = true;
        }
            
        if (!hasFlags) {
            builder.append(Texts.of((useColors ? TextColors.RED : TextColors.NONE), "(none)"));
        }
    }
    
    /**
     * Add information about parents.
     */
    public void appendParents() {
        appendParentTree(true);
    }
    
    /**
     * Add information about parents.
     * 
     * @param useColors true to use colors
     */
    public void appendParentTree(boolean useColors) {
        if (region.getParent() == null) {
            return;
        }
        
        List<ProtectedRegion> inheritance = new ArrayList<ProtectedRegion>();

        ProtectedRegion r = region;
        inheritance.add(r);
        while (r.getParent() != null) {
            r = r.getParent();
            inheritance.add(r);
        }

        ListIterator<ProtectedRegion> it = inheritance.listIterator(
                inheritance.size());

        int indent = 0;
        while (it.hasPrevious()) {
            ProtectedRegion cur = it.previous();

            // Put symbol for child
            TextBuilder tree = Texts.builder();
            tree.color(useColors ? TextColors.GRAY : TextColors.NONE);
            if (indent != 0) {
                for (int i = 0; i < indent; i++) {
                    tree.append(Texts.of("  "));
                }
                tree.append(Texts.of("\u2517"));
            }
            builder.append(tree.build());
            
            // Put name
            builder.append(Texts.of(cur.getId()));
            
            // Put (parent)
            if (!cur.equals(region)) {
                builder.append(Texts.of((useColors ? TextColors.GRAY : TextColors.NONE), " (parent, priority=" + cur.getPriority() + ")"));
            }
            
            indent++;
            newLine();
        }
    }
    
    /**
     * Add information about members.
     */
    public void appendDomain() {
        builder.append(Texts.of(TextColors.BLUE, "Owners: "));
        addDomainString(region.getOwners());
        newLine();

        builder.append(Texts.of(TextColors.BLUE, "Members: "));
        addDomainString(region.getMembers());
        newLine();
    }

    private void addDomainString(DefaultDomain domain) {
        if (domain.size() != 0) {
            builder.append(Texts.of(TextColors.YELLOW, domain.toUserFriendlyString(cache)));
        } else {
            builder.append(Texts.of(TextColors.RED, "(none)"));
        }
    }
    
    /**
     * Add information about coordinates.
     */
    public void appendBounds() {
        BlockVector min = region.getMinimumPoint();
        BlockVector max = region.getMaximumPoint();
        builder.append(Texts.of(TextColors.BLUE, "Bounds:"));
        builder.append(Texts.of(TextColors.YELLOW,
                " (" + min.getBlockX() + "," + min.getBlockY() + "," + min.getBlockZ() + ")" +
                        " -> (" + max.getBlockX() + "," + max.getBlockY() + "," + max.getBlockZ() + ")"));
        newLine();
    }

    private void appendRegionInformation() {
        builder.append(Texts.of(TextColors.GRAY,
                "\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550"
                        + " Region Info "
                        + "\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550"));
        newLine();
        appendBasics();
        appendFlags();
        appendParents();
        appendDomain();
        appendBounds();

        if (cache != null) {
            builder.append(Texts.of(TextColors.GRAY, "Any names suffixed by * are 'last seen names' and may not be up to date."));
            newLine();
        }
    }

    @Override
    public String call() throws Exception {
        appendRegionInformation();
        return builder.toString();
    }

    /**
     * Send the report to a {@link CommandSource}.
     *
     * @param sender the recipient
     */
    public void send(CommandSource sender) {
        sender.sendMessage(toText());
    }

    public TextBuilder append(boolean b) {
        return builder.append(Texts.of(b));
    }

    public TextBuilder append(char c) {
        return builder.append(Texts.of(c));
    }

    public TextBuilder append(char[] str) {
        return builder.append(Texts.of(String.valueOf(str)));
    }

    public TextBuilder append(CharSequence s) {
        return builder.append(Texts.of(s));
    }

    public TextBuilder append(double d) {
        return builder.append(Texts.of(d));
    }

    public TextBuilder append(float f) {
        return builder.append(Texts.of(f));
    }

    public TextBuilder append(int i) {
        return builder.append(Texts.of(i));
    }

    public TextBuilder append(long lng) {
        return builder.append(Texts.of(lng));
    }

    public TextBuilder append(Object obj) {
        return builder.append(Texts.of(obj));
    }

    public TextBuilder append(String str) {
        return builder.append(Texts.of(str));
    }

    public TextBuilder append(StringBuffer sb) {
        return builder.append(Texts.of(sb.toString()));
    }

    public Text toText() {
        return builder.build();
    }

}
