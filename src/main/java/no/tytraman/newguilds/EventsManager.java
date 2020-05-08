package no.tytraman.newguilds;

import no.tytraman.newguilds.guilds.Guild;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventsManager implements Listener {


    // AsyncPlayerChatEvent
    @EventHandler (priority = EventPriority.LOW)
    public void onChatMessage(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Guild guild = new Guild(player.getUniqueId().toString());
        //Chat format
        if(guild.isPlayerInGuild())
            e.setFormat(ChatColor.GRAY + "[" + guild.getGuildColor() + guild.getGuildName() + ChatColor.GRAY + "] " +
                    "[" + player.getDisplayName() + ChatColor.GRAY + "] " + ChatColor.WHITE + "%2$s");
        else
            e.setFormat(ChatColor.GRAY + "[" + player.getDisplayName() + ChatColor.GRAY + "] " + ChatColor.WHITE + "%2$s");
    }

}
