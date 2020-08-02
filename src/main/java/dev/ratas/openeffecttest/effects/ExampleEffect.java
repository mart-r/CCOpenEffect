package dev.ratas.openeffecttest.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.openeffecttest.effects.SpiralEffect.Axis;

public class ExampleEffect implements Effect {
    private static final Particle PARTICLE = Particle.FLAME;
    private static final Particle PARTICLE_2 = Particle.CRIT_MAGIC;
    private static final double RADIUS = 0.7D;
    private static final double SLOPE = 0.05D;
    private static final int PER_CIRCLE = 30;
    private static final double MAX_HEIGHT = 1.2D;
    private static final int WAIT_TIME = 80;
    private static final int PARTICLES_AT_ONCE = 5;
    private static final double AXIS_OFFSET = 0.5D;
    private final List<List<SubEffect>> effectStages = new ArrayList<>();
    
    // TODO - better way of scheduling sub-effects.
    // namely, each subeffect should have its own specified time
    // at which they should start

    public ExampleEffect(JavaPlugin plugin) {
        List<SubEffect> stage1 = new ArrayList<>();
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.X, AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Y, AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Z, AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, -SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.X, -AXIS_OFFSET));
        stage1.add(new SpiralEffect(plugin, PARTICLE, RADIUS, -SLOPE, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Z, -AXIS_OFFSET));
        effectStages.add(stage1);
        List<SubEffect> stage2 = new ArrayList<>();
        stage2.add(new SpiralEffect(plugin, PARTICLE_2, RADIUS * 2, SLOPE * 2, PER_CIRCLE, MAX_HEIGHT, WAIT_TIME,
                PARTICLES_AT_ONCE, Axis.Y, AXIS_OFFSET));
        effectStages.add(stage2);
    }

    @Override
    public void play(Location location) {
        if (effectStages.isEmpty()) {
            throw new IllegalStateException("No subeffects defined");
        }
        playSubIfPresent(0, location);
    }

    protected void playSubIfPresent(int index, Location location) {
        if (index >= effectStages.size()) {
            return;
        }
        List<SubEffect> subs = effectStages.get(index);
        for (SubEffect sub : subs) {
            sub.play(location, () -> {
                subs.remove(sub);
                if (subs.isEmpty()) {
                    playSubIfPresent(index + 1, location);
                }
            });
        }
    }

}