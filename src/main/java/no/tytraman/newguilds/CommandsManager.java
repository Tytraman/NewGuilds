package no.tytraman.newguilds;

import no.tytraman.newguilds.guilds.Guild;
import no.tytraman.newguilds.threads.Knuckles;
import no.tytraman.newguilds.threads.Sonic;
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
                Guild guild = new Guild(p.getUniqueId().toString());
                if(args.length > 0) {
                    //create
                    if(args[0].equalsIgnoreCase("create")) {
                        if(args.length > 1) {
                            if(args.length == 2) {
                                guild.createNewGuild(args[1]);
                            }else
                                p.sendMessage(guild.getPrefix() + ChatColor.RED + "Le nom de la guilde ne peut pas contenir d'espace.");
                        }else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Indique le nom de la guilde.");
                    //accept
                    }else if(args[0].equalsIgnoreCase("accept")) {
                        if(args.length > 1) {
                            guild.acceptMember(args[1]);
                        }else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Indique le pseudo du joueur.");
                    //leave
                    }else if(args[0].equalsIgnoreCase("leave")) {
                        guild.leaveGuild();
                    //join
                    }else if(args[0].equalsIgnoreCase("join")) {
                        if(args.length > 1) {
                            if(args.length == 2) {
                                guild.request(args[1]);
                            }else
                                p.sendMessage(guild.getPrefix() + ChatColor.RED + "Le nom de la guilde ne peut pas contenir d'espace.");
                        }else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Indique le nom de la guilde.");
                    //newchef
                    }else if(args[0].equalsIgnoreCase("newchef")) {
                        if(args.length > 1)
                            new Tails("Tails" , guild, args[1]).start();
                        else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Indique le pseudo du joueur.");
                    //setcolor
                    }else if(args[0].equalsIgnoreCase("setcolor")) {
                        if(args.length > 1) {
                            if(guild.isPlayerInGuild()) {
                                if(guild.isPlayerTheChef()) {
                                    if(guild.setColor(args[1])) {
                                        guild.saveGuildData();
                                        new Sonic("Sonic", guild, p.getUniqueId().toString(), ChatColor.AQUA + "{player} a mis à jour la couleur de la guilde.").start();
                                    }else
                                        p.sendMessage(guild.getPrefix() + ChatColor.RED + "La couleur est invalide.");
                                }else
                                    p.sendMessage(guild.getPrefix() + ChatColor.RED + "Seul le chef est autorisé à faire ça.");
                            }else
                                p.sendMessage(guild.getPrefix() + ChatColor.RED + "Tu dois être le chef d'une guilde pour pouvoir faire ça.");
                        }else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Indique la couleur.");
                    //setdescription
                    }else if(args[0].equalsIgnoreCase("setdescription")) {
                        if(guild.isPlayerInGuild()) {
                            if(guild.isPlayerTheChef()){
                                StringBuilder sg = new StringBuilder();
                                for(int i = 1; i < args.length; i++) {
                                    sg.append(args[i] + " ");
                                }
                                if(sg.toString().trim().length() <= 75) {
                                    guild.setDescription(sg.toString().trim());
                                    guild.saveGuildData();
                                    new Sonic("Sonic", guild, p.getUniqueId().toString(), ChatColor.AQUA + "{player} a mis à jour la description de la guilde.").start();
                                }else
                                    p.sendMessage(guild.getPrefix() + ChatColor.RED + "La longueur maximum de la description est de 75 caractères.");
                            }else
                                p.sendMessage(guild.getPrefix() + ChatColor.RED + "Seul le chef est autorisé à faire ça.");
                        }else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Tu dois être le chef d'une guilde pour pouvoir faire ça.");
                    //show
                    }else if(args[0].equalsIgnoreCase("show")) {
                        if(args.length == 2)
                            new Knuckles("Knuckles", p.getUniqueId().toString(), args[1]).start();
                        else if(args.length > 2)
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Le nom de la guilde ne peut pas contenir d'espace.");
                        else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Indique le nom de la guilde.");
                    }else
                        p.sendMessage(guild.getPrefix() + ChatColor.RED + "Argument invalide.");
                }else {
                    if(guild.isPlayerInGuild())
                        new Knuckles("Knuckles", guild).start();
                    else
                        p.sendMessage(guild.getPrefix() + ChatColor.RED + "Tu dois être dans une guilde pour pouvoir faire ça.");
                }

                return true;
            }
        }else
            sender.sendMessage(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RESET + "Seul un joueur peut exécuter cette commande");

        return false;
    }
}
