package ru.sonicxd2.brewingstand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Ticker {
    public static final int TICK_AMOUNT = 10;
    private List<BrewingStand> stands;

    public Ticker(JavaPlugin plugin) {
        this.stands = new ArrayList<>();
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (BrewingStand stand : stands) {
                stand.tick();
            }
        }, TICK_AMOUNT, TICK_AMOUNT);
    }

    public void addStand(BrewingStand stand) {
        this.stands.add(stand);
    }

}
