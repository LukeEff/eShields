package io.github.lukeeff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldCooldown {

	ShieldListener shieldRestore;
	
	static eShields plugin;
	public static long DEFAULT_COOLDOWN;
	public ShieldCooldown(eShields instance) {
		plugin = instance;
		DEFAULT_COOLDOWN = configSectionGetLong(eShields.DEFAULT_COOLDOWN);
	}

	private final Map<UUID, Long> cooldowns = new HashMap<>();

	

	public void setCooldown(UUID p, long time) {
		if (time < 1) {
			cooldowns.remove(p);

		} else {
			cooldowns.put(p, (long) time);
		}
	}

	public long getCooldown(UUID p) {
		return cooldowns.getOrDefault(p, (long) 0);
	}

	/*
	 * Returns configuration section name for objects.
	 */
	private static String configSectionName() {
		return plugin.getCooldownSectionName();
	}

	/*
	 * Returns config double value from shieldListenerSection specified in
	 * parameter.
	 */
	private static long configSectionGetLong(Object[] configName) {
		return plugin.getConfig().getConfigurationSection(configSectionName()).getLong((String) configName[0]);
	}
}
