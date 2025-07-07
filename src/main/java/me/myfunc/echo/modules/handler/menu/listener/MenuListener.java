package me.myfunc.echo.modules.handler.menu.listener;

import me.myfunc.echo.modules.handler.Module;
import me.myfunc.echo.modules.handler.menu.Menu;
import me.myfunc.echo.modules.handler.menu.MenuManager;
import me.myfunc.echo.modules.handler.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * RankManager | MenuListener
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class MenuListener extends Module<MenuManager> {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getClickedInventory() == null) return;

        Menu menu = getManager().getMenus().get(player.getUniqueId());

        if (menu != null) {
            if (!menu.isAllowInteract()) {
                e.setCancelled(true);
            }

            menu.onClick(e);

            if (e.getClickedInventory() != player.getInventory()) {
                Button button = menu.getButtons().get(e.getSlot());
                if (button != null) button.onClick(e);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Menu menu = getManager().getMenus().get(player.getUniqueId());

        if (menu != null) {
            menu.onClose();
            menu.destroy();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Menu menu = getManager().getMenus().remove(player.getUniqueId());

        if (menu != null) {
            menu.onClose();
            menu.destroy();
        }
    }

    public MenuListener(MenuManager manager) {
        super(manager);
    }
}