package org.inesgar.DiabloDungeonBridge;

import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;

public class DDBTask implements Runnable
{

	private Chest chest;

	public DDBTask(Chest chest)
	{
		this.chest = chest;
	}

	@Override
	public void run()
	{
		Inventory chestInv = chest.getBlockInventory();
		for (int i = 0; i < DiabloDungeonBridge.instance.random
				.nextInt(chestInv.getSize()); i++)
		{
			CraftItemStack cis = DiabloDungeonBridge.instance.dd.dropsAPI
					.getItem();
			while (cis == null)
				cis = DiabloDungeonBridge.instance.dd.dropsAPI.getItem();
			chestInv.setItem(i, cis);
		}
	}

}
