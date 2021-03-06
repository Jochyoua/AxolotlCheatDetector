package me.omgpandayt.acd.checks.movement.speed;

import me.omgpandayt.acd.checks.Check;
import me.omgpandayt.acd.checks.PlayerData;
import me.omgpandayt.acd.checks.PlayerDataManager;
import me.omgpandayt.acd.util.BlockUtils;
import me.omgpandayt.acd.util.PlayerUtil;
import me.omgpandayt.acd.violation.Violations;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedA extends Check implements Listener {

    private static final String PATH = "checks.speed.a.";

    public SpeedA() {
        super("SpeedA", false);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (e.getTo() == null) {
            return;
        }
        double distX = Math.abs(e.getFrom().getX() - e.getTo().getX());
        double distZ = Math.abs(e.getFrom().getZ() - e.getTo().getZ());

        double maxXZMove = config.getDouble(PATH + "maximum-speed");

        for (Block b : BlockUtils.getBlocksBelow(p.getLocation())) {
            if (b != null && BlockUtils.isIce(b)) {
                maxXZMove += config.getDouble(PATH + "ice-increase");
            }
        }

        PotionEffect effect = p.getPotionEffect(PotionEffectType.SPEED);
        if (effect != null) {
            maxXZMove += effect.getAmplifier() / (Math.PI * Math.PI);
        }

        if (distZ < distX / 1.1 && Math.abs(distZ - distX) > 0.2f) {
            maxXZMove -= (distX / 2f);
            maxXZMove += 0.15f;
        } else if (distX < distZ / 1.1 && Math.abs(distX - distZ) > 0.2f) {
            maxXZMove -= (distZ / 2f);
            maxXZMove += 0.15f;
        }

        double distance = Math.floor((distX + distZ) * 100);
        double maxDistance = Math.floor(maxXZMove * 100);

        boolean dontFlag = false;

        for (Block b : BlockUtils.getBlocksBelow(p.getLocation().clone().add(0, 1, 0))) {
            if (b.getType().isSolid()) {
                dontFlag = true;
                break;
            }
        }
        if (!dontFlag) {
            for (Block b : BlockUtils.getBlocksBelow(p.getLocation())) {
                if (b.getType().isSolid()) {
                    dontFlag = true;
                    break;
                }
            }
        }

        PlayerData playerData = PlayerDataManager.getPlayer(p);
        if (playerData == null) return;

        if (distance > maxDistance && PlayerUtil.isValid(p) && !dontFlag && !p.isGliding() && !playerData.isLastPacketNearBoat()) {
            flag(p, "Speed (A)", "(MOVE " + (distance / 100) + " > " + (maxDistance / 100) + ") (VL" + (Violations.getViolations(this, p) + 1) + ")");
            lagBack(e);
        }

    }

}
