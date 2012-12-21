package org.inesgar.DiabloDungeonBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;

import com.modcrafting.diablodrops.events.EntitySpawnWithItemEvent;
import com.modcrafting.diablodrops.events.RuinGenerateEvent;
import com.timvisee.DungeonMaze.API.DungeonMazeAPI;
import com.timvisee.DungeonMaze.event.generation.DMGenerationChestEvent;

public class DDBListener implements Listener
{

    private final DiabloDungeonBridge plugin;

    public DDBListener(final DiabloDungeonBridge plugin)
    {
        this.plugin = plugin;
    }

    public DiabloDungeonBridge getPlugin()
    {
        return plugin;
    }

    @EventHandler
    public void onChestGenerate(final DMGenerationChestEvent event)
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
        if ((plugin.ddcr != null) && plugin.ddcr.isEnabled())
        {
            plugin.ddcr.blocks.put(event.getBlock(), new ArrayList<String>());
        }
        Chest chest = (Chest) block.getState();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                new DDBTask(chest), 20L * 1);
    }

    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent event)
    {
        LivingEntity entity = event.getEntity();
        int chance = plugin.dd.config.getInt("Precentages.ChancePerSpawn", 9)
                * DungeonMazeAPI.getDMLevel(event.getLocation().getBlock());
        if (chance >= (plugin.dd.gen.nextInt(100) + 1))
        {
            List<ItemStack> items = new ArrayList<ItemStack>();
            for (int i = 0; i < (plugin.dd.gen.nextInt(5) + 1); i++)
            {
                ItemStack ci = plugin.dd.dropsAPI.getItem();
                while (ci == null)
                {
                    ci = plugin.dd.dropsAPI.getItem();
                }
                if (plugin.config.getBoolean("Custom.Only", false)
                        && plugin.config.getBoolean("Custom.Enabled", true))
                {
                    ci = plugin.dd.custom.get(plugin.dd.gen
                            .nextInt(plugin.dd.custom.size()));
                }
                if (ci != null)
                {
                    items.add(ci);
                }
            }
            EntitySpawnWithItemEvent eswi = new EntitySpawnWithItemEvent(
                    entity, items);
            plugin.getServer().getPluginManager().callEvent(eswi);
            if (eswi.isCancelled())
                return;

            for (ItemStack cis : eswi.getItems())
                if (cis != null)
                {
                    if (plugin.dd.drop.isHelmet(cis.getType()))
                    {
                        entity.getEquipment().setHelmet(cis);
                        entity.getEquipment().setHelmetDropChance(2.0F);
                    }
                    else if (plugin.dd.drop.isChestPlate(cis.getType()))
                    {
                        entity.getEquipment().setChestplate(cis);
                        entity.getEquipment().setChestplateDropChance(2.0F);
                    }
                    else if (plugin.dd.drop.isLeggings(cis.getType()))
                    {
                        entity.getEquipment().setLeggings(cis);
                        entity.getEquipment().setLeggingsDropChance(2.0F);
                    }
                    else if (plugin.dd.drop.isBoots(cis.getType()))
                    {
                        entity.getEquipment().setBoots(cis);
                        entity.getEquipment().setLeggingsDropChance(2.0F);
                    }
                    else
                    {
                        entity.getEquipment().setItemInHand(cis);
                        entity.getEquipment().setItemInHandDropChance(2.0F);
                    }
                }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRuinGenerate(final RuinGenerateEvent event)
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

}
