package me.myfunc.echo.modules.handler.command;

import me.myfunc.echo.Ranks;
import me.myfunc.echo.modules.command.MainCommand;
import me.myfunc.echo.modules.command.list.*;

import java.util.Arrays;

/*
 * RankManager | CommandRegistry
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
public class CommandRegistry {

    public static void registerAll(Ranks main) {
        CommandManager manager = new CommandManager(main);

        Arrays.asList(
                new MainCommand(manager),
                new GrantsCmd(manager),
                new PermsCmd(manager),
                new PromoteCmd(manager),
                new DowngradeCmd(manager),
                new DemoteCmd(manager)
        ).forEach(manager::register);
    }
}