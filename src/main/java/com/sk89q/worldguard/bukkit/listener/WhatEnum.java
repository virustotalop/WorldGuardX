package com.sk89q.worldguard.bukkit.listener;

public enum WhatEnum {

	OPEN_THAT,BREAK_THAT_BLOCK,PLACE_THAT_BLOCK,PLACE_THAT_FIRE,USE_THAT;
	
	public String toString()
	{
		return this.name().replace("_", " ").toLowerCase();
	}
	
}
