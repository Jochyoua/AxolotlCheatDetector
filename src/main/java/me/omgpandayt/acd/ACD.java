package me.omgpandayt.acd;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.omgpandayt.acd.checks.*;

import me.omgpandayt.acd.checks.combat.criticals.*;
import me.omgpandayt.acd.checks.combat.reach.*;

import me.omgpandayt.acd.checks.movement.elytrafly.*;
import me.omgpandayt.acd.checks.movement.fly.*;
import me.omgpandayt.acd.checks.movement.speed.*;

import me.omgpandayt.acd.checks.player.groundspoof.*;
import me.omgpandayt.acd.checks.player.invmove.*;
import me.omgpandayt.acd.checks.player.jesus.*;
import me.omgpandayt.acd.checks.player.noslowdown.*;

import me.omgpandayt.acd.checks.world.impossibleactions.*;
import me.omgpandayt.acd.checks.world.fastplace.*;

import me.omgpandayt.acd.listeners.RegisterListeners;

import net.md_5.bungee.api.ChatColor;

public class ACD extends JavaPlugin {
	
	private static ACD instance; // The plugins instance (Used for finding things in the plugin not static)
	
	public static String prefix = "&7[&cACD&7]";

	public String 
	
					startupMessage = "&cPreventing your baby axolotls from cheating!",
					turnoffMessage = "&cNo longer preventing your baby axolotls from cheating!";
    
	public void onEnable() {
		log(startupMessage);
		
		instance = this;  // Creating our instance
		
		RegisterListeners.register(instance);
		
		//PacketEvents.get().registerListener(new ACD());
		
		new SpeedA();
		new SpeedB();
		
		new GroundSpoofA();
		
		new FlyA();
		new FlyB();
		new FlyC();
		new FlyD();
		
		new ReachA();
		
		new CriticalsA();
		
		new NoSlowdownA();
		
		new JesusA();
		new JesusB();
		new JesusC();
		
		new ElytraFlyA();
		new ElytraFlyB();
		
		new InvMoveA();
		
		new ImpossibleActionsA();
		
		new FastPlaceA();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			PlayerDataManager.createPlayer(p);
		}
		
        Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                	PlayerData playerData = PlayerDataManager.getPlayer(player);
                    playerData.addTicksSinceHit();
                    playerData.ticksLived++;
                    
                    if(playerData.ticksLived % 20 == 0) {
                    	if(playerData.flyALimiter > 0) {
                    		playerData.flyALimiter--;
                    	}
                    	if(playerData.flyBLimiter > 0) {
                    		playerData.flyBLimiter--;
                    	}
                    	if(playerData.flyCLimiter > 0) {
                    		playerData.flyCLimiter--;
                    	}
                       	if(playerData.flyDLimiter > 0) {
                    		playerData.flyDLimiter--;
                    	}
                    	if(playerData.jesusBLimiter > 0) {
                    		playerData.jesusBLimiter--;
                    	}
                    	if(playerData.jesusCLimiter > 0) {
                    		playerData.jesusCLimiter--;
                    	}
                	
                    	if(playerData.impactALimiter > 0) {
                    		playerData.impactALimiter--;
                    	}
                    } else if (playerData.ticksLived % 2 == 0) {
                    	if(playerData.placedBlocks > 0) {
                    		playerData.placedBlocks--;
                    	}
                    }
                    
                    for(Object c : CheckManager.getRegisteredChecks()) {
                    	((Check)c).onTick(player);
                    }
                }
            }
            
        }, 1, 0);
		
	}
	
	public void onDisable() {
		log(turnoffMessage);
		
		instance = null;
	}
	
	public void log(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + message));
	}
	
	public static ACD getInstance() {
		return instance;  // Giving the instance
	}

	public static void logPlayers(Object b) {
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			if(p.hasPermission("acd.notify")) {
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + b));
				
			}
			
		}
		
	}
	
}
