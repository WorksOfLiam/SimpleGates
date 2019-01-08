package com.worksofbarry.sg;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.worksofbarry.sg.Listeners.BlockListener;

public class SimpleGates extends JavaPlugin {
    private final BlockListener blockListener = new BlockListener();
	
	@Override
	public void onEnable() {
		
		//Add seperate listeners here
		PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(blockListener, this);
        
        NamespacedKey key = new NamespacedKey(this, getDescription().getName());
        ShapedRecipe disk = new ShapedRecipe(key, Gate.GetGateBlock());
        disk.shape(new String[]{"*A*","*B*","*C*"}).setIngredient('A', Material.REDSTONE).setIngredient('B', Material.STONE).setIngredient('C', Material.IRON_FENCE); 
        Bukkit.getServer().addRecipe(disk);
        
		if (!new File(getDataFolder(), "config.yml").exists())
			saveDefaultConfig();
		
        LoadGates();
       
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Gate block : Gate.List.values()) {
					block.DoWork();
				}
			}
		}, 20, 20);
	}

	@Override
	public void onDisable() {
		SaveGates();
	}
	
	public void LoadGates() {
		ConfigurationSection currentConfig;
		if (getConfig().isSet("gates")) {
			for(String key : getConfig().getConfigurationSection("gates").getKeys(false)) {
				currentConfig = getConfig().getConfigurationSection("gates." + key);
				Gate.List.put(key, new Gate(LocationFromString(key), currentConfig.getDouble("DrillY")));
			}
		}
	}
	
	public void SaveGates() {
		String location;
		ConfigurationSection currentConfig;
		
		getConfig().set("gates", null);
		for (Gate quary : Gate.List.values()) {
			location = Gate.LocationString(quary.getBlockLocation());
			currentConfig = getConfig().createSection("gates." + location);
			
			currentConfig.set("DrillY", quary.getDrillY());
		}
		
		saveConfig();
	}
	
	public Location LocationFromString(String loc) {
		String[] data = loc.split(",");
		return new Location(Bukkit.getWorld(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]));
	}
	
}

