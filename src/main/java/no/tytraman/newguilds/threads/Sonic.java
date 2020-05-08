package no.tytraman.newguilds.threads;

import no.tytraman.newguilds.GsonManager;
import no.tytraman.newguilds.NewGuilds;
import no.tytraman.newguilds.guilds.Guild;
import org.bukkit.ChatColor;



public class Sonic extends Thread {

    private Guild guild;
    private String uuid;
    private String message;

    // Envoie un message à tous les membres connectés d'une guilde en convertissant un uuid en username
    public Sonic(String threadName, Guild guild, String uuid, String message) {
        super(threadName);
        this.guild = guild;
        this.uuid = uuid;
        this.message = message;
    }

    @Override
    public void run() {
        GsonManager gson = new GsonManager();
        String username = gson.getUsernameFromUuid(uuid);
        if(username == null)
            username = ".error.";
        guild.sendMessageToOnlineMembers(NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix") + ChatColor.RESET + message.replace("{player}", username));
    }
}
