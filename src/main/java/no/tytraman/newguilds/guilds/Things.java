package no.tytraman.newguilds.guilds;

import org.bukkit.ChatColor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Things {

    public static int convertExpToLevel(int exp) {
        int level = (int)(0.2 * Math.sqrt(exp));
        return level;
    }

    public static int getExpRequired(int level) {
        int exp = (int)Math.pow((level + 1) / 0.2, 2);
        return exp;
    }

    public static String getLisibleTimeFromMillis(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        String strTime = date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear() + " " + date.getHour() + "h" + date.getMinute() + "m" + date.getSecond() + "s";
        return strTime;
    }

    public static ChatColor getRandomColor() {
        return ChatColor.values()[new Random().nextInt(16)];
    }

    public static <T> List<T> convertSetToList(Set<T> set) {
        List<T> list = new ArrayList<>();
        for(T t : set) {
            list.add(t);
        }
        return list;
    }



}
