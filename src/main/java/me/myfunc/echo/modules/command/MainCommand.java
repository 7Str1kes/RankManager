package me.myfunc.echo.modules.command;

import me.myfunc.echo.modules.handler.command.Command;
import me.myfunc.echo.modules.handler.command.CommandManager;
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
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return List.of("reload");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            return getManager().getFiles().stream()
                    .map(file -> file.getName().replace(".yml", ""))
                    .toList();
        }

        return List.of();
    }

    @Override
    public List<String> aliases() {
        return List.of("rm", "rankmanage", "manageranks");
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
            sendUsage(player);
            return;
        }

        String action = args[0];

        switch (action) {
            case "reload" -> {
                if (args.length < 2) {
                    sendMessage(player, "&eUsage: /echograves reload <configFile>");
                    return;
                }

                String fileName = args[1].toLowerCase().replace(".yml", "");

                switch (fileName) {
                    case "config" -> getConfigFile().reload();
                    case "language" -> getLanguageFile().reload();
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