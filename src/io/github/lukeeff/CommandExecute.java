package io.github.lukeeff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandExecute implements CommandExecutor {
		
	eShields plugin;
	
	public CommandExecute(eShields instance) {
		plugin = instance;
	}
	




	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg0, String[] args) {
		// TODO give player Overshields and other cool features
        return true;
	}
	
	//TODO reloadConfig logic.
	private void reloadConfig() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Coming soon...");
	}
	
}
