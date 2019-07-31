package ru.sonicxd2.brewingstand;

import org.bukkit.Material;
import org.bukkit.potion.PotionData;

import java.util.EnumMap;
import java.util.Map;

public class Recipes {
    private static final Map<Material, Recipe> recipes = new EnumMap<>(Material.class);

    static {
        recipes.put(Material.REDSTONE, new Recipe(Material.REDSTONE, potionMeta -> {
            return !potionMeta.getBasePotionData().isExtended() && potionMeta.getBasePotionData().getType().isExtendable();
        }, potionMeta -> {
            PotionData data = new PotionData(potionMeta.getBasePotionData().getType(), true, false);
            potionMeta.setBasePotionData(data);
        }));
    }

    public static Recipe recipe(Material material) {
        return recipes.get(material);
    }


    private Recipes() {
        throw new AssertionError("You cant allocate this class");
    }
}
