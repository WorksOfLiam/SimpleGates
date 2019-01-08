package com.worksofbarry.sg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gate {

	public static Map<String, Gate> List = new HashMap<String, Gate>();

	public static String LocationString(Location BlockLocation) {
		String result = BlockLocation.getWorld().getName() + "," + 
	                    String.valueOf(BlockLocation.getBlockX()) + "," +
	                    String.valueOf(BlockLocation.getBlockY()) + "," +
	                    String.valueOf(BlockLocation.getBlockZ());
		
		return result;
	}

	public static void createGate(Location loc) {
		List.put(LocationString(loc), new Gate(loc));
	}
	
	public static boolean isGateBlock(Location loc) {
		return List.containsKey(LocationString(loc));
	}
	
	public static void deleteGate(Location loc) {
		String key = LocationString(loc);
		if (List.containsKey(key)) {
			List.remove(key);
		}
	}
	
	public static ItemStack GetGateBlock() {
		ItemStack item = new ItemStack(Material.STONE); 
        ItemMeta im = item.getItemMeta(); 
        im.setDisplayName("Gate Block"); 
        List<String> lore = new ArrayList<String>(); 
        lore.add("When powered, this block"); 
        lore.add("will lower the gate.");
        im.setLore(lore); 
        item.setItemMeta(im); 
        return item;
	}
	
	public static boolean isActive(Location loc) {
		double y = List.get(LocationString(loc)).getDrillY();
		return (y > 0);
	}
	
	public static boolean isGatePart(Location loc) {
		int fenceCount = 0;
		while (true) {
			switch (loc.getBlock().getType()) {
			case IRON_FENCE:
				loc.setY(loc.getY() + 1);
				fenceCount++;
				
				if (fenceCount >= 10)
					return true; //Stop counting after 6 fences to improve performance
				break;
			case STONE:
				return (isGateBlock(loc));
			default:
				return false;
			}
		}
	}
	
	private Location BlockLocation;
	private double GateY; //Relative to BlockLocation.Y

	public Gate(Location key) {
		this.BlockLocation = key;
		this.GateY = 0;
	}
	
	public Gate(Location key, double drillY) {
		this.BlockLocation = key;
		this.GateY = drillY;
	}

	public Location getBlockLocation() {
		return BlockLocation;
	}
	public double getDrillY() {
		return GateY;
	}
	
	public void DoWork() {
		Block quaryBlock = BlockLocation.getBlock(), drillBlock;
		Location drillLocation;

		drillLocation = BlockLocation.clone();
		
		if (quaryBlock.isBlockIndirectlyPowered()) {
			drillLocation.setY(BlockLocation.getY() - (GateY+1));
			
			drillBlock = drillLocation.getBlock();
			
			if (drillBlock.getType().equals(Material.AIR)) {
				drillBlock.setType(Material.IRON_FENCE);
				GateY++;
			}
			
		} else {
			if (GateY >= 1) {
				drillLocation.setY(BlockLocation.getY() - GateY);
				drillLocation.getBlock().setType(Material.AIR);
				this.GateY--;
			}
		}
	}
}
