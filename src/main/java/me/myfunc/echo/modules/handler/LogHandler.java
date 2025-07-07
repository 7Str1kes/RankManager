package me.myfunc.echo.modules.handler;

import lombok.Getter;
import me.myfunc.echo.utilities.CC;
import org.bukkit.Bukkit;

/*
 * RankManager | LogHandler
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
@Getter
public class LogHandler {
    private final String LINE = CC.t("&5&m                                   &r");

    public void sendEnable() {
        print(
                LINE,
                "&d[RankManager] &7Enabling plugin...",
                "",
                "&7» &dAuthor: &57Str1kes",
                "&7» &dVersion: &51.0-SNAPSHOT",
                LINE
        );
    }

    public void sendDisable() {
        print(
                LINE,
                "&d[RankManager] &7Disabling plugin...",
                "",
                "&7» &dAuthor: &57Str1kes",
                "&7» &dVersion: &51.0-SNAPSHOT",
                LINE
        );
    }

    public void logInfo(String... messages) {
        for (String message : messages) {
            print("&7[INFO] &a" + message);
        }
    }

    public void logError(String... messages) {
        for (String message : messages) {
            print("&7[ERROR] &c" + message);
        }
    }

    public void logWarn(String... messages) {
        for (String message : messages) {
            print("&7[WARN] &e" + message);
        }
    }

    public static void print(String... messages) {
        for (String message : messages) {
            Bukkit.getConsoleSender().sendMessage(CC.t(message));
        }
    }
}