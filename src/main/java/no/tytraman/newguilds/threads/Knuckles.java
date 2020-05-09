package no.tytraman.newguilds.threads;

import no.tytraman.newguilds.GsonManager;
import no.tytraman.newguilds.NewGuilds;
import no.tytraman.newguilds.guilds.Guild;
import no.tytraman.newguilds.guilds.Things;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Knuckles extends Thread {

    private Guild guild;

    public Knuckles(String threadName, Guild guild) {
        super(threadName);
        this.guild = guild;
    }

    public Knuckles(String threadName,String uuid, String guildName) {
        super(threadName);
        this.guild = new Guild(uuid, guildName);
    }

    @Override
    public void run() {
        if(guild.getGuildFile().exists()) {
            GsonManager gson = new GsonManager();
            String ownerName = gson.getUsernameFromUuid(guild.getGuildOwner());
            if(ownerName == null)
                ownerName = guild.getGuildOwner();
            Inventory inv = Bukkit.createInventory(null, 27, ChatColor.WHITE + "Guilde: " + guild.getGuildColor() + guild.getGuildName());
            inv.setItem(0, Things.createItemForGui(Material.PAPER, ChatColor.YELLOW + "Informations de la guilde",
                    new String[]{
                            ChatColor.WHITE + "Nom: " + ChatColor.GRAY + guild.getGuildName(),
                            ChatColor.WHITE + "Description: " + ChatColor.GRAY + guild.getGuildDescription(),
                            ChatColor.WHITE + "Chef: " + ChatColor.GRAY + ownerName,
                            ChatColor.WHITE + "Membres: " + ChatColor.GRAY + guild.getNumberOfMembers() + "/" + NewGuilds.INSTANCE.getConfig().getInt("guilds.max-members"),
                            ChatColor.WHITE + "Niveau: " + ChatColor.GRAY + Things.convertExpToLevel(guild.getGuildExp()),
                            ChatColor.WHITE + "Exp: " + ChatColor.GRAY + guild.getGuildExp() + "/" + guild.getGuildExpRequired(),
                            ChatColor.WHITE + "Date de création: " + ChatColor.GRAY + Things.getLisibleTimeFromMillis(guild.getGuildCreationTime())
                    }));
            new BukkitRunnable() {
                @Override
                public void run() {
                    guild.getPlayer().openInventory(inv);
                }
            }.runTask(NewGuilds.INSTANCE);
        }else
            guild.sendMessageToPlayer(guild.getPrefix() + ChatColor.RED + "La guilde n'existe pas.");
    }
}
