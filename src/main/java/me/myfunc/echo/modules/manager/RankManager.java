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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void promotePlayer(Player player, Player target) {
        if (rankList.isEmpty()) {
            player.sendMessage(CC.t(getLanguageFile().getString("GLOBAL.NO_RANKS")));
            return;
        }

        Collections.reverse(rankList);

        UUID targetUUID = target.getUniqueId();
        User user = luckPermsUtil.getUserManager().getUser(targetUUID);

        if (user == null) {
            player.sendMessage(CC.t(getLanguageFile().getString("GLOBAL.DATA_NOT_FOUND")));
            return;
        }

        Optional<String> currentRank = user.getNodes().stream()
                .filter(node -> node.getKey().startsWith("group."))
                .map(node -> node.getKey().replace("group.", ""))
                .filter(rankList::contains)
                .findFirst();

        String newRank;

        if (currentRank.isPresent()) {
            int index = rankList.indexOf(currentRank.get());
            if (index == rankList.size() - 1) {
                player.sendMessage(CC.t(getLanguageFile().getString("PROMOTE_CMD.MAX_RANK")
                        .replace("<player>", target.getName())));
                return;
            }
            newRank = rankList.get(index + 1);
        } else {
            newRank = rankList.get(0);
            user.data().remove(InheritanceNode.builder("default").build());
        }

        currentRank.ifPresent(rank -> user.data().remove(InheritanceNode.builder(rank).build()));

        user.data().add(InheritanceNode.builder(newRank).build());

        luckPermsUtil.getUserManager().saveUser(user);

        player.sendMessage(CC.t(getLanguageFile().getString("PROMOTE_CMD.TARGET_PROMOTED")
                .replace("<player>", target.getName())
                .replace("<rank>", newRank)));
        target.sendMessage(CC.t(getLanguageFile().getString("PROMOTE_CMD.PROMOTED")
                .replace("<player>", player.getName())
                .replace("<rank>", newRank)));
    }

    public void downgradePlayer(Player player, Player target) {
        if (rankList.isEmpty()) {
            player.sendMessage(CC.t(getLanguageFile().getString("GLOBAL.NO_RANKS")));
            return;
        }

        UUID targetUUID = target.getUniqueId();
        User user = luckPermsUtil.getUserManager().getUser(targetUUID);

        if (user == null) {
            player.sendMessage(CC.t(getLanguageFile().getString("GLOBAL.DATA_NOT_FOUND")));
            return;
        }

        Optional<String> currentRank = user.getNodes().stream()
                .filter(node -> node.getKey().startsWith("group."))
                .map(node -> node.getKey().replace("group.", ""))
                .filter(rankList::contains)
                .findFirst();

        String newRank;

        if (currentRank.isPresent()) {
            int index = rankList.indexOf(currentRank.get());

            if (index == rankList.size() - 1) {
                player.sendMessage(CC.t(getLanguageFile().getString("DOWNGRADE_CMD.MIN_RANK")
                        .replace("<player>", target.getName())));
                return;
            }

            newRank = rankList.get(index + 1);
            currentRank.ifPresent(rank -> user.data().remove(InheritanceNode.builder(rank).build()));
            user.data().add(InheritanceNode.builder(newRank).build());
        } else {
            player.sendMessage(CC.t(getLanguageFile().getString("DOWNGRADE_CMD.MIN_RANK")
                    .replace("<player>", target.getName())));
            return;
        }

        luckPermsUtil.getUserManager().saveUser(user);

        player.sendMessage(CC.t(getLanguageFile().getString("DOWNGRADE_CMD.TARGET_DOWNGRADED")
                .replace("<player>", target.getName())
                .replace("<rank>", newRank)));
        target.sendMessage(CC.t(getLanguageFile().getString("DOWNGRADE_CMD.TARGET_DOWNGRADED")
                .replace("<player>", player.getName())
                .replace("<rank>", newRank)));
    }

    public void demotePlayer(Player player, Player target) {
        if (rankList.isEmpty()) {
            player.sendMessage(CC.t(getLanguageFile().getString("GLOBAL.NO_RANKS")));
            return;
        }

        UUID targetUUID = target.getUniqueId();
        User user = luckPermsUtil.getUserManager().getUser(targetUUID);

        if (user == null) {
            player.sendMessage(CC.t(getLanguageFile().getString("GLOBAL.DATA_NOT_FOUND")));
            return;
        }

        boolean removedAnyRank = false;
        for (Node node : user.getNodes()) {
            if (node.getKey().startsWith("group.")) {
                String rank = node.getKey().replace("group.", "");

                if (rankList.contains(rank)) {
                    user.data().remove(node);
                    removedAnyRank = true;
                }
            }
        }

        if (!removedAnyRank) {
            player.sendMessage(CC.t(getLanguageFile().getString("DEMOTE_CMD.NO_RANKS")
                    .replace("<player>", target.getName())));
            return;
        }

        luckPermsUtil.getUserManager().saveUser(user);

        player.sendMessage(CC.t(getLanguageFile().getString("DEMOTE_CMD.TARGET_DEMOTED")
                .replace("<player>", target.getName())));
        target.sendMessage(CC.t(getLanguageFile().getString("DEMOTE_CMD.DEMOTED")
                .replace("<player>", target.getName())));
    }
}