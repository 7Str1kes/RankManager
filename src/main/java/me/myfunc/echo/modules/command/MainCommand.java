package me.myfunc.echo.modules.command;

import me.myfunc.echo.modules.handler.command.Command;
import me.myfunc.echo.modules.handler.command.CommandManager;
import me.myfunc.echo.modules.menu.MainMenu;
import me.myfunc.echo.modules.menu.RanksMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * RankManager | GraveCommand
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
public class MainCommand extends Command {

    public MainCommand(CommandManager manager) {
        super(manager, "rankmanager", "rankmanager.admin");
    }

    @Override
    public List<String> aliases() {
        return List.of("rm");
    }

    @Override
    public List<String> usage() {
        return getLanguageFile().getStringList("MAIN_CMD.USAGE");
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
            new MainMenu(getMain().getMenuManager(), player).open();
            return;
        }

        String action = args[0];

        switch (action) {
            case "reload" -> {
                if (args.length < 2) {
                    sendMessage(player, "&eUsage: /rankmanager reload <configFile>");
                    return;
                }

                String fileName = args[1].toLowerCase().replace(".yml", "");

                switch (fileName) {
                    case "config" -> getConfigFile().reload();
                    case "language" -> getLanguageFile().reload();
                    case "menus" -> getMenusFile().reload();
                    default -> {
                        sendMessage(player, "&cArchivo no encontrado: &f" + fileName);
                        return;
                    }
                }

                sendMessage(player, "&aArchivo &f" + fileName + ".yml &arecargado con éxito.");
            }
        }
    }
}