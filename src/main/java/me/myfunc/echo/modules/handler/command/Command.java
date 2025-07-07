package me.myfunc.echo.modules.handler.command;

import lombok.Getter;
import me.myfunc.echo.Ranks;
import me.myfunc.echo.modules.handler.Module;
import me.myfunc.echo.modules.handler.Manager;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * RankManager | Command
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
@Getter
public abstract class Command extends Module<CommandManager> {

    protected final Ranks main;
    private final String name;
    private final String permission;

    public Command(CommandManager manager, String name, String permission) {
        super(manager);
        this.main = manager.getMain();
        this.name = name;
        this.permission = permission;
    }

    protected void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(CC.t(msg));
    }

    protected void sendMessage(Player player, String msg) {
        player.sendMessage(CC.t(msg));
    }

    protected ConfigFile getConfigFile() {
        return Manager.getConfigFile();
    }

    protected ConfigFile getLanguageFile() {
        return Manager.getLanguageFile();
    }

    public abstract List<String> tabComplete(CommandSender sender, String[] args);

    public abstract List<String> aliases();

    public abstract List<String> usage();

    public abstract void execute(CommandSender sender, String[] args);

    protected void sendUsage(CommandSender sender) {
        sendMessage(sender, String.join(" ", usage()));
    }

    protected void sendUsage(Player player) {
        sendMessage(player, String.join(" ", usage()));
    }
}