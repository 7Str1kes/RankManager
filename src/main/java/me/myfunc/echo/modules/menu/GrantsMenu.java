package me.myfunc.echo.modules.menu;

import me.myfunc.echo.modules.handler.Manager;
import me.myfunc.echo.modules.handler.menu.Menu;
import me.myfunc.echo.modules.handler.menu.MenuManager;
import me.myfunc.echo.modules.handler.menu.button.Button;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.LuckPermsUtil;
import me.myfunc.echo.utilities.extra.ItemBuilder;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.util.Tristate;
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
                Manager.getMenusFile().getString("GRANTS.TITLE", "&cGrants").replace("<player>", user.getUsername()),
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
                .distinct()
                .toList();

        for (String group : groups) {
            buttons.put(slot++, createGroupButton(player, group));
        }

        return buttons;
    }

    private Button createGroupButton(Player player, String group) {
        String basePath = "GRANTS.ITEMS.RANK_ITEM";

        String duration = lpUtil.getDurationString(lpUtil.getGroupDuration(group));
        String prefix = lpUtil.getGroupPrefix(group);
        String suffix = lpUtil.getGroupSuffix(group);

        String materialName = Manager.getMenusFile().getString(basePath + ".MATERIAL");
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            material = Material.BOOK;
            getMain().getLogHandler().logWarn("Invalid material name in grants menu: " + materialName);
        }

        String name = Manager.getMenusFile().getString(basePath + ".NAME", "&f<group>")
                .replace("<prefix>", prefix)
                .replace("<suffix>", suffix)
                .replace("<group>", group);

        List<String> lore = Manager.getMenusFile().getStringList(basePath + ".LORE").stream()
                .map(line -> line
                        .replace("<duration>", duration)
                        .replace("<prefix>", prefix)
                        .replace("<suffix>", suffix)
                        .replace("<group>", group))
                .toList();

        ItemStack item = new ItemBuilder(material)
                .setName(CC.t(name))
                .setLore(CC.t(lore))
                .toItemStack();

        return new Button() {
            @Override
            public void onClick(InventoryClickEvent e) {
                e.setCancelled(true);
                InheritanceNode node = InheritanceNode.builder(group).build();

                if (user.data().contains(node, NodeEqualityPredicate.EXACT) == Tristate.TRUE) {
                    user.data().remove(node);
                    lpUtil.getUserManager().saveUser(user);
                    new GrantsMenu(getManager(), player, user).open();
                } else {
                    player.sendMessage(CC.t("&cEse rango ya no está asignado."));
                }
            }

            @Override
            public ItemStack getItemStack() {
                return item;
            }
        };
    }
}