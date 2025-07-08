package me.myfunc.echo.modules.manager;

import lombok.Getter;
import me.myfunc.echo.Ranks;
import me.myfunc.echo.modules.handler.Manager;
import me.myfunc.echo.utilities.CC;
import me.myfunc.echo.utilities.LuckPermsUtil;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;

import java.util.*;

/*
 * RankManager | RankManager
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
@Getter
public class RankManager extends Manager {
    private final LuckPermsUtil luckPermsUtil = getMain().getLuckPermsUtil();
    private final List<String> rankList = Manager.getConfigFile().getStringList("RANK_LIST");

    public RankManager(Ranks main) {
        super(main);
    }

    public void promotePlayer(Player sender, Player target) {
        if (!hasRanks(sender)) return;

        User user = getUserOrMsg(sender, target);
        if (user == null) return;

        Optional<String> current = getCurrentRank(user);
        List<String> reversed = new ArrayList<>(rankList);
        Collections.reverse(reversed);

        String newRank;

        if (current.isPresent()) {
            int index = reversed.indexOf(current.get());
            if (index == reversed.size() - 1) {
                sendLang(sender, "PROMOTE_CMD.MAX_RANK", target);
                return;
            }
            newRank = reversed.get(index + 1);
        } else {
            newRank = reversed.getFirst();
            user.data().remove(InheritanceNode.builder("default").build());
        }

        current.ifPresent(rank -> user.data().remove(InheritanceNode.builder(rank).build()));
        user.data().add(InheritanceNode.builder(newRank).build());
        luckPermsUtil.getUserManager().saveUser(user);

        sendLang(sender, "PROMOTE_CMD.TARGET_PROMOTED", target, newRank);
        sendLang(target, "PROMOTE_CMD.PROMOTED", sender, newRank);
    }

    public void downgradePlayer(Player sender, Player target) {
        if (!hasRanks(sender)) return;

        User user = getUserOrMsg(sender, target);
        if (user == null) return;

        Optional<String> current = getCurrentRank(user);
        if (current.isEmpty()) {
            sendLang(sender, "DOWNGRADE_CMD.MIN_RANK", target);
            return;
        }

        int index = rankList.indexOf(current.get());
        if (index == rankList.size() - 1) {
            sendLang(sender, "DOWNGRADE_CMD.MIN_RANK", target);
            return;
        }

        String newRank = rankList.get(index + 1);
        user.data().remove(InheritanceNode.builder(current.get()).build());
        user.data().add(InheritanceNode.builder(newRank).build());
        luckPermsUtil.getUserManager().saveUser(user);

        sendLang(sender, "DOWNGRADE_CMD.TARGET_DOWNGRADED", target, newRank);
        sendLang(target, "DOWNGRADE_CMD.TARGET_DOWNGRADED", sender, newRank);
    }

    public void demotePlayer(Player sender, Player target) {
        if (!hasRanks(sender)) return;

        User user = getUserOrMsg(sender, target);
        if (user == null) return;

        boolean removed = false;
        for (Node node : user.getNodes()) {
            if (node.getKey().startsWith("group.")) {
                String rank = node.getKey().substring(6);
                if (rankList.contains(rank)) {
                    user.data().remove(node);
                    removed = true;
                }
            }
        }

        if (!removed) {
            sendLang(sender, "DEMOTE_CMD.NO_RANKS", target);
            return;
        }

        luckPermsUtil.getUserManager().saveUser(user);
        sendLang(sender, "DEMOTE_CMD.TARGET_DEMOTED", target);
        sendLang(target, "DEMOTE_CMD.DEMOTED", sender);
    }

    private Optional<String> getCurrentRank(User user) {
        return user.getNodes().stream()
                .filter(node -> node.getKey().startsWith("group."))
                .map(node -> node.getKey().substring(6))
                .filter(rankList::contains)
                .findFirst();
    }

    private User getUserOrMsg(Player sender, Player target) {
        User user = luckPermsUtil.getUserManager().getUser(target.getUniqueId());
        if (user == null) {
            sendLang(sender, "GLOBAL.DATA_NOT_FOUND");
        }
        return user;
    }

    private boolean hasRanks(Player sender) {
        if (rankList.isEmpty()) {
            sendLang(sender, "GLOBAL.NO_RANKS");
            return false;
        }
        return true;
    }

    private void sendLang(Player player, String path) {
        player.sendMessage(CC.t(getLanguageFile().getString(path)));
    }

    private void sendLang(Player player, String path, Player other) {
        player.sendMessage(CC.t(getLanguageFile().getString(path)
                .replace("<player>", other.getName())));
    }

    private void sendLang(Player player, String path, Player other, String rank) {
        player.sendMessage(CC.t(getLanguageFile().getString(path)
                .replace("<player>", other.getName())
                .replace("<rank>", rank)));
    }
}