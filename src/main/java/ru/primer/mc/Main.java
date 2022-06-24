package ru.primer.mc;

import org.bukkit.plugin.java.JavaPlugin;
import ru.primer.mc.Commands.CommandManager;

public final class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        getCommand("primeitems").setExecutor(new CommandManager());

    }

    @Override
    public void onDisable() {

    }
}
