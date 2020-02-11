package io.github.lukeeff;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;

public class SmartConfigTester {

	eShields plugin;
	
	interface FuncInterTypes {
		boolean checkType(Object cfgObj);
	}
	
	private boolean check(Object cfgObj, FuncInterTypes isCorrectType) {
		return isCorrectType.checkType(cfgObj);
	}
	
	FuncInterTypes isString;
	FuncInterTypes isLong;
	FuncInterTypes isDouble;
	FuncInterTypes isSound;
	FuncInterTypes isBarColor;
	FuncInterTypes isBarStyle;
	FuncInterTypes typeChecker;
	protected final HashMap<String, BarStyle> shieldStyleMap;
	protected final HashMap<String, BarColor> shieldColorMap;
	protected final HashMap<String, Sound> soundMap;
	private final Object[] soundDefaults;
	private final Object[] floatDefaults;
	private final Object[] longDefaults;
	private final Object[] doubleDefaults;
	private final Object[] barStyleDefaults;
	private final Object[] barColorDefaults;
	private final Object[] stringDefaults;
	private final Object[] objectDefaults;
	private Object[] defaultObject;
	private HashMap<Object, FuncInterTypes> objectFuncMap;
	private String configKey;
	private String section;
	private final Object[] sections;
	Object configDefault;
	Object configObject;
	
	
	
	
	public SmartConfigTester(eShields instance) {
		plugin = instance;
		defIsString();
		defIsDouble();
		defIsSound();
		defIsBarColor();
		defIsBarStyle();
		defIsLong();
		sections = new Object[] { plugin.shieldListenerSection, plugin.shieldSoundSection, plugin.cooldownSection };
		soundDefaults = new Object[] { plugin.shieldRegenSound, plugin.shieldFractureSound };
		floatDefaults = new Object[] { plugin.shieldRegenVolume, plugin.shieldRegenPitch, plugin.shieldFractureVolume,
				plugin.shieldFracturePitch };
		longDefaults = new Object[] { eShields.DEFAULT_COOLDOWN };
		doubleDefaults = new Object[] { plugin.shieldHealth, plugin.shieldRegenPerTick };
		barStyleDefaults = new Object[] { plugin.shieldStyle };
		barColorDefaults = new Object[] { plugin.healthyShieldColor, plugin.fracturedShieldColor };
		stringDefaults = new Object[] { plugin.shieldName };
		objectDefaults = new Object[] { soundDefaults, floatDefaults, longDefaults, doubleDefaults, barStyleDefaults,
				barColorDefaults, stringDefaults };
		
		soundMap = plugin.soundMap;
		shieldColorMap = plugin.shieldColorMap;
		shieldStyleMap = plugin.shieldStyleMap;
		objectFuncMap = new HashMap<Object, FuncInterTypes>();
		mapSetStringType();
		mapSetDoubleType();
		mapSetLongType();
		mapSetFloatType();
		mapSetSoundType();
		mapSetBarColorType();
		mapSetBarStyleType();
		
	}
	
	/*
	 * This is the checker method.
	 */
	public void checkConfig() {
		
		for (Object objectDefault : objectDefaults) {
			this.defaultObject = (Object[]) objectDefault;
			checkValid();
		}
		
	}
	
	/*
	 * Lets use our objects to ensure
	 * the user inputs from config
	 * can be used in our code
	 */
	private void checkValid() {
		
		for (Object defaultKeyValues : this.defaultObject) {
			Object[] defaultKeyValue = (Object[]) defaultKeyValues;
			configKey = (String) defaultKeyValue[0];
			section = getSection();
			configDefault = defaultKeyValue[1];
			configObject = getSectionObject(); 
			checkCompatible();
		}
		
	}

	/*
	 * Checks if the config object is going
	 * to work in our code and will fix it
	 * if it won't work.
	 */
	private void checkCompatible() {
		
		typeChecker = objectFuncMap.get(configKey);
		
		boolean correct = check(configObject, typeChecker);
		if (!correct) {
			Bukkit.getConsoleSender()
			.sendMessage(ChatColor.DARK_RED + "Warning: " + ChatColor.YELLOW + "config object " + configKey
					+ " with value: " + getSectionObject()

					+ " could not load correctly! Overwritten to default value, " + defaultObject + ". Please type "
					+ ChatColor.AQUA + "/eCategory to see a list of possible inputs.");
	setSectionObject();
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Config object " + configKey + " with value: " + getSectionObject() + " successfully loaded.");
		}
	}
	
	
	/*
	 * Get the section of our object being
	 * checked.
	 */
	private String getSection() {
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
	 * Sets the config object to a value
	 */
	private void setSectionObject()  
	{
		plugin.getSection(section).set(configKey, configDefault);
	}

	/*
	 * Gets the config object
	 */
	private final Object getSectionObject() {

		return plugin.getSection(section).get(configKey);
	}
	
	
	/*
	 * Key value pair of sound and a way to check the type.
	 */
	private void mapSetSoundType() {
		for (Object soundObject : soundDefaults) {
			Object[] defaultSoundObject = (Object[]) soundObject;
			objectFuncMap.put(defaultSoundObject[0], isSound);
		}
	}
	
	/*
	 * Key value pair of string and a way to check the type.
	 */
	private void mapSetStringType() {
		for (Object stringObject : stringDefaults) {
			Object[] defaultStringObject = (Object[]) stringObject;
			
			objectFuncMap.put(defaultStringObject[0], isString);
			
		}
	}
	
	/*
	 * Key value pair of double and a way to check the type.
	 */
	private void mapSetDoubleType() {
		for (Object doubleObject : doubleDefaults) {
			Object[] defaultDoubleObject = (Object[]) doubleObject;
			objectFuncMap.put(defaultDoubleObject[0], isDouble);
			
		}
	}
	
	/*
	 * Key value pair of long and a way to check the type.
	 * Config reads long, int as double so we just
	 * check if it's a double
	 */
	private void mapSetLongType() {
		for (Object longObject : longDefaults) {
			Object[] defaultLongObject = (Object[]) longObject;
			objectFuncMap.put(defaultLongObject[0], isLong);
			
		}
	}
	
	/*
	 * Key value pair of float and a way to check the type.
	 * Config reads long, float as double so we just
	 * check if it's a double
	 */
	private void mapSetFloatType() {
		for (Object floatObject : floatDefaults) {
			Object[] defaultFloatObject = (Object[]) floatObject;
			objectFuncMap.put(defaultFloatObject[0], isDouble);
			
		}
	}
	
	/*
	 * Key value pair of bar color and a way to check the type.
	 */
	private void mapSetBarColorType() {
		for (Object barColorObject : barColorDefaults) {
			Object[] defaultBarColorObject = (Object[]) barColorObject;
			objectFuncMap.put(defaultBarColorObject[0], isBarColor);
			
		}
	}
	
	/*
	 * Key value pair of bar color and a way to check the type.
	 */
	private void mapSetBarStyleType() {
		for (Object barStyleObject : barStyleDefaults) {
			Object[] defaultBarStyleObject = (Object[]) barStyleObject;
			objectFuncMap.put(defaultBarStyleObject[0], isBarStyle);
			
		}
	}
	
	
	/*
	 * Gives a reference to a method
	 * for checking if type is string.
	 */
	private void defIsString() {
		this.isString = (Object cfgObj) -> {
			if (cfgObj instanceof String) {
				return true;
			} else {
				return false;
			}};
	}
	
	/*
	 * Gives a reference to a method
	 * for checking if type is double.
	 */
	private void defIsDouble() {
		this.isDouble = (Object cfgObj) -> {
			if (cfgObj instanceof Double | cfgObj instanceof Long | cfgObj instanceof Integer) {
				return true;
			} else {
				return false;
			}};
	}
	
	/*
	 * Gives a reference to a method
	 * for checking if type is long.
	 */
	private void defIsLong() {
		this.isLong = (Object cfgObj) -> {
			if (cfgObj instanceof Long | cfgObj instanceof Integer) {
				return true;
			} else {
				return false;
			}};
	}
	
	/*
	 * Gives a reference to a method
	 * for checking if type is sound.
	 */
	private void defIsSound() {
		this.isSound = (Object cfgObj) -> {
			String cfgSndKey = cfgObj.toString();
			if (soundMap.containsKey(cfgSndKey)) {
				return true;
			} else {
				return false;
			}};
	}
	
	/*
	 * Gives a reference to a method
	 * for checking if type is bar color.
	 */
	private void defIsBarColor() {
		this.isBarColor = (Object cfgObj) -> {
			String cfgColorKey = cfgObj.toString();
			if (shieldColorMap.containsKey(cfgColorKey)) {
				return true;
			} else {
				return false;
			}};
	}
	
	/*
	 * Gives a reference to a method
	 * for checking if type is bar style.
	 */
	private void defIsBarStyle() {
		this.isBarStyle = (Object cfgObj) -> {
			String cfgStyleKey = cfgObj.toString();
			if (shieldStyleMap.containsKey(cfgStyleKey)) {
				return true;
			} else {
				return false;
			}};
	}
}
