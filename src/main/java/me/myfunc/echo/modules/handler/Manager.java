package me.myfunc.echo.modules.handler;

import lombok.Getter;
import me.myfunc.echo.Ranks;
import me.myfunc.echo.utilities.ConfigFile;

import java.util.ArrayList;
import java.util.List;

/*
 * RankManager | Manager
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
@Getter
public class Manager {

    private final Ranks main;
    @Getter private static ConfigFile configFile, languageFile, menusFile;
    @Getter private final List<ConfigFile> files = new ArrayList<>();


    public Manager(Ranks main) {
        this.main = main;
        this.load();
    }

    private void load() {
        configFile = new ConfigFile(main, "config.yml");
        languageFile = new ConfigFile(main, "language.yml");
        menusFile = new ConfigFile(main, "menus.yml");

        files.add(configFile);
        files.add(languageFile);
        files.add(menusFile);
    }
}