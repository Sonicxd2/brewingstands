package ru.sonicxd2.brewingstand;

import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Recipe {
    private Material material;
    private Predicate<PotionMeta> predicate;
    private Consumer<PotionMeta> applier;

    public Recipe(Material material, Predicate<PotionMeta> predicate, Consumer<PotionMeta> applier) {
        this.material = material;
        this.predicate = predicate;
        this.applier = applier;
    }

    public boolean isAvailable(PotionMeta meta) {
        return predicate.test(meta);
    }

    public void apply(PotionMeta meta) {
        applier.accept(meta);
    }

    public Material getMaterial() {
        return material;
    }
}
