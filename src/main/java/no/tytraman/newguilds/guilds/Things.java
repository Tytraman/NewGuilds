package no.tytraman.newguilds.guilds;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Things {

    public static int convertExpToLevel(int exp) {
        int level = (int)(0.2 * Math.sqrt(exp));
        return level;
    }

    public static int getExpRequired(int level) {
        int exp = (int)Math.pow((level + 1) / 0.2, 2);
        return exp;
    }

    public static int getExpPrecedentLevel(int level) {
        int exp = (int)Math.pow((level - 1) / 0.2, 2);
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

    public static ItemStack createItemForGui(Material material, String customName, String[] lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(customName);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);

        return item;
    }
}
