package dev.ratas.openeffecttest.effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class FallingHeadEffect implements SubEffect {
    private final JavaPlugin plugin;
    private final float anglePer;
    private final int repeats;
    private final double yOffset;
    private final double fallPer;
    private int repeated = 0;
    private Runnable whenDone;

    // public FallingHeadEffect(JavaPlugin plugin, float rotate, int repeats, double yOffset) {
    //     this(plugin, rotate, repeats, yOffset);
    // }

    public FallingHeadEffect(JavaPlugin plugin, float anglePer, double fallPer, double fallingDistance, double yOffset) {
        this.plugin = plugin;
        this.anglePer = anglePer;
        this.fallPer = fallPer;
        this.repeats = (int) (fallingDistance / fallPer);
        this.yOffset = yOffset;
    }

    @Override
    public void play(Location location) {
        location = location.clone().add(0, yOffset, 0);
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class, (e) -> e.setVisible(false));
        // FallingBlock block = location.getWorld().spawnFallingBlock(location, data);
        // block.setDropItem(false);
        as.setVisible(false);
        as.setGravity(false);
        as.setInvulnerable(true);
        as.setBasePlate(false);
        as.getEquipment().setHelmet(new ItemStack(Material.PLAYER_HEAD));
        // block.
        plugin.getServer().getScheduler().runTaskTimer(plugin, (t) -> {
            if (repeated++ > repeats) {
                t.cancel();
                as.remove();
                if (whenDone != null) {
                    whenDone.run();
                }
                return;
            }
            Location tele = as.getLocation();
            tele.add(0, -fallPer, 0);
            as.teleport(tele);
            as.setRotation(anglePer * repeated, 0.0F);
        }, 1L, 1L);
    }

	@Override
	public void whenDone(Runnable runnable) {
		whenDone = runnable;
	}
    
}