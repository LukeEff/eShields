package io.github.lukeeff;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class eShields extends JavaPlugin {

	FileConfiguration config = this.getConfig();

	@SuppressWarnings("rawtypes")
	HashMap<UUID, HashMap> data = new HashMap<>();

	@Override
	public void onEnable() {

		configDefaults();
		getServer().getPluginManager().registerEvents(new ShieldListener(this), this);
		this.getCommand("shield").setExecutor(new CommandExecute());
	}

	@Override
	public void onDisable() {

	}
	/*
	 * Default values for configuration file.
	 */
	void configDefaults() {

		//TODO Add several configurable defaults to this.
		config.addDefault("Shield_Health", 30.0);
		config.addDefault("Regeneration_Time", 40.0);
		config.options().copyDefaults(true);
		saveConfig();

	}

	
}
