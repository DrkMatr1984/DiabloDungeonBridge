package org.inesgar.DiabloDungeonBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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
		if (!rand.nextBoolean())
			return;
		if ((rand.nextInt(10) + 1) != 1)
		{
			event.getContents().clear();
		}
		List<CraftItemStack> itemstacks = new ArrayList<CraftItemStack>();
		int size = rand.nextInt(((Chest) event.getBlock().getState())
				.getInventory().getSize());
		for (int i = 0; i < size; i++)
		{
			CraftItemStack cis = null;
			while (cis == null)
				cis = plugin.dd.dropsAPI.getItem();
			itemstacks.add(cis);
		}
		event.getContents().addAll(itemstacks);
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
