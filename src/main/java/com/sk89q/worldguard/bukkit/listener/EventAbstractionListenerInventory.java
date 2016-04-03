package com.sk89q.worldguard.bukkit.listener;

import static com.sk89q.worldguard.bukkit.cause.Cause.create;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.cause.Cause;
import com.sk89q.worldguard.bukkit.event.block.UseBlockEvent;
import com.sk89q.worldguard.bukkit.event.entity.UseEntityEvent;
import com.sk89q.worldguard.bukkit.listener.debounce.legacy.InventoryMoveItemEventDebounce;
import com.sk89q.worldguard.bukkit.listener.debounce.legacy.AbstractEventDebounce.Entry;
import com.sk89q.worldguard.bukkit.util.Events;

public class EventAbstractionListenerInventory extends AbstractListener {

	private final InventoryMoveItemEventDebounce moveItemDebounce = new InventoryMoveItemEventDebounce(30000);
	
	 /**
     * Construct the listener.
     *
     * @param plugin an instance of WorldGuardPlugin
     */
	public EventAbstractionListenerInventory(WorldGuardPlugin plugin) 
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) 
	{
        final InventoryHolder causeHolder = event.getInitiator().getHolder();
        InventoryHolder sourceHolder = event.getSource().getHolder();
        InventoryHolder targetHolder = event.getDestination().getHolder();

        Entry entry;

        if ((entry = moveItemDebounce.tryDebounce(event)) != null) 
        {
            Cause cause;

            if (causeHolder instanceof Entity) 
            {
                cause = create(causeHolder);
            } 
            else if (causeHolder instanceof BlockState) 
            {
                cause = create(((BlockState) causeHolder).getBlock());
            } else {
                cause = Cause.unknown();
            }

            if (!causeHolder.equals(sourceHolder)) 
            {
                handleInventoryHolderUse(event, cause, sourceHolder);
            }

            handleInventoryHolderUse(event, cause, targetHolder);

            if (event.isCancelled() && causeHolder instanceof Hopper) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        ((Hopper) causeHolder).getBlock().breakNaturally();
                    }
                });
            } else {
                entry.setCancelled(event.isCancelled());
            }
        }
    }
	
    @SuppressWarnings("unchecked")
    private static <T extends Event & Cancellable> void handleInventoryHolderUse(T originalEvent, Cause cause, InventoryHolder holder) 
    {
        if (originalEvent.isCancelled()) 
        {
            return;
        }

        if (holder instanceof Entity) 
        {
            Events.fireToCancel(originalEvent, new UseEntityEvent(originalEvent, cause, (Entity) holder));
        } 
        else if (holder instanceof BlockState) 
        {
            
        	Events.fireToCancel(originalEvent, new UseBlockEvent(originalEvent, cause, ((BlockState) holder).getBlock()));
        } 
        else if (holder instanceof DoubleChest) 
        {
            Events.fireToCancel(originalEvent, new UseBlockEvent(originalEvent, cause, ((BlockState) ((DoubleChest) holder).getLeftSide()).getBlock()));
            Events.fireToCancel(originalEvent, new UseBlockEvent(originalEvent, cause, ((BlockState) ((DoubleChest) holder).getRightSide()).getBlock()));
        }
    }
}
