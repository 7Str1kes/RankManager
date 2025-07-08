package me.myfunc.echo.modules.command.list;

import me.myfunc.echo.modules.handler.command.Command;
import me.myfunc.echo.modules.handler.command.CommandManager;
import me.myfunc.echo.modules.menu.GrantsMenu;
import me.myfunc.echo.modules.menu.RanksMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * RankManager | RanksCommand
 *
 * @author 7Str1kes
 * @date 08/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class RanksCommand extends Command {

    public RanksCommand(CommandManager manager) {
        super(manager, "ranks", "rankmanager.ranks");
    }

    @Override
    public List<String> aliases() {
        return List.of();
    }

    @Override
    public List<String> usage() {
        return List.of();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sendMessage(sender, getLanguageFile().getString("GLOBAL.NOT_PLAYER"));
            return;
        }

        if (!sender.hasPermission(getPermission())) {
            sendMessage(player, getLanguageFile().getString("GLOBAL.NO_PERMISSION"));
            return;
        }

        new RanksMenu(getMain().getMenuManager(), player).open();
    }
}