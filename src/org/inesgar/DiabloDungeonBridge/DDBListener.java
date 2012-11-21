package org.inesgar.DiabloDungeonBridge;

import java.util.List;
import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
		Block block = event.getBlock();
		Chest chestB = ((Chest) block.getState());
		Inventory chest = chestB.getBlockInventory();
		List<ItemStack> items = event.getContents();
		for (int i = 0; i < rand.nextInt(chest.getSize()); i++)
		{
			CraftItemStack cis = plugin.dd.dropsAPI.getItem();
			while (cis == null)
				cis = plugin.dd.dropsAPI.getItem();
			items.add(cis);
		}
		event.setContents(items);
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
