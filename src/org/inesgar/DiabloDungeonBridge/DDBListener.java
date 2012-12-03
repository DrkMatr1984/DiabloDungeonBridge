package org.inesgar.DiabloDungeonBridge;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;

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
        int chance = plugin.getConfig().getInt("ChestFill.Chance", 500);
        Random rand = event.getRandom();
        if ((rand.nextInt(1000) + 1) > chance)
            return;
        if ((rand.nextInt(10) + 1) != 1)
        {
            event.getContents().clear();
        }
        Block block = event.getBlock();
        Chest chest = ((Chest) block.getState());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                new DDBTask(chest), 20L * 1);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRuinGenerate(RuinGenerateEvent event)
    {
        Chunk chunk = event.getChunk();
        if (chunk == null)
            return;
        World world = chunk.getWorld();
        if (world == null)
            return;
        ChunkGenerator generator = world.getGenerator();
        if (generator == null)
            return;
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
