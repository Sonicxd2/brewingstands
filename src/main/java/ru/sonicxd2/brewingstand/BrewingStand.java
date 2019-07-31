package ru.sonicxd2.brewingstand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import ru.sonicxd2.alterapi.inventory.ClickableInventory;
import ru.sonicxd2.alterapi.inventory.InventoryManager;
import ru.sonicxd2.alterapi.itemstack.ItemStacks;

public class BrewingStand extends ClickableInventory {
    private static final int TIMING = 400; //20 sec
    private static final int INGREDIENT_SLOT = 13;
    private static final int[] POTIONS_SLOTS = {38, 40, 42};
    private static final int[] ANIMATION_SLOTS = {20, 21, 22, 23, 24, 29, 31, 33};

    private static final ItemStack GREEN_GLASS = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
    private static final ItemStack RED_GLASS = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

    private static final PotionMeta EMPTY_META = (PotionMeta) new ItemStack(Material.POTION).getItemMeta();

    private int progress;
    private Recipe cachedRecipe;

    public BrewingStand(InventoryManager manager) {
        super(54, "Brewing stand", manager);

        for (int slot : ANIMATION_SLOTS) {
            getInventory().setItem(slot, GREEN_GLASS);
        }

        addListener(InventoryClickEvent.class, e -> {
            if ((e.getClickedInventory() != null) && (e.getClickedInventory().getType() == InventoryType.PLAYER)) {
                return;
            }
            e.setCancelled(e.getSlot() != INGREDIENT_SLOT && e.getSlot() != POTIONS_SLOTS[0]
                    && e.getSlot() != POTIONS_SLOTS[1] && e.getSlot() != POTIONS_SLOTS[2]);
        });
    }

    public void tick() {
        if (cachedRecipe == null) {
            ItemStack item = getInventory().getItem(INGREDIENT_SLOT);
            cachedRecipe = Recipes.recipe(item == null ? Material.AIR : item.getType());
            progress = 0;
            for (int slot : ANIMATION_SLOTS) {
                getInventory().setItem(slot, GREEN_GLASS);
            }
        } else {
            progress += Ticker.TICK_AMOUNT;
            if (progress == 400) {
                ItemStack ingredient = getInventory().getItem(INGREDIENT_SLOT);
                ingredient.setAmount(ingredient.getAmount() - 1);
                for (int slot : POTIONS_SLOTS) {
                    PotionMeta meta = createPotionMeta(getInventory().getItem(slot));
                    if (meta == null) continue;
                    if (cachedRecipe.isAvailable(meta)) {
                        cachedRecipe.apply(meta);
                    }
                    getInventory().getItem(slot).setItemMeta(meta);
                }
                cachedRecipe = null;

                ItemStack readyItem = ItemStacks.create(GREEN_GLASS).createMetaChangeSession()
                        .setDisplayName(ChatColor.GREEN + "Готово!")
                        .finish();
                for (int slot : ANIMATION_SLOTS) {
                    getInventory().setItem(slot, readyItem);
                }

            } else {
                boolean available = false;
                for (int slot : POTIONS_SLOTS) {
                    PotionMeta meta = createPotionMeta(getInventory().getItem(slot));
                    if (meta == null) continue;
                    available = cachedRecipe.isAvailable(meta);
                    if (available) break;
                }
                if (!available) {
                    cachedRecipe = null;
                    for (int slot : ANIMATION_SLOTS) {
                        getInventory().setItem(slot, GREEN_GLASS);
                    }
                }

                ItemStack readyItem = ItemStacks.create(RED_GLASS).createMetaChangeSession()
                        .setDisplayName(ChatColor.RED + "Зелье будет готово через " + (TIMING - progress) / 20 + " секунд")
                        .finish();
                for (int slot : ANIMATION_SLOTS) {
                    getInventory().setItem(slot, readyItem);
                }
            }
        }


    }

    private PotionMeta createPotionMeta(ItemStack it) {
        if ((it != null ) && (it.getItemMeta() instanceof PotionMeta)) {
            return (PotionMeta) it.getItemMeta();
        } else {
            return null;
        }
    }


}
