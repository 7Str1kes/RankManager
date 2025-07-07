package me.myfunc.echo.modules.handler.menu;

import lombok.Getter;
import me.myfunc.echo.Ranks;
import me.myfunc.echo.modules.handler.Manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * RankManager | MenuManager
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
@Getter
public class MenuManager extends Manager {

    private final Map<UUID, Menu> menus;

    public MenuManager(Ranks main) {
        super(main);
        this.menus = new HashMap<>();
    }
}