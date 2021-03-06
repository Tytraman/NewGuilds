package no.tytraman.newguilds.threads;

import no.tytraman.newguilds.GsonManager;
import no.tytraman.newguilds.guilds.Guild;
import org.bukkit.ChatColor;

public class Tails extends Thread {
    private Guild guild;
    private String username;

    //Change l'owner d'une guilde, ne pas utiliser n'importe comment
    public Tails(String threadName, Guild guild, String username) {
        super(threadName);
        this.guild = guild;
        this.username = username;
    }

    @Override
    public void run() {
        if(guild.isPlayerInGuild()) {
            if(guild.isPlayerTheChef()) {
                if(!username.equalsIgnoreCase(guild.getPlayer().getName())) {
                    GsonManager gson = new GsonManager();
                    String uuid = gson.getUuidFromUsername(username);
                    if(uuid != null) {
                        uuid = uuid.replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5");
                        if(guild.isTheUuidInMembersList(uuid)) {
                            guild.setNewOwner(uuid);
                            guild.saveGuildData();
                            guild.updatePlayerData(false, true, guild.getGuildName());
                            guild.savePlayerData();
                            Guild tempGuild = new Guild(uuid);
                            tempGuild.updatePlayerData(true, false, guild.getGuildName());
                            tempGuild.savePlayerData();
                            guild.sendMessageToOnlineMembers(guild.getPrefix() + ChatColor.AQUA + guild.getPlayer().getName() + " a passé son statut de chef à " + gson.getUsernameFromUuid(uuid) + ".");
                        }else
                            guild.sendMessageToPlayer(guild.getPrefix() + ChatColor.RED + "Le joueur ciblé n'est pas dans la guilde.");
                    }else
                        guild.sendMessageToPlayer(guild.getPrefix() + ChatColor.RED + "Le joueur ciblé n'existe pas.");
                }else
                    guild.sendMessageToPlayer(guild.getPrefix() + ChatColor.RED + "Tu es déjà le chef de la guilde.");
            }else
                guild.sendMessageToPlayer(guild.getPrefix() + ChatColor.RED + "Seul le chef est autorisé à faire ça.");

        }else
            guild.sendMessageToPlayer(guild.getPrefix() + ChatColor.RED + "Tu dois être le chef d'une guilde pour pouvoir faire ça.");
    }
}
