package no.tytraman.newguilds.tabscompleter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuildPressTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1) {
            for(String str : new String[]{"create", "accept", "leave", "join", "newchef", "setcolor", "setdescription", "show", "infodev", "tp", "msg", "sethome", "home"}) {
                if(str.startsWith(args[0]))
                    list.add(str);
            }
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("newchef") || args[0].equalsIgnoreCase("tp")){
                return null;
            }else if(args[0].equalsIgnoreCase("setcolor")) {
                for(ChatColor color : ChatColor.values()) {
                    if(color.name().toLowerCase().startsWith(args[1].toLowerCase()))
                        list.add(color.name());
                }
            }
        }
        Collections.sort(list);
        return list;
    }
}
