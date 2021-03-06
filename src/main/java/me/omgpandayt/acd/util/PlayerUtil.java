package me.omgpandayt.acd.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerUtil {

    private PlayerUtil() {
        throw new UnsupportedOperationException("Cannot instantiate utility class.");
    }

    public static boolean isOnGround(Location loc) {
        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (loc.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getFallHeight(Player p) {
        int yHeight = (int) Math.floor(p.getPlayer().getLocation().getY());
        Location loc = p.getLocation().clone();
        loc.setY(yHeight);
        int fallHeight = 0;
        while (loc.getBlock().getType() == Material.AIR) {
            yHeight--;
            loc.setY(yHeight);
            fallHeight++;
        }
        return fallHeight;
    }

    public static boolean isValid(Player p) {
        return !p.isFlying() && (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) && !p.isDead();
    }

    public static boolean isUsingItem(Player p) {
        return p.getItemInUse() != null;
    }

    public static boolean isAboveLiquids(Location location) {
        for (Block b : BlockUtils.getBlocksBelow(location)) {
            if (b.getType() == Material.WATER || b.getType() == Material.LAVA) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAboveSlime(Location location) {
        for (Block b : BlockUtils.getBlocksBelow(location)) {
            if (b.getType() == Material.SLIME_BLOCK) {
                return true;
            } else {
                for (int i = 0; i < 10; i++) {
                    if (b.getLocation().clone().add(0, -i, 0).getBlock().getType() == Material.SLIME_BLOCK) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @deprecated Goes down till it finds slime
     */

    // W
    @Deprecated
    public static boolean isAboveSlimeUnsafe(Location location) {
        for (Block b : BlockUtils.getBlocksBelow(location)) {
            if (b.getType() == Material.SLIME_BLOCK) {
                return true;
            } else {
                for (int i = 0; i < location.getY(); i++) {
                    if (b.getLocation().clone().add(0, -i, 0).getBlock().getType() == Material.SLIME_BLOCK) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean aboveAreAir(Player p) {
        for (Block b : BlockUtils.getBlocksBelow(p.getLocation().clone().add(0, 3, 0))) {
            if (b.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }

}
