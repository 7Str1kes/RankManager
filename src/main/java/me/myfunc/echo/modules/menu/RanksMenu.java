package me.myfunc.echo.modules.menu;

import me.myfunc.echo.modules.handler.Manager;
import me.myfunc.echo.modules.handler.menu.Menu;
import me.myfunc.echo.modules.handler.menu.MenuManager;
import me.myfunc.echo.modules.handler.menu.button.Button;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.LuckPermsUtil;
import me.myfunc.echo.utilities.extra.ItemBuilder;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RanksMenu extends Menu {

    private final LuckPermsUtil lpUtil = getMain().getLuckPermsUtil();

    public RanksMenu(MenuManager menuManager, Player viewer) {
        super(menuManager, viewer,
                Manager.getMenusFile().getString("RANKS.TITLE"),
                Manager.getMenusFile().getInt("RANKS.SIZE"),
                false);

        this.setFillEnabled(true);
        this.setFiller(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&7").toItemStack());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        List<Group> groups = new ArrayList<>(lpUtil.getLoadedGroups());

        groups.sort(Comparator.comparingInt(g -> g.getWeight().orElse(0)));
        Collections.reverse(groups);

        int size = getSize();
        int slot = 0;

        for (Group group : groups) {
            if (slot >= size) break;

            String name = group.getName();
            int weight = group.getWeight().orElse(0);
            CachedMetaData meta = group.getCachedData().getMetaData();
            String prefix = meta.getPrefix() != null ? meta.getPrefix() : "";
            String suffix = meta.getSuffix() != null ? meta.getSuffix() : "";

            ItemStack item = createRankItem(name, weight, prefix, suffix);
            buttons.put(slot++, new Button() {
                @Override
                public void onClick(InventoryClickEvent e) {
                    e.setCancelled(true);
                }

                @Override
                public ItemStack getItemStack() {
                    return item;
                }
            });
        }

        return buttons;
    }

    private ItemStack createRankItem(String name, int weight, String prefix, String suffix) {
        String basePath = "RANKS.ITEMS.RANK_ITEM";

        String materialName = Manager.getMenusFile().getString(basePath + ".MATERIAL");
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            material = Material.NAME_TAG;
            getMain().getLogHandler().logWarn("Invalid material in RanksMenu config: " + materialName);
        }

        String displayName = Manager.getMenusFile().getString(basePath + ".NAME")
                .replace("<name>", name)
                .replace("<weight>", String.valueOf(weight))
                .replace("<prefix>", prefix)
                .replace("<suffix>", suffix);

        List<String> lore = Manager.getMenusFile().getStringList(basePath + ".LORE").stream()
                .map(line -> line
                        .replace("<name>", name)
                        .replace("<weight>", String.valueOf(weight))
                        .replace("<prefix>", prefix)
                        .replace("<suffix>", suffix))
                .toList();

        return new ItemBuilder(material)
                .setName(CC.t(displayName))
                .setLore(CC.t(lore))
                .toItemStack();
    }
}