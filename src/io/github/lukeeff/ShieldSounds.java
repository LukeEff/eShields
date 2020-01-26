package io.github.lukeeff;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ShieldSounds {
	// TODO Set all field objects to values in a config file.
	// TODO Make sound volumes modifiable to give effect of cutting off.
	// TODO Low shield health sound.
	final private static Sound shieldRegen = Sound.BLOCK_BEACON_ACTIVATE;
	final private static float shieldRegenVolume = 1f;
	final private static float shieldRegenPitch = .4f;
	final private static Sound shieldFracture = Sound.BLOCK_ANVIL_LAND;
	final private static float shieldFractureVolume = 1f;
	final private static float shieldFracturePitch = .4f;
	/*
	 * Sound played when shield health hits 0.
	 */
	public static void shieldFracture(Player player) {
		player.playSound(player.getLocation(), shieldFracture, shieldFractureVolume, shieldFracturePitch);
	}
	/*
	 * Sound played when shield begins to regen.
	 */
	public static void shieldRegen(Player player) {
		player.playSound(player.getLocation(), shieldRegen, shieldRegenVolume, shieldRegenPitch);
	}
}
