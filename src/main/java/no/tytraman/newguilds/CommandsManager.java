package no.tytraman.newguilds;

import no.tytraman.newguilds.guilds.Guild;
import no.tytraman.newguilds.threads.Knuckles;
import no.tytraman.newguilds.threads.Sonic;
import no.tytraman.newguilds.threads.Tails;
import org.bukkit.Bukkit;
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
                    //infodev
                    }else if(args[0].equalsIgnoreCase("infodev")) {
                        //TODO mettre à jour cette ligne
                        NewGuilds.INSTANCE.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() +
                                " [\"\"," +
                                "{\"text\":\"" + NewGuilds.INSTANCE.getDescription().getName() + "\",\"color\":\"light_purple\"}," +
                                "{\"text\":\"| \",\"color\":\"gray\"}," +
                                "{\"text\":\"Dev par \",\"color\":\"blue\"}," +
                                "{\"text\":\"" + NewGuilds.INSTANCE.getDescription().getAuthors() + "\",\"color\":\"dark_purple\"}," +
                                "{\"text\":\"\\n\"}," +
                                "{\"text\":\"Version \",\"color\":\"yellow\"}," +
                                "{\"text\":\"" + NewGuilds.INSTANCE.getDescription().getVersion() + "\",\"color\":\"gold\"}," +
                                "{\"text\":\"\\n\"}," +
                                "{\"text\":\"Lien \",\"color\":\"green\"}," +
                                "{\"text\":\"Github\",\"italic\":true,\"underlined\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://github.com/Tytraman\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Clic ici !\"}}]");
                    //tp
                    }else if(args[0].equalsIgnoreCase("tp")) {
                        if(guild.isPlayerInGuild()) {
                            if(args.length > 1) {
                                Player target = Bukkit.getPlayer(args[1]);
                                if(target != null) {
                                    if(target != p) {
                                        if(guild.isTheUuidInMembersList(target.getUniqueId().toString())) {
                                            if(p.teleport(target.getLocation())) {
                                                p.sendMessage(guild.getPrefix() + ChatColor.AQUA + "Téléporté(e) vers " + target.getName() + ".");
                                                target.sendMessage(guild.getPrefix() + ChatColor.AQUA + p.getName() + " s'est téléporté(e) sur toi.");
                                            }else
                                                p.sendMessage(guild.getPrefix() + ChatColor.RED + "Une erreur s'est produite.");
                                        }else
                                            p.sendMessage(guild.getPrefix() + ChatColor.RED + target.getName() + " n'est pas dans la guilde.");
                                    }else
                                        p.sendMessage(guild.getPrefix() + ChatColor.RED + "Tu ne peux pas te téléporter à toi-même.");
                                }else
                                    p.sendMessage(guild.getPrefix() + ChatColor.RED + "Le joueur ciblé n'est pas connecté.");
                            }else
                                p.sendMessage(guild.getPrefix() + ChatColor.RED + "Indique le pseudo du joueur.");
                        }else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Tu dois être dans une guilde pour pouvoir faire ça.");
                    //msg
                    }else if(args[0].equalsIgnoreCase("msg")) {
                        if(args.length > 1) {
                            if(guild.isPlayerInGuild()) {
                                StringBuilder sg = new StringBuilder();
                                for(int i = 1; i < args.length; i++) {
                                    sg.append(args[i] + " ");
                                }
                                guild.sendMessageToOnlineMembers(guild.getPrefix() + ChatColor.GRAY + "[" + ChatColor.AQUA + "Guilde" + ChatColor.GRAY + "] " +
                                        "[" + p.getDisplayName() + ChatColor.GRAY + "] " + ChatColor.AQUA + sg.toString().trim());
                            }else
                                p.sendMessage(guild.getPrefix() + ChatColor.RED + "Tu dois être dans une guilde pour pouvoir faire ça.");
                        }else
                            p.sendMessage(guild.getPrefix() + ChatColor.RED + "Ton message ne peut pas être vide.");
                    }else
                        p.sendMessage(guild.getPrefix() + ChatColor.RED + "Argument invalide.");
                }
                else {
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
