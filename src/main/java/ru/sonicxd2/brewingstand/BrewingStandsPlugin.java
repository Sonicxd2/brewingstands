package ru.sonicxd2.brewingstand;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sonicxd2.alterapi.inventory.InventoryManager;
import ru.sonicxd2.alterapi.inventory.InventoryManagerImpl;

public class BrewingStandsPlugin extends JavaPlugin {
    private Ticker ticker;

    @Override
    public void onEnable() {
        this.ticker = new Ticker(this);
        InventoryManager manager = new InventoryManagerImpl(this);
        getCommand("bs").setExecutor((commandSender, command, s, strings) -> {
            Player player = (Player) commandSender;
            BrewingStand stand = new BrewingStand(manager);
            ticker.addStand(stand);
            player.openInventory(stand.getInventory());
            return true;
        });
        super.onEnable();
    }
}
