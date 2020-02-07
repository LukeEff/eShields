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
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] inputName) {
		if (inputName.length > 0) {
			if (inputName[0].equalsIgnoreCase("reload")) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Reloading config...");
				plugin.reloadTest();
				ShieldListener.reload();
				ShieldCooldown.reload();
				ShieldSounds.reload();
				//plugin.onEnable(); 
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "eShields config successfully reloaded.");
				return true;
			} else {
				return true;
			}
		}
        return true;
	}
	
	
	
}

