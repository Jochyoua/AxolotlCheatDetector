package me.omgpandayt.acd.checks.combat.criticals;

import me.omgpandayt.acd.checks.Check;
import me.omgpandayt.acd.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

public class CriticalsA extends Check {

    public CriticalsA() {
        super("CriticalsA", false);
    }

    @Override
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || e.getCause() != DamageCause.ENTITY_ATTACK)
            return;
        Player player = (Player) e.getDamager();
        if (isCritical(player)
                || player.getLocation().getY() % 0.5 == 0
                && player.getLocation().clone().subtract(0, 1.0, 0).getBlock().getType().isSolid()) {
            cancelDamage(e);
            flag(player, "Criticals (A)", "");
        }
    }

    private boolean isCritical(Player player) {
        return player.getFallDistance() > 0.0f && !PlayerUtil.isOnGround(player.getLocation()) && !player.isInsideVehicle()
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS)
                && player.getLocation().clone().add(0, -1, 0).getBlock().getType() != Material.WATER
                && player.getLocation().clone().add(0, -1, 0).getBlock().getType() != Material.LAVA
                && player.getEyeLocation().getBlock().getType() != Material.LADDER;
    }

}
