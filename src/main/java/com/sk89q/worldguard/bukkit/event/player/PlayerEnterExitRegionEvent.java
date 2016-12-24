package com.sk89q.worldguard.bukkit.event.player;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlayerEnterExitRegionEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private Set<ProtectedRegion> entered;
	private Set<ProtectedRegion> exited;
	
	public PlayerEnterExitRegionEvent(Player player, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited)
	{
		this.player = player;
		this.entered = entered;
		this.exited = exited;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public Set<ProtectedRegion> getEnteredRegions()
	{
		return this.entered;
	}
	
	public Set<ProtectedRegion> getExitedRegions()
	{
		return this.exited;
	}
	
    @Override
    public HandlerList getHandlers() 
    {
        return handlers;
    }

    public static HandlerList getHandlerList() 
    {
        return handlers;
    }
}
