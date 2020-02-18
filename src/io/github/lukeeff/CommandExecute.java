package io.github.lukeeff;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandExecute implements CommandExecutor {
		
	eShields plugin;

	public CommandExecute(eShields instance) {
		plugin = instance;

	}
	




	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] inputName) {
		Player p = (Player) arg0;
		if (p.hasPermission("ecategory")) {
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
			} else if(inputName[0].equalsIgnoreCase("overshield")) {
				
				BossBar overShield = Bukkit.createBossBar(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Overshield", BarColor.PINK, BarStyle.SOLID);
				overShield.addPlayer(p);
				overShield.setVisible(true);
				
				
				return true;
			} else {
				
				return true;
			}
			}
		
        return true;
        
	}
		return true;
	}
	
}

