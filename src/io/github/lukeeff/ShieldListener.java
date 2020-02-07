package io.github.lukeeff;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ShieldListener implements Listener {

	static eShields plugin;
	static ShieldCooldown shieldCooldown;
	static ShieldSounds shieldSounds;
	static public BarColor fracturedShieldColor; // Color when shield health hits 0.
	static public BarColor healthyShieldColor; // Color when shield begins to regenerate.
	static public BarStyle shieldStyle; // Style for shield.
	static public double shieldRegenPerTick; // Regenerate shield %/tick.
	static public String shieldName; // Name above BossBar that player sees.
	static public double shieldHealth; // Gives the shield a value of 1/2 heart per health.
	@SuppressWarnings("rawtypes")
	static HashMap<UUID, HashMap> data; // Hashmap holding each player's BossBar.

	public ShieldListener(eShields instance) {
		plugin = instance;
		setVariables();
	}
	
	static void setVariables() {
		shieldSounds = plugin.sounds;
		fracturedShieldColor = configSectionGetBarColor(plugin.fracturedShieldColor);
		healthyShieldColor = configSectionGetBarColor(plugin.healthyShieldColor);
		shieldCooldown = plugin.cooldown;
		shieldStyle = configSectionGetBarStyle(plugin.shieldStyle);
		shieldRegenPerTick = (configSectionGetDouble(plugin.shieldRegenPerTick) / 2000);
		shieldName = configSectionGetString(plugin.shieldName);
		shieldHealth = configSectionGetDouble(plugin.shieldHealth);
		data = plugin.data;
	}
	
	static void reload() {
		setVariables();
		for (Player player : Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			BossBar playerShield = (BossBar) data.get(uuid).get("playerShield");
			updateShield(playerShield);
		}
		
	}

	private static void updateShield(BossBar playerShield) {
		if(playerShield.getProgress() <= 0.01) {
			playerShield.setColor(fracturedShieldColor);
		} else {
			playerShield.setColor(healthyShieldColor);
		}
		playerShield.setStyle(shieldStyle);
	}
	// TODO Fix very rare bug where damage doesn't trigger health regen for some
	// reason.
	// TODO Give a way to spectate battles and see shield status of any player.
	// TODO Keep functionality on reload.
	// TODO Put long comments over line not to side.
	// TODO Declare in outter wrapper, assign them in construcuter.
	// TODO Brainstorm ideas for player-exclusive options.

	/*	
	 * Defines player object and BossBar for map key and value pair. BossBar is what
	 * I used for the energy shield.
	 */
	
	
	
	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		BossBar playerShield = Bukkit.createBossBar(shieldName, healthyShieldColor, shieldStyle);
		storeData(player, playerShield);
		initializeShield(player, playerShield);

	}

	
	
	/*
	 * This method is for future configuration that I haven't added yet.
	 */
	private void storeData(Player player, BossBar shield) {
		HashMap<String, BossBar> contents = new HashMap<String, BossBar>();
		contents.put("playerShield", shield);
		data.put(player.getUniqueId(), contents);
	}

	/*
	 * Initializes the shield and makes it visible to our player.
	 */
	private void initializeShield(Player player, BossBar shield) {
		shield.addPlayer(player);
		shield.setVisible(true);
	}

	/*
	 * Defines player object and the damage we will add to the shield.
	 */
	@EventHandler
	private void playerDamaged(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			double shieldDamage = (event.getDamage() / 100); // Divide by 100 so player takes miniscule damage. Can't
																// set to 0.
			Player player = (Player) event.getEntity();
			shieldProcessing(player, shieldDamage, event);

		}
	}

	/*
	 * Assigns a shield regen cooldown for the player. Checks if the shield health
	 * is greater than 0.
	 */
	private void shieldProcessing(Player player, double shieldDamage, EntityDamageEvent event) {
		BossBar playerShield = getPlayerShield(player);
		setShieldCooldown(player);
		beginShieldRestoreTimer(player, playerShield);
		if (getShieldProgress(playerShield) > 0d) {
			try {
				setShieldProgress(playerShield, getShieldProgress(playerShield) - shieldDamage * (shieldHealth / 4)); // Divide    20 5 5
																														// by
																														// 4
																														// so
																														// shieldHealth
																														// works
																														// correctly.
				event.setDamage(shieldDamage); // Sets player damage to 1% if shield is active.
			} catch (Exception negativeShieldHealth) {
				shieldFracture(player, shieldDamage, playerShield, event);
			}
		}
	}

	/*
	 * Store cooldown in HashMap.
	 */
	private void setShieldCooldown(Player player) {
		shieldCooldown.setCooldown(player.getUniqueId(), System.currentTimeMillis());
	}

	/*
	 * Changes player's BossBar color. Sets player damage to leftover damage of
	 * shield.
	 */
	private void shieldFracture(Player player, double shieldDamage, BossBar playerShield, EntityDamageEvent event) {
		playerShield.setColor(fracturedShieldColor);
		event.setDamage(((event.getDamage() - (getShieldProgress(playerShield) * shieldHealth))));
		setShieldProgress(playerShield, 0);
		playShieldFractureSound(player);
	}

	/*
	 * Restore shield in DEFAULT_COOLDOWN seconds.
	 */
	private void beginShieldRestoreTimer(Player player, BossBar playerShield) {
		Bukkit.getScheduler().runTaskLater(plugin, () -> restoreShield(player, playerShield),
				toTicks(io.github.lukeeff.ShieldCooldown.DEFAULT_COOLDOWN) + 1); // Add one tick or canRegen will always
																					// say false.
	}

	/*
	 * Begins shield restoration if player is allowed to regen.
	 */
	private void restoreShield(Player player, BossBar playerShield) {

		long timeLeft = getTimeLeft(player);
		if (canRegen(timeLeft)) {
			playShieldRegenSound(player);
			beginShieldRestore(player, playerShield);
		}
	}

	/*
	 * Repeat task until shield is full or setShieldCooldown is called. Task
	 * restores shield at shieldRegenPerTick per tick. Allows for a smooth and
	 * cancelable shield regen.
	 */
	private void beginShieldRestore(Player player, BossBar playerShield) {
		playerShield.setColor(healthyShieldColor); //Throws NPE...
		new BukkitRunnable() {

			@Override
			public void run() {

				long timeLeft = getTimeLeft(player);

				if (!canRegen(timeLeft)) {
					this.cancel();
				} else if (getShieldProgress(playerShield) + shieldRegenPerTick >= 1) {
					setShieldProgress(playerShield, 1);
					this.cancel();
				} else {
					setShieldProgress(playerShield, getShieldProgress(playerShield) + shieldRegenPerTick);
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}

	/*
	 * Checks if cooldown has expired.
	 */
	private boolean canRegen(long timeLeft) {
		return TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= io.github.lukeeff.ShieldCooldown.DEFAULT_COOLDOWN;
	}

	/*
	 * Returns player's BossBar progress.
	 */
	private double getShieldProgress(BossBar playerShield) {
		return playerShield.getProgress(); //Throws NPE... 
	}

	/*
	 * Sets player's BossBar progress
	 */
	private void setShieldProgress(BossBar playerShield, double progress) {
		playerShield.setProgress(progress);
	}

	/*
	 * Plays sound for shield fracturing.
	 */
	private void playShieldFractureSound(Player player) {
		shieldSounds.shieldFracture(player);
	}

	/*
	 * Plays sound for shield regeneration.
	 */
	private void playShieldRegenSound(Player player) {
		shieldSounds.shieldRegen(player);
	}

	/*
	 * Returns player's BossBar
	 */
	private BossBar getPlayerShield(Player player) {
		return (BossBar) data.get(player.getUniqueId()).get("playerShield");
	}

	/*
	 * Returns the time left.
	 */
	private long getTimeLeft(Player player) {
		return System.currentTimeMillis() - shieldCooldown.getCooldown(player.getUniqueId());
	}

	/*
	 * Converts seconds to ticks and returns.
	 */
	private long toTicks(long seconds) {
		return seconds * 20;
	}

	/*
	 * Returns configuration section name for objects.
	 */
	private static String configSectionName() {
		return plugin.getShieldListenerSectionName();
	}

	/*
	 * Returns config string value from shieldListenerSection specified in
	 * parameter.
	 */
	private static String configSectionGetString(Object[] configName) {
		return plugin.getConfig().getConfigurationSection(configSectionName()).getString((String) configName[0]);
	}

	/*
	 * Returns config double value from shieldListenerSection specified in
	 * parameter.
	 */
	private static double configSectionGetDouble(Object[] configName) {
		return plugin.getConfig().getConfigurationSection(configSectionName()).getDouble((String) configName[0]);
	}

	/*
	 * Returns config BarStyle value from shieldListenerSection specified in
	 * parameter.
	 */
	private static BarStyle configSectionGetBarStyle(Object[] configName) {
		return plugin.shieldStyleMap.get(plugin.getConfig().getConfigurationSection(configSectionName())
				.getString((String) configName[0]).toUpperCase());
	}

	/*
	 * Returns config BarColor value from shieldListenerSection specified in
	 * parameter.
	 */
	private static BarColor configSectionGetBarColor(Object[] configName) {
		// return
		// plugin.getConfig().getConfigurationSection(configSectionName()).getObject((String)
		// configName[0],
		// BarColor.class);
		return plugin.shieldColorMap.get(plugin.getConfig().getConfigurationSection(configSectionName())
				.getString((String) configName[0]).toUpperCase());
	}

}