package me.myfunc.echo;

import lombok.Getter;
import me.myfunc.echo.modules.handler.LogHandler;
import me.myfunc.echo.modules.handler.command.CommandRegistry;
import me.myfunc.echo.modules.handler.menu.MenuManager;
import me.myfunc.echo.modules.listener.ListenerManager;
import me.myfunc.echo.modules.handler.Manager;

import me.myfunc.echo.modules.manager.RankManager;
import me.myfunc.echo.utilities.LuckPermsUtil;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * RankManager | Graves
 *
 * @author 7Str1kes
 * @date 22/06/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
@Getter
public final class Ranks extends JavaPlugin {
    // Core
    @Getter private static Ranks main;
    private final LogHandler logHandler = new LogHandler();
    private LuckPermsUtil luckPermsUtil;
    private final Manager manager = new Manager(this);

    // Managers
    private ListenerManager listenerManager;
    private MenuManager menuManager;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;
        logHandler.sendEnable();
        luckPermsUtil = new LuckPermsUtil();

        menuManager = new MenuManager(this);
        listenerManager = new ListenerManager(this);
        rankManager = new RankManager(this);

        CommandRegistry.registerAll(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logHandler.sendDisable();
    }
}
