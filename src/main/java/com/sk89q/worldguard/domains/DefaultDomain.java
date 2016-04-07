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

package com.sk89q.worldguard.domains;

import com.google.common.collect.ImmutableMap;
import com.sk89q.squirrelid.Profile;
import com.sk89q.squirrelid.cache.ProfileCache;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.util.ChangeTracked;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A combination of a {@link PlayerDomain} and a {@link GroupDomain}.
 */
public class DefaultDomain implements Domain, ChangeTracked {

    private PlayerDomain playerDomain = new PlayerDomain();
    private GroupDomain groupDomain = new GroupDomain();

    /**
     * Create a new domain.
     */
    public DefaultDomain() {}

    /**
     * Create a new domain from an existing one, making a copy of all values.
     *
     * @param existing the other domain to copy values from
     */
    public DefaultDomain(DefaultDomain existing) 
    {
    	this.setPlayerDomain(existing.getPlayerDomain());
    	this.setGroupDomain(existing.getGroupDomain());
    }

    /**
     * Get the domain that holds the players.
     *
     * @return a domain
     */
    public PlayerDomain getPlayerDomain() 
    {
        return this.playerDomain;
    }

    /**
     * Set a new player domain.
     *
     * @param playerDomain a domain
     */
    public void setPlayerDomain(PlayerDomain playerDomain) 
    {
        checkNotNull(playerDomain);
        this.playerDomain = new PlayerDomain(playerDomain);
    }

    /**
     * Set the domain that holds the groups.
     *
     * @return a domain
     */
    public GroupDomain getGroupDomain() 
    {
        return this.groupDomain;
    }

    /**
     * Set a new group domain.
     *
     * @param groupDomain a domain
     */
    public void setGroupDomain(GroupDomain groupDomain) 
    {
        checkNotNull(groupDomain);
        this.groupDomain = new GroupDomain(groupDomain);
    }

    /**
     * Add the given player to the domain, identified by the player's name.
     *
     * @param name the name of the player
     */
    public void addPlayer(String name) 
    {
    	this.playerDomain.addPlayer(name);
    }

    /**
     * Remove the given player from the domain, identified by the player's name.
     *
     * @param name the name of the player
     */
    public void removePlayer(String name) 
    {
    	this.playerDomain.removePlayer(name);
    }

    /**
     * Remove the given player from the domain, identified by the player's UUID.
     *
     * @param uuid the UUID of the player
     */
    public void removePlayer(UUID uuid) 
    {
    	this.playerDomain.removePlayer(uuid);
    }

    /**
     * Add the given player to the domain, identified by the player's UUID.
     *
     * @param uniqueId the UUID of the player
     */
    public void addPlayer(UUID uniqueId) 
    {
    	this.playerDomain.addPlayer(uniqueId);
    }

    /**
     * Remove the given player from the domain, identified by either the
     * player's name, the player's unique ID, or both.
     *
     * @param player the player
     */
    public void removePlayer(LocalPlayer player) 
    {
    	this.playerDomain.removePlayer(player);
    }

    /**
     * Add the given player to the domain, identified by the player's UUID.
     *
     * @param player the player
     */
    public void addPlayer(LocalPlayer player) 
    {
    	this.playerDomain.addPlayer(player);
    }

    /**
     * Add all the entries from another domain.
     *
     * @param other the other domain
     */
    public void addAll(DefaultDomain other) 
    {
        checkNotNull(other);
        for (String player : other.getPlayers()) 
        {
        	this.addPlayer(player);
        }
        for (UUID uuid : other.getUniqueIds()) 
        {
        	this.addPlayer(uuid);
        }
        for (String group : other.getGroups()) 
        {
        	this.addGroup(group);
        }
    }

    /**
     * Remove all the entries from another domain.
     *
     * @param other the other domain
     */
    public void removeAll(DefaultDomain other) 
    {
        checkNotNull(other);
        for (String player : other.getPlayers()) 
        {
        	this.removePlayer(player);
        }
        for (UUID uuid : other.getUniqueIds()) 
        {
            this.removePlayer(uuid);
        }
        for (String group : other.getGroups()) 
        {
        	this.removeGroup(group);
        }
    }

    /**
     * Get the set of player names.
     *
     * @return the set of player names
     */
    public Set<String> getPlayers() 
    {
        return this.playerDomain.getPlayers();
    }

    /**
     * Get the set of player UUIDs.
     *
     * @return the set of player UUIDs
     */
    public Set<UUID> getUniqueIds() {
        return this.playerDomain.getUniqueIds();
    }

    /**
     * Add the name of the group to the domain.
     *
     * @param name the name of the group.
     */
    public void addGroup(String name) 
    {
    	this.groupDomain.addGroup(name);
    }

    /**
     * Remove the given group from the domain.
     *
     * @param name the name of the group
     */
    public void removeGroup(String name) 
    {
    	this.groupDomain.removeGroup(name);
    }

    /**
     * Get the set of group names.
     *
     * @return the set of group names
     */
    public Set<String> getGroups() 
    {
        return this.groupDomain.getGroups();
    }

    @Override
    public boolean contains(LocalPlayer player) 
    {
        return playerDomain.contains(player) || groupDomain.contains(player);
    }

    @Override
    public boolean contains(UUID uniqueId) 
    {
        return playerDomain.contains(uniqueId);
    }

    @Override
    public boolean contains(String playerName) 
    {
        return playerDomain.contains(playerName);
    }

    @Override
    public int size() 
    {
        return this.groupDomain.size() + this.playerDomain.size();
    }

    @Override
    public void clear() 
    {
    	this.playerDomain.clear();
    	this.groupDomain.clear();
    }

    public void removeAll() {
        clear();
    }

    public String toPlayersString() 
    {
        return toPlayersString(null);
    }

    @SuppressWarnings("deprecation")
    public String toPlayersString(@Nullable ProfileCache cache) 
    {
        StringBuilder str = new StringBuilder();
        List<String> output = new ArrayList<String>();

        for (String name : playerDomain.getPlayers()) 
        {
            output.add("name:" + name);
        }

        if (cache != null) 
        {
            ImmutableMap<UUID, Profile> results = cache.getAllPresent(this.playerDomain.getUniqueIds());
            for (UUID uuid : this.playerDomain.getUniqueIds()) 
            {
                Profile profile = results.get(uuid);
                if (profile != null) 
                {
                    output.add(profile.getName() + "*");
                } 
                else 
                {
                    output.add("uuid:" + uuid);
                }
            }
        } else 
        {
            for (UUID uuid : this.playerDomain.getUniqueIds()) 
            {
                output.add("uuid:" + uuid);
            }
        }

        Collections.sort(output, String.CASE_INSENSITIVE_ORDER);
        for (Iterator<String> it = output.iterator(); it.hasNext();) 
        {
            str.append(it.next());
            if (it.hasNext()) 
            {
                str.append(", ");
            }
        }
        return str.toString();
    }
    
    public String toGroupsString() 
    {
        StringBuilder str = new StringBuilder();
        for (Iterator<String> it = this.groupDomain.getGroups().iterator(); it.hasNext(); ) 
        {
            str.append("*");
            str.append(it.next());
            if (it.hasNext()) 
            {
                str.append(", ");
            }
        }
        return str.toString();
    }

    public String toUserFriendlyString() 
    {
        StringBuilder str = new StringBuilder();

        if (this.playerDomain.size() > 0) 
        {
            str.append(toPlayersString());
        }

        if (this.groupDomain.size() > 0) 
        {
            if (str.length() > 0) 
            {
                str.append("; ");
            }

            str.append(toGroupsString());
        }

        return str.toString();
    }

    public String toUserFriendlyString(ProfileCache cache) 
    {
        StringBuilder str = new StringBuilder();

        if (this.playerDomain.size() > 0) 
        {
            str.append(toPlayersString(cache));
        }

        if (this.groupDomain.size() > 0) 
        {
            if (str.length() > 0) 
            {
                str.append("; ");
            }

            str.append(toGroupsString());
        }

        return str.toString();
    }

    @Override
    public boolean isDirty() 
    {
        return this.playerDomain.isDirty() || this.groupDomain.isDirty();
    }

    @Override
    public void setDirty(boolean dirty) 
    {
    	this.playerDomain.setDirty(dirty);
    	this.groupDomain.setDirty(dirty);
    }

    @Override
    public String toString() 
    {
        return "{players=" + this.playerDomain + ", groups=" + this.groupDomain +'}';
    }

}
