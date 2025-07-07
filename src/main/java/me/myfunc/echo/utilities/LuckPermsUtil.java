package me.myfunc.echo.utilities;

import lombok.Getter;
import me.myfunc.echo.modules.handler.Manager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.InheritanceNode;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/*
 * RankManager | LuckPermsUtil
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 7Str1kes. All rights reserved.
 */
public class LuckPermsUtil {
    private final ConfigFile language = Manager.getLanguageFile();

    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public Map<Integer, Group> getLowerGroups(Group executorGroup) {
        Map<Integer, Group> lowerGroups = new HashMap<>();
        executorGroup.getWeight().ifPresent(executorWeight -> {
            luckPerms.getGroupManager().getLoadedGroups().stream()
                    .filter(group -> group.getWeight().isPresent() && group.getWeight().getAsInt() < executorWeight)
                    .forEach(group -> lowerGroups.put(lowerGroups.size(), group));
        });
        return lowerGroups;
    }

    public User getUser(String name) {
        return luckPerms.getUserManager().getUser(name);
    }

    public User getUser(UUID uuid) {
        return luckPerms.getUserManager().getUser(uuid);
    }

    public UserManager getUserManager() {
        return luckPerms.getUserManager();
    }

    public GroupManager getGroupManager() {
        return luckPerms.getGroupManager();
    }

    public Group getGroup(String name) {
        return luckPerms.getGroupManager().getGroup(name);
    }

    public String getGroupPrefix(String groupName) {
        Group group = getGroup(groupName);
        if (group == null) return "";
        String prefix = group.getCachedData().getMetaData().getPrefix();
        if (prefix == null) {
            return language.getString("PLACEHOLDERS.NO_PREFIX");
        }
        return group.getCachedData().getMetaData().getPrefix();
    }

    public String getGroupSuffix(String groupName) {
        Group group = getGroup(groupName);
        if (group == null) return "";
        String suffix = group.getCachedData().getMetaData().getSuffix();
        if (suffix == null) {
            return language.getString("PLACEHOLDERS.NO_SUFFIX");
        }
        return group.getCachedData().getMetaData().getSuffix();
    }

    public int getGroupWeight(String groupName) {
        Group group = getGroup(groupName);
        return group != null ? group.getWeight().orElse(0) : 0;
    }

    public Duration getGroupDuration(String groupName) {
        Group group = getGroup(groupName);
        if (group == null) return Duration.ZERO;

        Optional<InheritanceNode> node = group.getNodes().stream()
                .filter(n -> n instanceof InheritanceNode)
                .map(n -> (InheritanceNode) n)
                .filter(n -> n.getGroupName().equalsIgnoreCase(groupName))
                .findFirst();

        if (node.isPresent()) {
            InheritanceNode inheritanceNode = node.get();
            if (inheritanceNode.hasExpiry()) {
                return Duration.between(Instant.now(), inheritanceNode.getExpiry());
            }
        }

        return Duration.ZERO;
    }

    public String getDurationString(@Nullable Duration duration) {
        if (duration.isZero()) {
            return language.getString("PLACEHOLDERS.PERMANENT");
        }

        if (duration.isNegative()) {
            return language.getString("PLACEHOLDERS.EXPIRED");
        }

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0) sb.append(seconds).append("s");

        return sb.toString().trim();
    }
}