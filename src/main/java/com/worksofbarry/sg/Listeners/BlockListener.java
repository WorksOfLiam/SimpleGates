package com.worksofbarry.sg.Listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.worksofbarry.sg.Gate;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockBuild(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Location blockLoc = event.getBlock().getLocation();

        if (item.getItemMeta().hasDisplayName()) {
        	if (item.getItemMeta().getDisplayName().equals("Gate Block")) {
        		Gate.createGate(blockLoc);
        	}
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	Block block = event.getBlock();
    	
    	switch (block.getType()) {
    	case STONE:
    		if (Gate.isGateBlock(block.getLocation())) {
    			if (Gate.isActive(block.getLocation())) {
    				event.setCancelled(true);
    			} else {
	    			event.setDropItems(false);
	    			block.getLocation().getWorld().dropItemNaturally(block.getLocation(), Gate.GetGateBlock());
	    			Gate.deleteGate(block.getLocation());
    			}
    		}
    		break;
    		
    	case GRASS:
    		if (Gate.isGatePart(block.getLocation())) {
    			event.setCancelled(true);
    		}
    		break;
		default:
			break;
    	}
    }
}
