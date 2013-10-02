package org.inesgar.DiabloDungeonBridge;

import java.util.Random;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.modcrafting.ddchestregen.DDChestRegen;
import com.modcrafting.diablodrops.DiabloDrops;
import com.timvisee.DungeonMaze.DungeonMaze;

public class DiabloDungeonBridge extends JavaPlugin
{

    public DiabloDrops dd;
    public DungeonMaze dm;
    public DDChestRegen ddcr;
    public Random random;
    public NamesLoader namesLoader;
    public FileConfiguration config;

    public static DiabloDungeonBridge instance;

    private DDChestRegen checkDDChestRegen()
    {
        Plugin plugin = getServer().getPluginManager()
                .getPlugin("DDChestRegen");

        if ((plugin == null) || !(plugin instanceof DDChestRegen))
            return null;

        return (DDChestRegen) plugin;
    }

    private DiabloDrops checkDiabloDrops()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("DiabloDrops");

        if ((plugin == null) || !(plugin instanceof DiabloDrops))
            return null;

        return (DiabloDrops) plugin;
    }

    private DungeonMaze checkDungeonMaze()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("DungeonMaze");

        if ((plugin == null) || !(plugin instanceof DungeonMaze))
            return null;

        return (DungeonMaze) plugin;
    }

    private void log(final String message)
    {
        getServer().getLogger().log(Level.INFO,
                "[DiabloDungeonBridge] " + message);
    }

    @Override
    public void onDisable()
    {
        log("Disabled.");
    }

    @Override
    public void onEnable()
    {
        instance = this;
        dd = checkDiabloDrops();
        dm = checkDungeonMaze();
        if (dd == null)
        {
            log("DiabloDrops was not found. Plugin is being disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (dm == null)
        {
            log("DungeonMaze was not found. Plugin is being disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        ddcr = checkDDChestRegen();
        if (ddcr == null)
        {
            log("DDChestRegen was not found - chests will not automatically regenerate.");
        }
        getDataFolder().mkdir();
        namesLoader = new NamesLoader(instance);
        namesLoader.writeDefault("config.yml");
        config = getConfig();
        random = new Random();
        getServer().getPluginManager().registerEvents(new DDBListener(this),
                this);
    }

}
