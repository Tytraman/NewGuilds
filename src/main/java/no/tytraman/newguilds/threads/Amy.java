package no.tytraman.newguilds.threads;

import no.tytraman.newguilds.GsonManager;
import no.tytraman.newguilds.NewGuilds;
import no.tytraman.newguilds.guilds.Guild;
import org.bukkit.ChatColor;

public class Amy extends Thread {

    private Guild guild;
    private String username;

    //Accepte un membre dans une guilde, ne pas utiliser n'importe comment
    public Amy(String threadName, Guild guild, String username) {
        super(threadName);
        this.guild = guild;
        this.username = username;
    }

    @Override
    public void run() {
        GsonManager gson = new GsonManager();
        String uuid = gson.getUuidFromUsername(username);
        if(uuid != null) {
            uuid = uuid.replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5");
            if(guild.isUuidInRequestList(uuid)) {
                guild.removeRequest(uuid);
                guild.addMember(uuid);
                guild.saveGuildData();
                Guild tempGuild = new Guild(uuid);
                tempGuild.updatePlayerData(false, true, guild.getGuildName());
                tempGuild.clearRequestedList();
                tempGuild.savePlayerData();
                guild.sendMessageToOnlineMembers(guild.getPrefix() + ChatColor.GREEN + gson.getUsernameFromUuid(uuid) + " a rejoint la guilde");
            }else
                guild.sendMessageToPlayer(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Ce joueur n'a pas envoyé de requête.");
        }else
            guild.sendMessageToPlayer(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RED + "Le joueur ciblé n'existe pas.");
    }
}
