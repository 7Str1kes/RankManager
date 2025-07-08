package me.myfunc.echo.modules.command.list;

import me.myfunc.echo.modules.handler.command.Command;
import me.myfunc.echo.modules.handler.command.CommandManager;
import me.myfunc.echo.modules.menu.PermsMenu;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.LuckPermsUtil;
import net.luckperms.api.model.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * RankManager | PermsCmd
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class PermsCmd extends Command {

    private final LuckPermsUtil luckPermsUtil = getMain().getLuckPermsUtil();

    public PermsCmd(CommandManager manager) {
        super(manager, "perminfo", "rankmanager.perms");
    }

    @Override
    public List<String> aliases() {
        return List.of();
    }

    @Override
    public List<String> usage() {
        return getLanguageFile().getStringList("PERMS_CMD.USAGE");
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

        String targetName = args[0];

        User user = luckPermsUtil.getUser(targetName);

        if (user == null) {
            player.sendMessage(CC.t(getLanguageFile().getString("GLOBAL.DATA_NOT_FOUND"))
                    .replace("<player>", targetName));
            return;
        }

        new PermsMenu(getMain().getMenuManager(), player, user).open();
    }
}