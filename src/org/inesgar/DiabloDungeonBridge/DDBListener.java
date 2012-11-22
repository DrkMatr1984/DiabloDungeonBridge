package org.inesgar.DiabloDungeonBridge;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.modcrafting.diablodrops.events.RuinGenerateEvent;
import com.timvisee.DungeonMaze.event.generation.DMGenerationChestEvent;

public class DDBListener implements Listener
{

	private final DiabloDungeonBridge plugin;

	public DDBListener(DiabloDungeonBridge plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onChestGenerate(DMGenerationChestEvent event)
	{
		Random rand = event.getRandom();
		if ((rand.nextInt(10) + 1) != 1)
		{
			event.getContents().clear();
		}
		Block block = event.getBlock();
		Chest chest = ((Chest) block.getState());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
				new DDBTask(chest), 20L * 1);
	}

	@EventHandler
	public void onRuinGenerate(RuinGenerateEvent event)
	{
		if (event.getChunk().getWorld().getGenerator().getClass().getName()
				.equals("com.timvisee.DungeonMaze.DungeonMazeGenerator"))
		{
			event.setCancelled(true);
		}
	}

	public DiabloDungeonBridge getPlugin()
	{
		return plugin;
	}

}
