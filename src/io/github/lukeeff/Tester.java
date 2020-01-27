package io.github.lukeeff;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

//TODO Rebuild this entire class better...

public class Tester {

	// Testing our config for incorrect input.

	eShields plugin;
	private final Object[] soundCategory;
	private final Object[] floatCategory;
	private final Object[] longCategory;
	private final Object[] doubleCategory;
	private final Object[] barStyleCategory;
	private final Object[] barColorCategory;
	private final Object[] stringCategory;
	private final Object[] testSubjects;
	private final Object[] sections;
	final HashMap[] typeMaps; // Each type map checks if value is possible for object.
	private final HashMap<Object[], HashMap> mapGetter;
	private int hashCode;
	private final HashMap<String, Float> floatMap;
	private final HashMap<String, Long> longMap;
	private final HashMap<String, Double> doubleMap;
	private final HashMap<String, String> stringMap;
	Object[] objectCategory;
	// protected final boolean properValues;

	public Tester(eShields instance) {

		plugin = instance;
		floatMap = new HashMap<String, Float>();
		longMap = new HashMap<String, Long>();
		doubleMap = new HashMap<String, Double>();
		stringMap = new HashMap<String, String>();

		soundCategory = new Object[] { plugin.shieldRegenSound, plugin.shieldFractureSound };
		floatCategory = new Object[] { plugin.shieldRegenVolume, plugin.shieldRegenPitch, plugin.shieldFractureVolume,
				plugin.shieldFracturePitch };
		longCategory = new Object[] { eShields.DEFAULT_COOLDOWN };
		doubleCategory = new Object[] { plugin.shieldHealth, plugin.shieldRegenPerTick };
		barStyleCategory = new Object[] { plugin.shieldStyle };
		barColorCategory = new Object[] { plugin.healthyShieldColor, plugin.fracturedShieldColor };
		stringCategory = new Object[] { plugin.shieldName };
		mapGetter = new HashMap<Object[], HashMap>();
		typeMaps = new HashMap[] { plugin.soundMap, floatMap, longMap, doubleMap, plugin.shieldStyleMap,
				plugin.shieldColorMap, stringMap };
		testSubjects = new Object[] { soundCategory, floatCategory, longCategory, doubleCategory, barStyleCategory,
				barColorCategory, stringCategory };
		sections = new Object[] { plugin.shieldListenerSection, plugin.shieldSoundSection, plugin.cooldownSection };
		testerMapSetter(testSubjects, typeMaps);
		floatMapSetter();
		doubleMapSetter();
		longMapSetter();
		stringMapSetter();

	}

	void checkConfig() {

		for (Object testSubject : testSubjects) {
			objectCategory = (Object[]) testSubject;
			HashMap<String, Object> valueMap = getMap(objectCategory);
			isCorrect(valueMap, objectCategory);
		}

	}

	private void testerMapSetter(Object[] testSubjects, HashMap[] typeMaps) {
		for (int i = 0; i < testSubjects.length; i++) {
			mapGetter.put((Object[]) testSubjects[i], typeMaps[i]);
		}

	}

	private HashMap<String, Object> getMap(Object[] objectTest) {
		hashCode = mapGetter.get(objectTest).hashCode();
		return mapGetter.get(objectTest);
	}

	private String getSection(String configKey) {
		String sectionName = null;
		for (Object section : sections) {

			Object[] configSections = (Object[]) section;
			ConfigurationSection configSection = plugin.getSection((String) configSections[0]);
			if (configSection.contains(configKey)) {
				sectionName = configSection.getName();
				return sectionName;
			}
		}
		Bukkit.getConsoleSender().sendMessage(
				ChatColor.DARK_RED + "Warning: " + ChatColor.YELLOW + " config section not found for: " + configKey);
		return sectionName;
	}

	/*
	 * Check for null or bad syntax. If null, replace with default value. If bad
	 * syntax, replace with default value.
	 */
	// TODO New file for improper syntax
	private final boolean isCorrect(HashMap<String, Object> valueMap, Object[] testBatchDefaults) {
		boolean correct = true;

		for (Object defaultKeyValues : testBatchDefaults) {
			Object[] defaultKeyValue = (Object[]) defaultKeyValues;
			String configKey = (String) defaultKeyValue[0];
			String section = getSection(configKey);
			Object defaultObject = defaultKeyValue[1];
			Object configObject = getSectionObject(section, configKey);
			correct = checkCorrect(valueMap, section, configObject, configKey, defaultObject, correct);
		}
		return correct;
	}

	public void floatMapSetter() {
		for (Object floatValue : floatCategory) {
			Object[] floatValueForMap = (Object[]) floatValue;
			String name = (String) floatValueForMap[0];
			Float value = (Float) floatValueForMap[1];
			floatMap.put(name, value);
		}
	}

	public void stringMapSetter() {
		for (Object stringValue : stringCategory) {
			Object[] stringValueForMap = (Object[]) stringValue;
			String name = (String) stringValueForMap[0];
			String value = (String) stringValueForMap[1];
			stringMap.put(name, value);
		}
	}

	public void longMapSetter() {
		for (Object longValue : longCategory) {
			Object[] longValueForMap = (Object[]) longValue;
			String name = (String) longValueForMap[0];
			Long value = (Long) longValueForMap[1];
			longMap.put(name, value);
		}
	}

	public void doubleMapSetter() {
		for (Object doubleValue : doubleCategory) {
			Object[] doubleValueForMap = (Object[]) doubleValue;
			String name = (String) doubleValueForMap[0];
			Double value = (Double) doubleValueForMap[1];
			doubleMap.put(name, value);
		}
	}

	private boolean checkCorrect(HashMap<String, Object> valueMap, String section, Object configObject,
			String configKey, Object defaultObject, boolean correct) {

		boolean valid = checkIfValid(valueMap, section, configKey);
		if (!valid) {

			Bukkit.getConsoleSender()
					.sendMessage(ChatColor.DARK_RED + "Warning: " + ChatColor.YELLOW + "config object " + configKey
							+ " with value: " + getSectionObject(section, configKey)

							+ " could not load correctly! Overwritten to default value, " + defaultObject + ". Please type "
							+ ChatColor.AQUA + "/eCategory to see a list of possible inputs.");
			setSectionObject(section, configKey, defaultObject);
			return false;
		} else {
			return correct;
		}
	}

	private void setSectionObject(String section, String name, Object value) {
		plugin.getSection(section).set(name, value);
	}

	private final Object getSectionObject(String section, String name) {

		return plugin.getSection(section).get(name);
	}

	private boolean checkIfValid(HashMap<String, ?> valueMap, String section, String configKey) {
		for (Object value : valueMap.values().toArray()) {

			String mapString = value.toString();
			String configString = getSectionObject(section, configKey).toString();

			if (mapString.equals(configString)) {

				return true;
			} else if (getSectionObject(section, configKey) instanceof Double
					| getSectionObject(section, configKey) instanceof Integer
					| (getSectionObject(section, configKey) instanceof String) & valueMap.equals(stringMap)) {

				return true;
			}
		}
		return false;
	}

}