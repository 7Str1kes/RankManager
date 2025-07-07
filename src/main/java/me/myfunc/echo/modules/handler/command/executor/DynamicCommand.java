package me.myfunc.echo.modules.handler.command.executor;

import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/*
 * RankManager | DynamicCommand
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
@Setter
public class DynamicCommand extends Command {

    private CommandExecutor executor;

    public DynamicCommand(String name, String description, String permission, String[] aliases) {
        super(name);
        setDescription(description);
        setPermission(permission);
        setAliases(List.of(aliases));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        if (executor != null) {
            return executor.onCommand(sender, this, label, args);
        }

        return false;
    }
}