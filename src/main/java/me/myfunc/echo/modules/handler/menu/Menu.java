package me.myfunc.echo.modules.handler.menu;

import lombok.Getter;
import lombok.Setter;
import me.myfunc.echo.modules.handler.Module;
import me.myfunc.echo.modules.handler.menu.button.Button;
import me.myfunc.echo.utilities.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

/*
 * RankManager | Menu
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
@Setter
@Getter
public abstract class Menu extends Module<MenuManager> {

    protected Map<Integer, Button> buttons;
    protected Player player;
    protected Inventory inventory;
    protected BukkitTask updater;

    protected ItemStack filler;
    protected String title;
    protected boolean fillEnabled;
    protected boolean allowInteract;
    protected int size;

    public Menu(MenuManager manager) {
        super(manager);
    }

    public Menu(MenuManager manager, Player player, String title, int size, boolean update) {
        super(manager);
        this.player = player;
        this.inventory = Bukkit.createInventory(null, size, CC.t(title));
        this.updater = (update ? Bukkit.getScheduler().runTaskTimer(getMain(), this::update, 0L, 10L) : null);
        this.fillEnabled = false;
        this.allowInteract = false;
        this.title = title;
        this.size = size;
    }

    public Menu(MenuManager manager, Player player, String title, boolean update, Inventory inventory) {
        super(manager);
        this.player = player;
        this.inventory = (inventory instanceof DoubleChestInventory ?
                Bukkit.createInventory(null, 54, CC.t(title)) :
                Bukkit.createInventory(null, inventory.getType(), CC.t(title)));
        this.updater = (update ? Bukkit.getScheduler().runTaskTimer(getMain(), this::update, 0L, 10L) : null);
        this.fillEnabled = false;
        this.allowInteract = false;
        this.title = title;
        this.size = inventory.getSize();
    }

    public void open() {
        this.buttons = getButtons(player);

        for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
            int slot = entry.getKey();
            if (slot >= 0 && slot < inventory.getSize()) {
                inventory.setItem(slot, entry.getValue().getItemStack());
            }
        }

        if (fillEnabled) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() == Material.AIR) {
                    inventory.setItem(i, filler);
                }
            }
        }

        player.openInventory(inventory);
        getManager().getMenus().put(player.getUniqueId(), this);
    }

    public void update() {
        if (!player.isOnline()) {
            destroy();
            return;
        }

        Map<Integer, Button> buttons = (this.buttons = getButtons(player));

        for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
            int slot = entry.getKey();
            if (slot >= 0 && slot < inventory.getSize()) {
                inventory.setItem(slot, entry.getValue().getItemStack());
            }
        }

        if (fillEnabled) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() == Material.AIR) {
                    inventory.setItem(i, filler);
                }
            }
        }
    }

    public void destroy() {
        buttons.clear();
        inventory.clear();
        getManager().getMenus().remove(player.getUniqueId());
        if (updater != null) {
            updater.cancel();
        }
    }

    public void onClick(InventoryClickEvent e) {
    }

    public void onClose() {
    }

    public Map<Integer, Button> getButtons(Player player) {
        throw new IllegalArgumentException("Menu not set up properly.");
    }
}