package me.myfunc.echo.modules.handler.command;

import me.myfunc.echo.Ranks;
import me.myfunc.echo.modules.handler.command.executor.CommandExecutorAdapter;
import me.myfunc.echo.modules.handler.command.executor.DynamicCommand;
import me.myfunc.echo.modules.handler.Manager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import java.lang.reflect.Field;

/*
 * RankManager | CommandManager
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
public class CommandManager extends Manager {

    public CommandManager(Ranks main) {
        super(main);
    }

    public void register(Command command) {
        try {
            DynamicCommand dynamicCommand = new DynamicCommand(
                    command.getName(),
                    "Command name: " + command.getName() + ", Permission: " + command.getPermission() + ", Aliases: " + command.aliases(),
                    command.getPermission(),
                    command.aliases().toArray(new String[0])
            );

            dynamicCommand.setExecutor(new CommandExecutorAdapter(command));
            getCommandMap().register(getMain().getDescription().getName(), dynamicCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CommandMap getCommandMap() throws Exception {
        Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
    }
}