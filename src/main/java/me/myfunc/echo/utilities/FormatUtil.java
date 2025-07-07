package me.myfunc.echo.utilities;

/*
 * RankManager | FormatUtil
 *
 * @author 7Str1kes
 * @date 07/07/2025
 *
 * Copyright (c) 2025 EchoDev. All rights reserved.
 */
public class FormatUtil {

    public static long parseDuration(String input) {
        long duration = 0;
        String number = "";
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                number += c;
            } else {
                switch (c) {
                    case 'd' -> duration += Long.parseLong(number) * 86400;
                    case 'h' -> duration += Long.parseLong(number) * 3600;
                    case 'm' -> duration += Long.parseLong(number) * 60;
                    case 's' -> duration += Long.parseLong(number);
                    case 'y' -> duration += Long.parseLong(number) * 31536000;
                    default -> throw new IllegalArgumentException("Formato de duración inválido: " + input);
                }
                number = "";
            }
        }
        return duration;
    }

    public static long convertToSeconds(int duration, String unit) {
        return switch (unit) {
            case "m" -> duration * 60L;
            case "h" -> duration * 3600L;
            case "d" -> duration * 86400L;
            case "y" -> duration * 31536000L;
            default -> throw new IllegalArgumentException("Unidad de tiempo inválida: " + unit);
        };
    }
}