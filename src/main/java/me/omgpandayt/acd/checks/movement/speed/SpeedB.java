package me.omgpandayt.acd.checks.movement.speed;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.omgpandayt.acd.checks.Check;
import me.omgpandayt.acd.checks.PlayerData;
import me.omgpandayt.acd.checks.PlayerDataManager;
import me.omgpandayt.acd.util.BlockUtils;
import me.omgpandayt.acd.util.PlayerUtil;
import me.omgpandayt.acd.violation.Violations;

public class SpeedB extends Check implements Listener {

	public SpeedB() {
		super("SpeedB", false, 12);
	}
	
	@Override
	public void onMove(PlayerMoveEvent e) {

		Player p = e.getPlayer();

		double distX = Math.abs(e.getFrom().getX() - e.getTo().getX());
		double distZ = Math.abs(e.getFrom().getZ() - e.getTo().getZ());
		double dist = distX + distZ;

		PlayerData playerData = PlayerDataManager.getPlayer(p);
		
		if(playerData == null) return;
		
		double lastDist = 0;

		float friction = 0.91F;
		double shiftedLastDist = lastDist * friction;
		double equalness = dist - shiftedLastDist;
		double scaledEqualness = equalness;

		playerData.setLastLastOnGround(playerData.lastOnGround);
		playerData.setLastOnGround(playerData.isOnGround);
		playerData.setOnGround(PlayerUtil.isOnGround(p.getLocation()));
		playerData.setLastDist(dist);
		
		double tooFast = 0.51f;
		
        PotionEffect effect = p.getPotionEffect( PotionEffectType.SPEED );
        if ( effect != null )
        {
            tooFast += effect.getAmplifier() / (Math.PI * Math.PI);
        }
		
		for (Block b : BlockUtils.getBlocksBelow(p.getLocation().clone().add(0, -0.825, 0))) {
			if (BlockUtils.isIce(b)) {
				tooFast += 0.11f;
			} else if (b.getType() == Material.SLIME_BLOCK) {
				tooFast += 0.04f;
			}
		}
		
		boolean dontFlag = false;
		

		if (!playerData.lastOnGround && !playerData.isOnGround && !playerData.lastLastOnGround && scaledEqualness > tooFast && PlayerUtil.isValid(p) && !dontFlag && !p.isGliding() && PlayerUtil.getFallHeight(p) > 0.1) {
			double got = Math.floor(scaledEqualness * 100);
			flag(p, "Speed (B)", "(EXP " + ((Math.floor(tooFast * 100)) / 100) + ") (GOT " + (got / 100) + " (VL" + (Violations.getViolations(this, p) + 1) + ")");
			p.teleport(e.getFrom());
		}

	}

}
