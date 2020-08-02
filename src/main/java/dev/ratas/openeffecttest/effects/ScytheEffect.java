package dev.ratas.openeffecttest.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ScytheEffect implements SubEffect {
    // offsets
    private static final double OX = 0.0D;
    private static final double OY = 0.0D;
    private static final double OZ = 0.0D;
    private static final int COUNT = 1;
    private static final long WAIT = 1L;
    // LONG lasting
    // private static final Particle HANDLE = Particle.DRIP_LAVA;
    // private static final Particle HEAD = Particle.DRIP_WATER;
    // QUICK
    private static final Particle HANDLE = Particle.WATER_DROP;
    private static final Particle HEAD = Particle.CURRENT_DOWN;
    private static final Vector VERTICAL = new Vector(0, 1, 0);
    private final JavaPlugin plugin;
    private final int particlesInHandle;
    private final int particlesInHead;
    private final Vector direction;
    private final double handleIncrement;
    private final double headAngleIncrement;
    private Runnable whenDone;

    public ScytheEffect(JavaPlugin plugin, int particlesInHandle, double headLength,
            int particlesInHead, Vector handleSpan) {
        this.plugin = plugin;
        this.particlesInHandle = particlesInHandle;
        this.particlesInHead = particlesInHead;
        handleSpan = handleSpan.clone();
        double handleLength = handleSpan.length();
        this.handleIncrement = handleLength / particlesInHandle;
        this.direction = handleSpan.normalize();
        // maxAngle = headLength / radius radius = handle length
        this.headAngleIncrement = headLength / handleLength / particlesInHead;
    }

    @Override
    public void play(Location location) {
        Location cur = location.clone();
        Vector toAdd = direction.clone().multiply(handleIncrement);
        for (int nr = 0; nr < particlesInHandle; nr++) {
            spawnHandleParticle(cur);
            cur.add(toAdd);
        }
        Vector vec = cur.clone().subtract(location).toVector();
        for (int nr = 0; nr < particlesInHead; nr++) {
            spawnHeadParticle(cur);
            vec.rotateAroundAxis(VERTICAL, headAngleIncrement);
            cur = location.clone().add(vec);
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> done(), WAIT);
    }

    private void spawnHandleParticle(Location loc) {
        loc.getWorld().spawnParticle(HANDLE, loc, COUNT, OX, OY, OZ, null);
    }

    private void spawnHeadParticle(Location loc) {
        loc.getWorld().spawnParticle(HEAD, loc, COUNT, OX, OY, OZ, null);
    }

    private void done() {
        if (whenDone != null) {
            whenDone.run();
        }
    }

    @Override
    public void whenDone(Runnable runnable) {
        this.whenDone = runnable;
    }

}