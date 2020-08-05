package dev.ratas.openeffecttest.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpiralEffect extends BukkitRunnable implements SubEffect {
    private final JavaPlugin plugin;
    private final Particle particle;
    private final double radius;
    private final double slope;
    private final double angleIncrement;
    private final double maxHeight;
    private final int particlesAtOnce;
    private final double waitBeforeFade;
    private final Axis axis;
    private final double axisOffset;

    private final int count = 1; // change if needbe
    private final double offsetX = 0;
    private final double offsetY = 0;
    private final double offsetZ = 0;
    private final double extra = 0;

    private Location startCenter;
    private double curAngle = 0;
    private SpiralState state = SpiralState.START;
    private long until;
    private List<Location> locs = new ArrayList<>();
    private Runnable whenDone = null;

    public SpiralEffect(JavaPlugin plugin, Particle particle, double radius, double slope, int perCircle,
            double maxHeight, double waitBeforeFade, int particlesAtOnce) {
        this(plugin, particle, radius, slope, perCircle, maxHeight, waitBeforeFade, particlesAtOnce, Axis.Y, 0.0D);
    }

    public SpiralEffect(JavaPlugin plugin, Particle particle, double radius, double slope, int perCircle,
            double maxHeight, double waitBeforeFade, int particlesAtOnce, Axis axis, double axisOffset) {
        if (slope == 0) {
            throw new IllegalArgumentException("This would go on forever!");
        }
        this.plugin = plugin;
        this.particle = particle;
        this.radius = radius;
        this.slope = slope;
        this.maxHeight = maxHeight;
        this.particlesAtOnce = particlesAtOnce;
        this.angleIncrement = Math.PI * 2 / perCircle;
        this.waitBeforeFade = waitBeforeFade;
        this.axis = axis;
        this.axisOffset = axisOffset;
    }

    public void start(Location loc) {
        this.startCenter = loc.clone();
        if (axisOffset > 0) {
            startCenter.add(axis == Axis.X ? axisOffset : 0.0D, axis == Axis.Y ? axisOffset : 0.0D,
                    axis == Axis.Z ? axisOffset : 0.0D);
        }
        runTaskTimer(plugin, 1L, 1L);
    }

    @Override
    public void run() {
        spawnAllParticles();
        if (state == SpiralState.START) {
            addParticle();
        } else if (state == SpiralState.WAIT && until > System.currentTimeMillis()) {
            state = SpiralState.FADE;
        } else if (state == SpiralState.FADE) {
            remove();
        }
    }

    private void spawnAllParticles() {
        for (Location cLoc : locs) {
            spawnParticleAtLocation(cLoc);
        }
    }

    private void spawnParticleAtLocation(Location cLoc) {
        cLoc.getWorld().spawnParticle(particle, cLoc, count, offsetX, offsetY, offsetZ, extra, null);
    }

    private void addParticle() {
        for (int i = 0; i < particlesAtOnce; i++) {
            double x;
            double y;
            double z;
            switch (axis) {
                case X:
                    x = curAngle * slope;
                    y = Math.sin(curAngle) * radius;
                    z = Math.cos(curAngle) * radius;
                    break;
                case Y:
                default:
                    x = Math.sin(curAngle) * radius;
                    y = curAngle * slope;
                    z = Math.cos(curAngle) * radius;
                    break;
                case Z:
                    x = Math.cos(curAngle) * radius;
                    y = Math.sin(curAngle) * radius;
                    z = curAngle * slope;
                    break;
            }
            if (hasReachedTop(x, y, z)) {
                state = SpiralState.WAIT;
                until = System.currentTimeMillis() + (long) (waitBeforeFade * 1000L);
            }
            Location cLoc = startCenter.clone().add(x, y, z);
            locs.add(cLoc);
            spawnParticleAtLocation(cLoc);
            curAngle += angleIncrement;
        }
    }

    private boolean hasReachedTop(double x, double y, double z) {
        switch (axis) {
            case X:
                return (slope > 0 && x > maxHeight) || (slope < 0 && x < -maxHeight);
            case Y:
            default:
                return (slope > 0 && y > maxHeight) || (slope < 0 && y < -maxHeight);
            case Z:
                return (slope > 0 && z > maxHeight) || (slope < 0 && z < -maxHeight);
        }
    }

    private void remove() {
        locs.clear();
        cancel();
        whenDone.run();
    }

    public enum SpiralState {
        START, WAIT, FADE
    }

    public static enum Axis {
        X, Y, Z
    }

    @Override
    public void play(Location location, Runnable whenDone) {
        this.whenDone = whenDone;
        start(location);
    }

}