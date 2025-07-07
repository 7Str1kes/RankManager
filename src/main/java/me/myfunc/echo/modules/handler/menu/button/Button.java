package me.myfunc.echo.modules.handler.menu.button;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/*
 * RankManager | Button
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
@Getter
public abstract class Button {

    public abstract void onClick(InventoryClickEvent e);

    public abstract ItemStack getItemStack();
}