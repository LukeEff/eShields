package io.github.lukeeff;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class eShields extends JavaPlugin {

	// TODO field variables of all the defaults. Maybe make a map to hold key and
	// value and use that as reference.
	// TODO NMS implementation for shield regen visual, red vs blue team, etc
	// (skins).
	// TODO New class for config.
	
	//This is for configuration reference
	FileConfiguration config;
	File modifiedConfigFile; //Loads config file in datafolder.
	// These are objects of the ShieldListenerClass.
	protected final Object[] shieldHealth;
	protected final Object[] shieldName;
	protected final Object[] shieldRegenPerTick;
	protected final Object[] shieldStyle;
	protected final Object[] healthyShieldColor;
	protected final Object[] fracturedShieldColor;
	// These are objects of the ShieldCooldown class.
	protected static Object[] DEFAULT_COOLDOWN;
	// These are objects of the ShieldSounds class.
	protected final Object[] shieldRegenSound;
	protected final Object[] shieldRegenVolume;
	protected final Object[] shieldRegenPitch;
	protected final Object[] shieldFractureSound;
	protected final Object[] shieldFractureVolume;
	protected final Object[] shieldFracturePitch;
	// Objects that hold categories of these objects for config.
	protected final Object[] shieldListenerSection;
	protected final Object[] shieldSoundSection;
	protected final Object[] cooldownSection;
	protected final HashMap<String, BarStyle> shieldStyleMap;
	protected final HashMap<String, BarColor> shieldColorMap;
	protected final HashMap<String, Sound> soundMap;

	@SuppressWarnings("rawtypes")
	HashMap<UUID, HashMap> data = new HashMap<>();

	public eShields() {
		config = this.getConfig();
		modifiedConfigFile = new File(this.getDataFolder(), "config.yml");
		shieldHealth = new Object[] { "Shield health", 20 };
		shieldName = new Object[] { "Shield name", "Shield Status" };
		shieldRegenPerTick = new Object[] { "Shield percent regeneration per second", 20 };
		shieldStyle = new Object[] { "Shield Style", "SOLID" };
		healthyShieldColor = new Object[] { "Active shield color", "BLUE" };
		fracturedShieldColor = new Object[] { "Fractured shield color", "RED" };
		DEFAULT_COOLDOWN = new Object[] { "Cooldown before shield regenerates", 5 };
		shieldRegenSound = new Object[] { "Shield regeration sound", "BLOCK_BEACON_ACTIVATE" };
		shieldRegenVolume = new Object[] { "Shield regeneration volume", 1f };
		shieldRegenPitch = new Object[] { "Shield regeneration pitch", .4f };
		shieldFractureSound = new Object[] { "Shield fracture sound", "BLOCK_ANVIL_LAND" };
		shieldFractureVolume = new Object[] { "Shield fracture volume", 1f };
		shieldFracturePitch = new Object[] { "Shield fracture pitch", .4f };
		shieldListenerSection = new Object[] { "Shield Properties", shieldHealth, shieldName, shieldRegenPerTick,
				shieldStyle, healthyShieldColor, fracturedShieldColor };
		shieldSoundSection = new Object[] { "Sounds", shieldRegenSound,
				shieldRegenVolume, shieldRegenPitch, shieldFractureSound,
				shieldFractureVolume, shieldFracturePitch };
		cooldownSection = new Object[] { "Cooldowns", DEFAULT_COOLDOWN };
		shieldStyleMap = new HashMap<String, BarStyle>();
		shieldStyleMapSetter();
		shieldColorMap = new HashMap<String, BarColor>();
		shieldColorMapSetter();
		soundMap = new HashMap<String, Sound>();
		soundMapSetter();
		
	}

	@Override
	public void onEnable() {
		
		try {
			config.load(modifiedConfigFile);
			saveConfig();
		} catch (IOException | InvalidConfigurationException e) {
			
			configDefaults();
			e.printStackTrace();
		}

		getServer().getPluginManager().registerEvents(new ShieldListener(this), this);
		this.getCommand("eShields").setExecutor(new CommandExecute(this));
	}

	@Override
	public void onDisable() {

	}

	/*
	 * Default values for configuration file.
	 */
	void configDefaults() {

		// TODO Comment to describe each value.
		listenerDefaults();
		soundDefaults();
		cooldownDefaults();
		config.options().copyDefaults(true);

		saveConfig();

	}

	void soundDefaults() {
		config.createSection(getShieldSoundSectionName());
		addDefault(shieldSoundSection);

		
	}

	void listenerDefaults() {
		config.createSection(getShieldListenerSectionName());
		addDefault(shieldListenerSection);
	}

	void cooldownDefaults() {
		config.createSection(getCooldownSectionName());
		addDefault(cooldownSection);
	}

	void addDefault(Object[] section) {
		String sectionName = (String) section[0];
		boolean isFirst = true;
		for (Object configValuePairs : section) {
			if (!isFirst) {
				Object[] configValuePair = (Object[]) configValuePairs;
				if (configValuePair.length == 2 && (configValuePairs != section[0])) {
					String configName = (String) configValuePair[0];
					config.options().copyDefaults(true);
					saveConfig();
					config.getConfigurationSection(sectionName).addDefault(configName, configValuePair[1]);
				} else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED
							+ "Warning; incorrect number of objects passed from " + ChatColor.AQUA + sectionName);
				}
			} else {
				isFirst = false;
				
			}
		}
	}
	
	protected final void shieldColorMapSetter() {
		for (BarColor barColors : BarColor.values()) {
			shieldColorMap.put(barColors.name(), barColors);
			//Bukkit.getConsoleSender().sendMessage(barColors.name());
		}
	}
	
	protected final void soundMapSetter() {
		for(Sound sounds : Sound.values()) {
			soundMap.put(sounds.name(), sounds);
		}
	}
		
	
	protected final void shieldStyleMapSetter() {
		for (BarStyle barStyles : BarStyle.values()) {
			shieldStyleMap.put(barStyles.name(), barStyles);
		}
	}

	protected final String getShieldListenerSectionName() {
		return (String) shieldListenerSection[0];
	}

	protected final String getShieldSoundSectionName() {
		return (String) shieldSoundSection[0];
	}

	protected final String getCooldownSectionName() {
		return (String) cooldownSection[0];
	}

}
