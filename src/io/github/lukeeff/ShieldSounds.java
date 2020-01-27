package io.github.lukeeff;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ShieldSounds {
	
	// TODO Make sound volumes modifiable to give effect of cutting off.
	// TODO Low shield health sound.
	static eShields plugin;
	private static Sound shieldRegenSound;
	private static float shieldRegenVolume;
	private static float shieldRegenPitch;
	private static Sound shieldFractureSound;
	private static float shieldFractureVolume;
	private static float shieldFracturePitch;

	
	public ShieldSounds(eShields instance) {
		plugin = instance;
		setVariables();

	}
	
	static void setVariables() {
		shieldRegenSound = configSectionGetSound(plugin.shieldRegenSound);
		shieldRegenVolume = configSectionGetFloat(plugin.shieldRegenVolume);
		shieldRegenPitch = configSectionGetFloat(plugin.shieldRegenPitch);
		shieldFractureSound = configSectionGetSound(plugin.shieldFractureSound);
		shieldFractureVolume = configSectionGetFloat(plugin.shieldFractureVolume);
		shieldFracturePitch = configSectionGetFloat(plugin.shieldFracturePitch);
	}
	
	static void reload() {
		setVariables();
	}
	
	/*
	 * Sound played when shield health hits 0.
	 */
	public void shieldFracture(Player player) {
		player.playSound(playerLocation(player), shieldFractureSound, shieldFractureVolume, shieldFracturePitch);
	}

	/*
	 * Sound played when shield begins to regen.
	 */
	public void shieldRegen(Player player) {
		player.playSound(playerLocation(player), shieldRegenSound, shieldRegenVolume, shieldRegenPitch);
	}

	/*
	 * Returns player location.
	 */
	private static Location playerLocation(Player player) {
		return player.getLocation();
	}
	/*
	 * Returns regen configuration section name for objects.
	 */
	private static String configSoundSectionName() {
		return plugin.getSoundSectionName();
	}

	/*
	 * Returns config Sound value from shieldFractureSoundSection specified in parameter.
	 */
	private static Sound configSectionGetSound(Object[] configName) {
		return plugin.soundMap.get(getSoundName(configName));
		
		//return plugin.getConfig().getConfigurationSection(configSoundSectionName()).getObject((String) configName[0], Sound.class);
	}
	private static final String getSoundName(Object[] configName) {
		return plugin.getConfig().getConfigurationSection(configSoundSectionName()).getString((String) configName[0]).toUpperCase();
	}
	
	/*
	 * Returns config float value from shieldFractureSoundSection specified in parameter.
	 */
	private static float configSectionGetFloat(Object[] configName) {
		return (float) plugin.getConfig().getConfigurationSection(configSoundSectionName()).getDouble((String) configName[0]);
	}
}
