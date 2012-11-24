package org.inesgar.DiabloDungeonBridge;

import java.util.Random;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.modcrafting.diablodrops.DiabloDrops;
import com.timvisee.DungeonMaze.DungeonMaze;

public class DiabloDungeonBridge extends JavaPlugin
{

	public DiabloDrops dd;
	public DungeonMaze dm;
	public Random random;
	public NamesLoader namesLoader;
	public FileConfiguration config;

	public static DiabloDungeonBridge instance;

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
		this.getDataFolder().mkdir();
		namesLoader = new NamesLoader(instance);
		namesLoader.writeDefault("config.yml");
		config = this.getConfig();
		random = new Random();
		getServer().getPluginManager().registerEvents(new DDBListener(this),
				this);
	}

	@Override
	public void onDisable()
	{
		log("Disabled.");
	}

	private void log(String message)
	{
		getServer().getLogger().log(Level.INFO,
				"[DiabloDungeonBridge] " + message);
	}

	private DungeonMaze checkDungeonMaze()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("DungeonMaze");

		if (plugin == null || !(plugin instanceof DungeonMaze))
		{
			return null;
		}

		return (DungeonMaze) plugin;
	}

	private DiabloDrops checkDiabloDrops()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("DiabloDrops");

		if (plugin == null || !(plugin instanceof DiabloDrops))
		{
			return null;
		}

		return (DiabloDrops) plugin;
	}

}
