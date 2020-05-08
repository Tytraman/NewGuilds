package no.tytraman.newguilds;

import no.tytraman.newguilds.guilds.Guild;
import no.tytraman.newguilds.threads.Tails;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player)sender;
            //guild
            if(label.equalsIgnoreCase("guild")) {
                if(args.length > 0) {
                    //create
                    if(args[0].equalsIgnoreCase("create")) {
                        if(args.length > 1) {
                            if(args.length == 2) {
                                new Guild(p.getUniqueId().toString()).createNewGuild(args[1]);
                            }else
                                p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Le nom de la guilde ne peut pas contenir d'espace.");
                        }else
                            p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Indique le nom de la guilde.");
                    //accept
                    }else if(args[0].equalsIgnoreCase("accept")) {
                        if(args.length > 1) {
                            new Guild(p.getUniqueId().toString()).acceptMember(args[1]);
                        }else
                            p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Indique le pseudo du joueur.");
                    //leave
                    }else if(args[0].equalsIgnoreCase("leave")) {
                        new Guild(p.getUniqueId().toString()).leaveGuild();
                    //join
                    }else if(args[0].equalsIgnoreCase("join")) {
                        if(args.length > 1) {
                            if(args.length == 2) {
                                new Guild(p.getUniqueId().toString()).request(args[1]);
                            }else
                                p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Le nom de la guilde ne peut pas contenir d'espace.");
                        }else
                            p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Indique le nom de la guilde.");
                    //newchef
                    }else if(args[0].equalsIgnoreCase("newchef")) {
                        if(args.length > 1)
                            new Tails("Tails" , new Guild(p.getUniqueId().toString()), args[1]).start();
                        else
                            p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Indique le pseudo du joueur.");
                    //setcolor
                    }else if(args[0].equalsIgnoreCase("setcolor")) {

                    //setdescription
                    }else if(args[0].equalsIgnoreCase("setdescription")) {

                    //show
                    }else if(args[0].equalsIgnoreCase("show")) {

                    }else
                        p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Argument invalide.");
                }else
                    p.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.AQUA + "Pour le moment cette commande ne fait rien ;)");
                return true;
            }
        }else
            sender.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RESET + "Seul un joueur peut exécuter cette commande");

        return false;
    }
}
