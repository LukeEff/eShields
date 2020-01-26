package io.github.lukeeff;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ShieldSounds {
	
	// TODO Make sound volumes modifiable to give effect of cutting off.
	// TODO Low shield health sound.
	eShields plugin;
	final private Sound shieldRegenSound;
	final private float shieldRegenVolume;
	final private float shieldRegenPitch;
	final private Sound shieldFractureSound;
	final private float shieldFractureVolume;
	final private float shieldFracturePitch;

	
	public ShieldSounds(eShields instance) {
		plugin = instance;
		shieldRegenSound = configSectionGetSound(plugin.shieldRegenSound);
		shieldRegenVolume = configSectionGetFloat(plugin.shieldRegenVolume);
		shieldRegenPitch = configSectionGetFloat(plugin.shieldRegenPitch);
		shieldFractureSound = configSectionGetSound(plugin.shieldFractureSound);
		shieldFractureVolume = configSectionGetFloat(plugin.shieldFractureVolume);
		shieldFracturePitch = configSectionGetFloat(plugin.shieldFracturePitch);

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
	private String configSoundSectionName() {
		return plugin.getShieldSoundSectionName();
	}

	/*
	 * Returns config Sound value from shieldFractureSoundSection specified in parameter.
	 */
	private Sound configSectionGetSound(Object[] configName) {
		return plugin.soundMap.get(getSoundName(configName));
		
		//return plugin.getConfig().getConfigurationSection(configSoundSectionName()).getObject((String) configName[0], Sound.class);
	}
	private final String getSoundName(Object[] configName) {
		return plugin.getConfig().getConfigurationSection(configSoundSectionName()).getString((String) configName[0]).toUpperCase();
	}
	
	/*
	 * Returns config float value from shieldFractureSoundSection specified in parameter.
	 */
	private float configSectionGetFloat(Object[] configName) {
		return (float) plugin.getConfig().getConfigurationSection(configSoundSectionName()).getDouble((String) configName[0]);
	}
}
