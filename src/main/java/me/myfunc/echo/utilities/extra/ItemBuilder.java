package me.myfunc.echo.utilities.extra;

import me.myfunc.echo.utilities.CC;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/*
 * RankManager | ItemBuilder
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */

public class ItemBuilder {
    private final ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public ItemBuilder(ItemStack is, int amount) {
        this.is = is.clone();
        this.is.setAmount(amount);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(CC.t(name));
        is.setItemMeta(im);
        return this;
    }

    public List<String> getLore() {
        if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
            return is.getItemMeta().getLore();
        }

        return Collections.emptyList();
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(CC.t(lore));
        is.setItemMeta(im);
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }
}