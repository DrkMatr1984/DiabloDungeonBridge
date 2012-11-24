package org.inesgar.DiabloDungeonBridge;

import java.util.Random;
import java.util.logging.Level;

import net.h31ix.updater.Updater;
import net.h31ix.updater.Updater.UpdateResult;
import net.h31ix.updater.Updater.UpdateType;

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
		this.getServer().getScheduler()
				.scheduleAsyncDelayedTask(this, new Runnable()
				{

					@Override
					public void run()
					{
						if (config.getBoolean("Plugin.AutoUpdate", true))
						{
							Updater up = new Updater(
									DiabloDungeonBridge.instance,
									DiabloDungeonBridge.instance
											.getDescription().getName()
											.toLowerCase(), getFile(),
									UpdateType.DEFAULT, true);
							if (!up.getResult().equals(UpdateResult.SUCCESS)
									|| up.pluginFile(getFile().getName()))
							{
								if (up.getResult().equals(
										UpdateResult.FAIL_NOVERSION))
								{
									getLogger()
											.info("Unable to connect to dev.bukkit.org.");
								}
								else
								{
									getLogger()
											.info("No updates found on dev.bukkit.org.");
								}
							}
							else
							{
								getLogger()
										.info("Update "
												+ up.getLatestVersionString()
												+ " found and downloaded, please restart your server.");
							}
						}

					}

				});
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
