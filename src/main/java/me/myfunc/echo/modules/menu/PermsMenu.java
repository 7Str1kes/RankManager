package me.myfunc.echo.modules.menu;

import me.myfunc.echo.modules.handler.Manager;
import me.myfunc.echo.modules.handler.menu.Menu;
import me.myfunc.echo.modules.handler.menu.MenuManager;
import me.myfunc.echo.modules.handler.menu.button.Button;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.LuckPermsUtil;
import me.myfunc.echo.utilities.extra.ItemBuilder;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * RankManager | PermsMenu
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class PermsMenu extends Menu {

    private final User targetUser;
    private final LuckPermsUtil lpUtil = getMain().getLuckPermsUtil();

    public PermsMenu(MenuManager menuManager, Player viewer, User targetUser) {
        super(menuManager, viewer,
                Manager.getMenusFile().getString("PERMS.TITLE").replace("<player>", targetUser.getUsername()),
                Manager.getMenusFile().getInt("PERMS.SIZE"),
                false);
        this.targetUser = targetUser;
        this.setFillEnabled(true);
        this.setFiller(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&7").toItemStack());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Node> nodes = targetUser.getNodes().stream()
                .filter(node -> !node.getKey().startsWith("group."))
                .toList();

        int size = ((nodes.size() - 1) / 9 + 1) * 9;
        this.setSize(size);

        int i = 0;
        for (Node node : nodes) {
            String permission = node.getKey();
            boolean isTemporary = node.hasExpiry();

            String duration = isTemporary
                    ? lpUtil.getDurationString(Duration.between(Instant.now(), node.getExpiry()))
                    : Manager.getLanguageFile().getString("PLACEHOLDERS.PERMANENT");

            String path = "PERMS.ITEMS.PERMISSION_ITEM";

            String materialName = Manager.getMenusFile().getString(path + ".MATERIAL");
            Material material = Material.getMaterial(materialName);
            if (material == null) {
                material = Material.BOOK;
                getMain().getLogHandler().logWarn("Invalid material: " + materialName + " in perms menu.");
            }

            String name = Manager.getMenusFile().getString(path + ".NAME")
                    .replace("<permission>", permission);

            List<String> lore = Manager.getMenusFile().getStringList(path + ".LORE").stream()
                    .map(line -> line
                            .replace("<permission>", permission)
                            .replace("<duration>", duration))
                    .toList();

            ItemStack item = new ItemBuilder(material)
                    .setName(name)
                    .setLore(CC.t(lore))
                    .toItemStack();

            buttons.put(i++, new Button() {
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                    targetUser.data().remove(node);
                    lpUtil.getUserManager().saveUser(targetUser);
                    new PermsMenu(getManager(), player, targetUser).open();
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