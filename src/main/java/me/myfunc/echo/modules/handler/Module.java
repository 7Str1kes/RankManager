package me.myfunc.echo.modules.handler;

import lombok.Getter;
import me.myfunc.echo.Ranks;
import org.bukkit.event.Listener;

/*
 * RankManager | Module
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
@Getter
public class Module<T extends Manager> implements Listener {

    private final Ranks main;
    private final T manager;

    public Module(T manager) {
        this.main = manager.getMain();
        this.manager = manager;
    }
}