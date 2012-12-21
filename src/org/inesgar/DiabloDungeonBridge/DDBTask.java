package org.inesgar.DiabloDungeonBridge;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DDBTask implements Runnable
{

    private final Chest chest;

    public DDBTask(final Chest chest)
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
            ItemStack cis = DiabloDungeonBridge.instance.dd.dropsAPI.getItem();
            while (cis == null)
            {
                cis = DiabloDungeonBridge.instance.dd.dropsAPI.getItem();
            }
            chestInv.setItem(i, cis);
        }
    }

}
