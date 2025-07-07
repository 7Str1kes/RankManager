package me.myfunc.echo.modules.handler.command.executor;

import java.util.List;

import me.myfunc.echo.modules.handler.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

/*
 * RankManager | CommandExecutorAdapter
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
public class CommandExecutorAdapter implements CommandExecutor, TabCompleter {

    private final Command instance;

    public CommandExecutorAdapter(Command instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String @NotNull [] args) {
        instance.execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, String @NotNull [] args) {
        return instance.tabComplete(sender, args);
    }
}