package me.myfunc.echo.modules.menu;

import me.myfunc.echo.modules.handler.Manager;
import me.myfunc.echo.modules.handler.menu.Menu;
import me.myfunc.echo.modules.handler.menu.MenuManager;
import me.myfunc.echo.modules.handler.menu.button.Button;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.LuckPermsUtil;
import me.myfunc.echo.utilities.extra.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * RankManager | MainMenu
 *
 * @author 7Str1kes
 * @date 08/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class MainMenu extends Menu {
    private final LuckPermsUtil lpUtil;

    public MainMenu(MenuManager menuManager, Player viewer) {
        super(menuManager, viewer,
                Manager.getMenusFile().getString("MAIN.TITLE"),
                Manager.getMenusFile().getInt("MAIN.SIZE"),
                false);
        this.lpUtil = getMain().getLuckPermsUtil();
        this.setFillEnabled(true);
        this.setFiller(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&7").toItemStack());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // RANKS BUTTON
        buttons.put(
                getSlot("MAIN.ITEMS.RANKS_ITEM.SLOT"),
                createButton("MAIN.ITEMS.RANKS_ITEM", () -> new RanksMenu(getManager(), player).open())
        );

        // RELOAD BUTTON
        buttons.put(
                getSlot("MAIN.ITEMS.RELOAD_ITEM.SLOT"),
                createButton("MAIN.ITEMS.RELOAD_ITEM", () -> {
                    Manager.getConfigFile().reload();
                    Manager.getLanguageFile().reload();
                    Manager.getMenusFile().reload();
                })
        );

        return buttons;
    }

    private int getSlot(String path) {
        return Manager.getMenusFile().getInt(path);
    }

    private Button createButton(String basePath, Runnable onClick) {
        String name = CC.t(Manager.getMenusFile().getString(basePath + ".NAME"));
        List<String> lore = CC.t(Manager.getMenusFile().getStringList(basePath + ".LORE"));

        String materialName = Manager.getMenusFile().getString(basePath + ".MATERIAL");
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            material = Material.BOOK;
            getMain().getLogHandler().logWarn("Invalid material name: " + materialName + " at path: " + basePath);
        }

        ItemStack item = new ItemBuilder(material)
                .setName(name)
                .setLore(lore)
                .toItemStack();

        return new Button() {
            @Override
            public void onClick(InventoryClickEvent e) {
                e.setCancelled(true);
                onClick.run();
            }

            @Override
            public ItemStack getItemStack() {
                return item;
            }
        };
    }
}