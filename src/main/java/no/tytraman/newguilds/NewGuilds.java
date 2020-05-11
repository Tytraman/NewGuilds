package no.tytraman.newguilds;

import no.tytraman.newguilds.tabscompleter.GuildPressTab;
import org.bukkit.plugin.java.JavaPlugin;


public final class NewGuilds extends JavaPlugin {

    public static NewGuilds INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();

        // Configuration des commandes
        getCommand("guild").setExecutor(new CommandsManager());

        // Configuration des tabs completer
        getCommand("guild").setTabCompleter(new GuildPressTab());

        // Configuration des events
        getServer().getPluginManager().registerEvents(new EventsManager(), this);
    }
}
