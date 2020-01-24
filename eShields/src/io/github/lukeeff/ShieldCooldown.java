package io.github.lukeeff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldCooldown {
	// TODO Set all field objects to values in a config file.
	ShieldListener shieldRestore;

	
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    
    public static final long DEFAULT_COOLDOWN = 5;

    public void setCooldown(UUID p, long time){
        if(time < 1) {
            cooldowns.remove(p);
                 
        } else {
            cooldowns.put(p, (long) time);
        }
    }

    public long getCooldown(UUID p){
        return cooldowns.getOrDefault(p, (long) 0);
    }
}
