package me.myfunc.echo.modules.menu;

import me.myfunc.echo.modules.handler.Manager;
import me.myfunc.echo.modules.handler.menu.Menu;
import me.myfunc.echo.modules.handler.menu.MenuManager;
import me.myfunc.echo.modules.handler.menu.button.Button;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.LuckPermsUtil;
import me.myfunc.echo.utilities.extra.ItemBuilder;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * RankManager | GrantsMenu
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class GrantsMenu extends Menu {

    private final User user;
    private final LuckPermsUtil lpUtil;

    public GrantsMenu(MenuManager menuManager, Player viewer, User user) {
        super(menuManager, viewer,
                Manager.getMenusFile().getString("GRANTS.TITLE").replace("<player>", user.getUsername()),
                Manager.getMenusFile().getInt("GRANTS.SIZE"),
                false);
        this.user = user;
        this.lpUtil = getMain().getLuckPermsUtil();
        this.setFillEnabled(true);
        this.setFiller(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&7").toItemStack());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int slot = 0;

        List<String> groups = user.getNodes().stream()
                .filter(node -> node.getKey().startsWith("group."))
                .map(node -> node.getKey().substring("group.".length()))
                .toList();

        for (String group : groups) {
            String basePath = "GRANTS.ITEMS.RANK_ITEM";

            String duration = lpUtil.getDurationString(lpUtil.getGroupDuration(group));
            String prefix = lpUtil.getGroupPrefix(group);
            String suffix = lpUtil.getGroupSuffix(group);

            String materialName = Manager.getMenusFile().getString(basePath + ".MATERIAL");
            Material material = Material.getMaterial(materialName);
            if (material == null) {
                material = Material.BOOK;
                getMain().getLogHandler().logWarn("Invalid material name: " + materialName + " in grants menu.");
            }

            String name = Manager.getMenusFile().getString(basePath + ".NAME")
                    .replace("<prefix>", prefix)
                    .replace("<suffix>", suffix);

            List<String> lore = Manager.getMenusFile().getStringList(basePath + ".LORE").stream()
                    .map(line -> line
                            .replace("<duration>", duration)
                            .replace("<prefix>", prefix)
                            .replace("<suffix>", suffix))
                    .toList();

            ItemStack item = new ItemBuilder(material)
                    .setName(name)
                    .setLore(CC.t(lore))
                    .toItemStack();

            buttons.put(slot++, new Button() {
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                    InheritanceNode node = InheritanceNode.builder(group).build();
                    user.data().remove(node);
                    lpUtil.getUserManager().saveUser(user);
                    new GrantsMenu(getManager(), player, user).open();
                }

                @Override
                public ItemStack getItemStack() {
                    return item;
                }
            });
        }

        return buttons;
    }
}