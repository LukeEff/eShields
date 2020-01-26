package io.github.lukeeff;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Tester extends eShields {

	// Testing our sounds for incorrect input.
	private final Object[] SoundTest;

	public Tester() {
		SoundTest = new Object[] {shieldRegenSound, shieldFractureSound};
		soundConfigTester();
	}

	private boolean configProblem() {
		// config.getKeys(true);
		return true;
	}

	/*
	 * Check for null or bad syntax. If null, replace with default value. If bad
	 * syntax, replace with default value.
	 */
	private boolean soundConfigTester() {


		return true;
	}

	// TODO build loop for iterating through sound objects in config and see if
	// return null in getter
	// if soundmap get no equal object
	private final boolean isSoundCorrect() {
		boolean correct = true;
		for (Object[] configName : SoundTest.keySet()) {
			if (soundMap.get(configName[1]) == null) {
				correct = false;
				config.getConfigurationSection((String) shieldSoundSection[0]).set((String) configName[0], configName[1]);
				Bukkit.getConsoleSender()
						.sendMessage(ChatColor.DARK_RED + "Warning: " + ChatColor.YELLOW + "config object " + configName[0]
								+ ChatColor.YELLOW + " could not load correctly! Overwritten to default value.");
				// set config value to default
			}
			return true;
		}

		return correct;
	}

}