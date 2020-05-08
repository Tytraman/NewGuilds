package no.tytraman.newguilds.tabscompleter;

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
            for(String str : new String[]{"create", "accept", "leave", "join", "newchef", "setcolor", "setdescription", "show"}) {
                if(str.startsWith(args[0]))
                    list.add(str);
            }
            Collections.sort(list);
            return list;
        }else
            return list;
    }
}
