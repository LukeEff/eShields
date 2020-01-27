package io.github.lukeeff;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandCategory implements CommandExecutor {

	eShields plugin;
	Tester maps;
	private HashMap[] categories;
	private HashMap<String, HashMap> key;
	public CommandCategory(eShields instance, Tester instance0) {
		plugin = instance;
		maps = instance0;
		categories = maps.typeMaps;
		key = new HashMap<String, HashMap>();
		key.put("soundcategory", plugin.soundMap);
		key.put("barcolorcategory", plugin.shieldStyleMap);
		key.put("barstylecategory", plugin.shieldColorMap);
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] inputName) {
		if (inputName.length > 0) {
		if (!(key.get(inputName[0].toLowerCase()) == null)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + inputName[0] + " possible values: " + ChatColor.AQUA + key.get(inputName[0]).values().toString());
			return true;
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error: You typed + /eCategories " + inputName[0] + " when the available options are: " + ChatColor.YELLOW  + key.keySet());
			return true;
		}
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error: You typed + /eCategories without an argument. Arguments: " + ChatColor.YELLOW  + key.keySet());
		return true;
	}

}
