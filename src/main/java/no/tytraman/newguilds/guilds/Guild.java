package no.tytraman.newguilds.guilds;

import com.sun.istack.internal.NotNull;
import no.tytraman.newguilds.NewGuilds;
import no.tytraman.newguilds.threads.Amy;
import no.tytraman.newguilds.threads.Sonic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Guild {
    private String uuid;
    private File playerFile;
    private YamlConfiguration playerYml;
    private File guildFile;
    private YamlConfiguration guildYml;
    private Player player;

    public Guild(String playerUuid) {
        this.uuid = playerUuid;
        this.playerFile = new File(getPlayerPath() + uuid + ".yml");
        this.playerYml = YamlConfiguration.loadConfiguration(playerFile);
        defaultPlayer();
        savePlayerData();
        this.guildFile = new File(getGuildPath() + "g." + playerYml.getString("guild-name").toLowerCase() + ".yml");
        this.guildYml = YamlConfiguration.loadConfiguration(guildFile);
        this.player = Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public void createNewGuild(String guildName) {
        if(!isPlayerInGuild()) {
            Pattern p = Pattern.compile("\\W\\.-");
            Matcher m = p.matcher(guildName);
            if(!m.find()) {
                if(guildName.length() >= NewGuilds.INSTANCE.getConfig().getInt("guilds.min-length-name") && guildName.length() <= NewGuilds.INSTANCE.getConfig().getInt("guilds.max-length-name")) {
                    guildFile = new File(getGuildPath() + "g." + guildName.toLowerCase() + ".yml");
                    guildYml = YamlConfiguration.loadConfiguration(guildFile);
                    if(!guildFile.exists()) {
                        long now = new Date().getTime();
                        guildYml.set("infos.name", guildName);
                        guildYml.set("infos.creator", uuid);
                        guildYml.set("infos.creation-time", now);
                        guildYml.set("infos.owner", uuid);
                        guildYml.set("infos.color", Things.getRandomColor().toString());
                        guildYml.set("infos.description", "");
                        guildYml.set("guild-points", 0);
                        guildYml.set("exp", 0);
                        addMember(uuid);
                        guildYml.set("requests", new ArrayList<>());
                        saveGuildData();
                        updatePlayerData(true, false, guildName);
                        savePlayerData();
                        sendMessageToPlayer(getPrefix() + ChatColor.AQUA + "La guilde \"" + guildName + "\" a bien été créé.");
                    }else
                        sendMessageToPlayer(getPrefix() + ChatColor.RED + "La guilde \"" + guildName + "\" existe déjà.");
                }else
                    sendMessageToPlayer(getPrefix() + ChatColor.RED + "Le nom de la guilde doit avoir entre " + NewGuilds.INSTANCE.getConfig().getInt("guilds.min-length-name") + " et " + NewGuilds.INSTANCE.getConfig().getInt("guilds.max-length-name") + " caractères.");
            }else
                sendMessageToPlayer(getPrefix() + ChatColor.RED + "Le nom de la guilde contient des caractères non autorisés.");
        }else
            sendMessageToPlayer(getPrefix() + ChatColor.RED + "Tu es déjà dans une guilde.");
    }

    public void leaveGuild() {
        if(isPlayerInGuild()) {
            guildYml.set("members." + uuid, null);
            saveGuildData();
            updatePlayerData(false, false, "");
            savePlayerData();
            sendMessageToPlayer(getPrefix() + ChatColor.BLUE + "Tu as quitté la guilde \"" + getGuildName() + "\".");
            new Sonic("Sonic", this, uuid, "{player} vient de quitter la guilde.").start();
            if(getNumberOfMembers() > 0) {
                if(isPlayerTheChef()) {
                    List joiningTime = new ArrayList();
                    for(String str : getMembersList()) {
                        joiningTime.add(guildYml.getLong("members." + str + ".infos.joining-time"));
                    }
                    int indexOfNewOwner = joiningTime.indexOf((long) Collections.min(joiningTime));
                    String tempUuid = Things.convertSetToList(getMembersList()).get(indexOfNewOwner);
                    setNewOwner(tempUuid);
                    saveGuildData();
                    new Sonic("Sonic", this, tempUuid, "{player} est devenu le chef de guilde.").start();
                }
            }else {
                setNewOwner("555-2368");
                saveGuildData();
            }
        }else
            sendMessageToPlayer(getPrefix() + ChatColor.RED + "Tu dois être dans une guilde pour pouvoir la quitter.");
    }

    public void setNewOwner(String uuid) {
        guildYml.set("infos.owner", uuid);
    }

    public void addMember(String uuid) {
        guildYml.set("members." + uuid + ".infos.joining-time", new Date().getTime());
        guildYml.set("members." + uuid + ".points", 0);
    }

    public void acceptMember(String playerUsername) {
        if(isPlayerInGuild()) {
            if(isPlayerTheChef()) {
                new Amy("Amy", this, playerUsername).start();
            }else
                sendMessageToPlayer(getPrefix() + ChatColor.RED + "Seul le chef est autorisé à faire ça.");
        }else
            sendMessageToPlayer(getPrefix() + ChatColor.RED + "Tu dois être le chef d'une guilde pour pouvoir faire ça.");
    }

    public void request(String guildName) {
        if(!isPlayerInGuild()) {
            guildFile = new File(getGuildPath() + "g." + guildName.toLowerCase() + ".yml");
            guildYml = YamlConfiguration.loadConfiguration(guildFile);
            if(guildFile.exists()) {
                try {
                    if(playerYml.getConfigurationSection("requested-to").getKeys(false).contains(guildName.toLowerCase())) {
                        sendMessageToPlayer(getPrefix() + ChatColor.RED + "Tu as déjà envoyé une requête à cette guilde.");
                        return;
                    }
                }catch(NullPointerException e) {}
                long now = new Date().getTime();
                guildYml.set("requests." + uuid + ".time-of-request", now);
                saveGuildData();
                playerYml.set("requested-to." + guildName.toLowerCase() + ".time-of-request", now);
                savePlayerData();
                sendMessageToPlayer(getPrefix() + ChatColor.BLUE + "Tu as envoyé une requête à la guilde \"" + guildName + "\", attends que le chef accepte.");
                new Sonic("Sonic", this, uuid, "{player} demande à rejoindre la guilde.").start();
            }else
                sendMessageToPlayer(getPrefix() + ChatColor.RED + "La guilde \"" + guildName + "\" n'existe pas.");
        }else
            sendMessageToPlayer(getPrefix() + ChatColor.RED + "Tu es déjà dans une guilde.");
    }

    public void removeRequest(String uuid) {
        guildYml.set("requests." + uuid, null);
    }

    public void sendMessageToOnlineMembers(String message) {
        Player member;
        for(String uuidMember : guildYml.getConfigurationSection("members").getKeys(false)) {
            member = Bukkit.getPlayer(UUID.fromString(uuidMember));
            if(member != null)
                member.sendMessage(message);
        }
    }

    public void updatePlayerData(boolean owner, boolean member, @NotNull String guildName) {
        playerYml.set("owner", owner);
        playerYml.set("member", member);
        playerYml.set("guild-name", guildName);
    }

    public void sendMessageToPlayer(String message) {
        if(player != null)
            player.sendMessage(message);
    }

    public void savePlayerData() {
        try {
            playerYml.save(playerFile);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGuildData() {
        try {
            guildYml.save(guildFile);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void clearRequestedList() {
        playerYml.set("requested-to", new ArrayList<>());
    }

    public boolean isUuidInRequestList(String uuid) {
        try {
            if(guildYml.getConfigurationSection("requests").getKeys(false).contains(uuid))
                return true;
        }catch(NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPlayerInGuild() {
        if(playerYml.getBoolean("owner") == true || playerYml.getBoolean("member") == true)
            return true;
        else
            return false;
    }

    private void defaultPlayer() {
        playerYml.addDefault("owner", false);
        playerYml.addDefault("member", false);
        playerYml.addDefault("guild-name", "");
        playerYml.addDefault("requested-to", new ArrayList<>());
        playerYml.options().copyDefaults(true);
    }

    public boolean isPlayerTheChef() {
        return uuid.equalsIgnoreCase(guildYml.getString("infos.owner"));
    }

    public boolean isTheUuidInMembersList(String uuid) {
        return getMembersList().contains(uuid);
    }

    // -------------Getters-------------
    private String getPlayerPath() {
        return NewGuilds.INSTANCE.getDataFolder() + "/guilds/playersdata/";
    }

    private String getGuildPath() {
        return NewGuilds.INSTANCE.getDataFolder() + "/guilds/guildsdata/";
    }

    public String getGuildName() { return guildYml.getString("infos.name");
    }

    public String getGuildCreator() {
        return guildYml.getString("infos.creator");
    }

    public long getGuildCreationTime() {
        return guildYml.getLong("infos.creation-time");
    }

    public String getGuildOwner() {
        return guildYml.getString("infos.owner");
    }

    public String getGuildColor() {
        return guildYml.getString("infos.color");
    }

    public String getGuildDescription() {
        return guildYml.getString("infos.description");
    }

    public int getGuildPoints() {
        return guildYml.getInt("guild-points");
    }

    public int getGuildExp() {
        return guildYml.getInt("exp");
    }

    public int getNumberOfMembers() {
        return guildYml.getConfigurationSection("members").getKeys(false).size();
    }

    public int getNumberOfRequests() {
        return guildYml.getConfigurationSection("requests").getKeys(false).size();
    }

    public Set<String> getMembersList() {
        return guildYml.getConfigurationSection("members").getKeys(false);
    }

    public String getPrefix() {
        return NewGuilds.INSTANCE.getConfig().getString("infos.plugin-prefix");
    }

    public Player getPlayer() {
        return player;
    }
}
