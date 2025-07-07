package me.myfunc.echo.modules.listener;

import lombok.Getter;
import me.myfunc.echo.Ranks;
import me.myfunc.echo.modules.handler.menu.listener.MenuListener;
import me.myfunc.echo.modules.listener.list.MainListener;
import me.myfunc.echo.modules.handler.Manager;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

/*
 * RankManager | ListenerManager
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
@Getter
public class ListenerManager extends Manager {

    public ListenerManager(Ranks main) {
        super(main);

        List<Listener> listeners = Arrays.asList(
                new MainListener(this),
                new MenuListener(getMain().getMenuManager())
        );

        registerListener(listeners.toArray(new Listener[0]));
    }

    private void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            getMain().getServer().getPluginManager().registerEvents(listener, getMain());
        }
    }
}