package me.myfunc.echo.modules.command.list;

import me.myfunc.echo.modules.handler.command.Command;
import me.myfunc.echo.modules.handler.command.CommandManager;
import me.myfunc.echo.modules.manager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * RankManager | DowngradeCmd
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class DowngradeCmd extends Command {

    public DowngradeCmd(CommandManager manager) {
        super(manager, "downgrade", "rankmanager.downgrade");
    }

    @Override
    public List<String> aliases() {
        return List.of();
    }

    @Override
    public List<String> usage() {
        return getLanguageFile().getStringList("DOWNGRADE_CMD.USAGE");
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

        if (args.length == 0) {
            sendUsage(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sendMessage(player, getLanguageFile().getString("GLOBAL.PLAYER_NOT_FOUND"));
            return;
        }

        RankManager rankManager = getMain().getRankManager();
        rankManager.downgradePlayer(player, target);
    }
}