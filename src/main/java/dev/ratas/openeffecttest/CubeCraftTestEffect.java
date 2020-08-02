package dev.ratas.openeffecttest;

import org.bukkit.plugin.java.JavaPlugin;

public class CubeCraftTestEffect extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EffectActivator(this), this);
    }
    
}