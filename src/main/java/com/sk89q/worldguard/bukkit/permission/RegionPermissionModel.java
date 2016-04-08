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

package com.sk89q.worldguard.bukkit.permission;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * Used for querying region-related permissions.
 */
public class RegionPermissionModel extends AbstractPermissionModel {
    
    public RegionPermissionModel(WorldGuardPlugin plugin, CommandSender sender) 
    {
        super(plugin, sender);
    }

    public boolean mayIgnoreRegionProtection(World world) 
    {
        return this.hasPluginPermission("region.bypass." + world.getName());
    }
    
    public boolean mayForceLoadRegions() 
    {
        return this.hasPluginPermission("region.load");
    }
    
    public boolean mayForceSaveRegions() 
    {
        return this.hasPluginPermission("region.save");
    }

    public boolean mayMigrateRegionStore() 
    {
        return this.hasPluginPermission("region.migratedb");
    }

    public boolean mayMigrateRegionNames() 
    {
        return this.hasPluginPermission("region.migrateuuid");
    }
    
    public boolean mayDefine() 
    {
        return this.hasPluginPermission("region.define");
    }
    
    public boolean mayRedefine(ProtectedRegion region) 
    {
        return this.hasPatternPermission("redefine", region);
    }
    
    public boolean mayClaim() 
    {
        return this.hasPluginPermission("region.claim");
    }
    
    public boolean mayClaimRegionsUnbounded() 
    {
        return this.hasPluginPermission("region.unlimited");
    }
    
    public boolean mayDelete(ProtectedRegion region) 
    {
        return this.hasPatternPermission("remove", region);
    }
    
    public boolean maySetPriority(ProtectedRegion region) 
    {
        return this.hasPatternPermission("setpriority", region);
    }
    
    public boolean maySetParent(ProtectedRegion child, ProtectedRegion parent) 
    {
        return this.hasPatternPermission("setparent", child) && (parent == null || this.hasPatternPermission("setparent", parent));
    }
    
    public boolean maySelect(ProtectedRegion region) 
    {
        return this.hasPatternPermission("select", region);
    }
    
    public boolean mayLookup(ProtectedRegion region) 
    {
        return this.hasPatternPermission("info", region);
    }
    
    public boolean mayTeleportTo(ProtectedRegion region) 
    {
        return this.hasPatternPermission("teleport", region);
    }
    
    public boolean mayList() 
    {
        return hasPluginPermission("region.list");
    }
    
    public boolean mayList(String targetPlayer) 
    {
        if (targetPlayer == null) 
        {
            return this.mayList();
        }
        
        if (targetPlayer.equalsIgnoreCase(getSender().getName())) 
        {
            return this.hasPluginPermission("region.list.own");
        }
        else 
        {
            return this.mayList();
        }
    }
    
    public boolean maySetFlag(ProtectedRegion region) 
    {
        return this.hasPatternPermission("flag.regions", region);
    }

    public boolean maySetFlag(ProtectedRegion region, Flag<?> flag) 
    {
        // This is a WTF permission
        return this.hasPatternPermission("flag.flags." + flag.getName().toLowerCase(), region);
    }

    public boolean maySetFlag(ProtectedRegion region, Flag<?> flag, @Nullable String value) 
    {
        String sanitizedValue;

        if (value != null) 
        {
            sanitizedValue = value.trim().toLowerCase().replaceAll("[^a-z0-9]", "");
            if (sanitizedValue.length() > 20) 
            {
                sanitizedValue = sanitizedValue.substring(0, 20);
            }
        } 
        else 
        {
            sanitizedValue = "unset";
        }

        // This is a WTF permission
        return this.hasPatternPermission("flag.flags." + flag.getName().toLowerCase() + "." + sanitizedValue, region);
    }

    public boolean mayAddMembers(ProtectedRegion region) 
    {
        return this.hasPatternPermission("addmember", region);
    }
    
    public boolean mayAddOwners(ProtectedRegion region) 
    {
        return this.hasPatternPermission("addowner", region);
    }

    public boolean mayRemoveMembers(ProtectedRegion region) 
    {
        return this.hasPatternPermission("removemember", region);
    }

    public boolean mayRemoveOwners(ProtectedRegion region) 
    {
        return this.hasPatternPermission("removeowner", region);
    }
    
    /**
     * Checks to see if the given sender has permission to modify the given region
     * using the region permission pattern.
     * 
     * @param perm the name of the node
     * @param region the region
     */
    private boolean hasPatternPermission(String perm, ProtectedRegion region) 
    {
        if (!(this.getSender() instanceof Player)) 
        {
            return true; // Non-players (i.e. console, command blocks, etc.) have full power
        }
        
        LocalPlayer localPlayer = getPlugin().wrapPlayer((Player) getSender());
        String idLower = region.getId().toLowerCase();
        String effectivePerm;
        
        if (region.isOwner(localPlayer)) 
        {
            return hasPluginPermission("region." + perm + ".own." + idLower) ||
                    hasPluginPermission("region." + perm + ".member." + idLower);
        } 
        else if (region.isMember(localPlayer)) 
        {
            return hasPluginPermission("region." + perm + ".member." + idLower);
        } 
        else 
        {
            effectivePerm = "region." + perm + "." + idLower;
        }

        return this.hasPluginPermission(effectivePerm);
    }

}
