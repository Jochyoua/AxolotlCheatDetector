package me.omgpandayt.acd.checks.player.jesus;

import me.omgpandayt.acd.checks.Check;
import me.omgpandayt.acd.checks.PlayerData;
import me.omgpandayt.acd.checks.PlayerDataManager;
import me.omgpandayt.acd.util.BlockUtils;
import me.omgpandayt.acd.util.PlayerUtil;
import me.omgpandayt.acd.violation.Violations;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class JesusB extends Check {

    private static final String PATH = "checks.jesus.b.";

    public JesusB() {
        super("JesusB", false);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        boolean dontFlag = false;

        if (e.getTo() == null) {
            return;
        }

        for (Block b : BlockUtils.getBlocksBelow(e.getTo())) {
            if (!b.isLiquid()) dontFlag = true;
        }
        if (!dontFlag) {
            for (Block b : BlockUtils.getBlocksBelow(e.getFrom())) {
                if (!b.isLiquid()) dontFlag = true;
            }
        }

        if (p.getVelocity().getY() > config.getDouble(PATH + "max-ascend") &&
                PlayerUtil.isAboveLiquids(p.getLocation()) &&
                p.getLocation().getBlock().getType() == Material.AIR
                && !dontFlag) {
            PlayerData playerData = PlayerDataManager.getPlayer(p);

            if (playerData == null) return;

            playerData.setJesusBLimiter(playerData.getJesusBLimiter() + 1);

            if (playerData.getJesusBLimiter() >= 3) {
                playerData.setJesusBLimiter(0);
                flag(p, "Jesus (B)", "(VL" + (Violations.getViolations(this, p) + 1) + ")");
                if (config.getBoolean("main.cancel-event"))
                    p.teleport(e.getFrom().clone().add(0, 0.2, 0));
            }
        }

    }

}
