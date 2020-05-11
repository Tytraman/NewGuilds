package no.tytraman.newguilds;

import no.tytraman.newguilds.guilds.Guild;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    // InventoryClickEvent
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryView inv = e.getView();
        Player p = (Player)e.getWhoClicked();
        if(inv.getTitle().startsWith(ChatColor.WHITE + "Guilde: ")) {
            e.setCancelled(true);
        }
    }

    // PlayerDeathEvent
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player deadP = e.getEntity();
        Player killerP = deadP.getKiller();
        if(killerP != null) {
            Guild guildK = new Guild(killerP.getUniqueId().toString());
            Guild guildD = new Guild(deadP.getUniqueId().toString());
            StringBuilder sb = new StringBuilder();
            if(guildK.isPlayerInGuild()) {
                sb.append(ChatColor.GRAY + "[" + guildK.getGuildColor() + guildK.getGuildName() + ChatColor.GRAY + "] ");
                if(!guildK.isTheUuidInMembersList(deadP.getUniqueId().toString())) {
                    if(NewGuilds.INSTANCE.getConfig().getBoolean("guilds.pvp.lightning-effect"))
                        deadP.getWorld().strikeLightningEffect(deadP.getLocation());
                    guildK.expManager(Guild.ExpType.ADD, NewGuilds.INSTANCE.getConfig().getInt("guilds.exp.kill-player"));
                    guildK.saveGuildData();
                    killerP.sendMessage(guildK.getPrefix() + ChatColor.AQUA + "Guilde: " +
                            ChatColor.GREEN + "+" + NewGuilds.INSTANCE.getConfig().getInt("guilds.exp.kill-player") + " exp");
                }
            }
            sb.append(ChatColor.GRAY + "[" + killerP.getDisplayName() + ChatColor.GRAY + "] " + ChatColor.GOLD + "a tué ");
            if(guildD.isPlayerInGuild())
                sb.append(ChatColor.GRAY + "[" + guildD.getGuildColor() + guildD.getGuildName() + ChatColor.GRAY + "] ");
            sb.append(ChatColor.GRAY + "[" + deadP.getDisplayName() + ChatColor.GRAY + "]");
            if(NewGuilds.INSTANCE.getConfig().getBoolean("guilds.pvp.kill-message"))
                e.setDeathMessage(sb.toString());
        }
    }

    // EntityDeathEvent
    @EventHandler
    public void test(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Player killerP = entity.getKiller();
        if(killerP != null && entity.getType() != EntityType.PLAYER) {
            Guild guild = new Guild(killerP.getUniqueId().toString());
            if(guild.isPlayerInGuild()) {
                guild.expManager(Guild.ExpType.ADD, NewGuilds.INSTANCE.getConfig().getInt("guilds.exp.kill-mob"));
                guild.saveGuildData();
                killerP.sendMessage(guild.getPrefix() + ChatColor.AQUA + "Guilde: " + ChatColor.GREEN + "+" + NewGuilds.INSTANCE.getConfig().getInt("guilds.exp.kill-mob") + " exp");
            }
        }
    }
}
