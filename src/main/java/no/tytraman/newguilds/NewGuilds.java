package no.tytraman.newguilds;

import org.bukkit.plugin.java.JavaPlugin;

public final class NewGuilds extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("guild").setExecutor(new CommandsManager());
    }
}
