package no.tytraman.newguilds;

import no.tytraman.newguilds.guilds.Guild;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.InventoryView;

public class EventsManager implements Listener {


    // AsyncPlayerChatEvent
    @EventHandler (priority = EventPriority.LOW)
    public void onChatMessage(AsyncPlayerChatEvent e) {
        if(!NewGuilds.INSTANCE.getConfig().getBoolean("chat.disable-format")) {
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryView inv = e.getView();
        Player p = (Player)e.getWhoClicked();
        if(inv.getTitle().startsWith(ChatColor.WHITE + "Guilde: ")) {
            e.setCancelled(true);
        }
    }
}
