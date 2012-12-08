package org.inesgar.DiabloDungeonBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.ChunkGenerator;

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
        Chest chest = (Chest) block.getState();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                new DDBTask(chest), 20L * 1);
    }

    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent event)
    {
        int chance = plugin.dd.config.getInt("Precentages.ChancePerSpawn", 9)
                * DungeonMazeAPI.getDMLevel(event.getLocation().getBlock());
        if (chance >= (plugin.dd.gen.nextInt(100) + 1))
        {
            List<CraftItemStack> items = new ArrayList<CraftItemStack>();
            CraftItemStack ci = plugin.dd.dropsAPI.getItem();
            while (ci == null)
            {
                ci = plugin.dd.dropsAPI.getItem();
            }
            if (plugin.config.getBoolean("Custom.Only", false))
            {
                ci = plugin.dd.custom.get(plugin.dd.gen
                        .nextInt(plugin.dd.custom.size()));
            }
            if (ci != null)
            {
                items.add(ci);
            }
            EntitySpawnWithItemEvent eswi = new EntitySpawnWithItemEvent(
                    event.getEntity(), items);
            plugin.getServer().getPluginManager().callEvent(eswi);
            if (eswi.isCancelled())
                return;
            for (CraftItemStack cis : eswi.getItems())
            {
                setEquipment(cis, event.getEntity());
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

    private void setEquipment(final CraftItemStack ci, final Entity e)
    {
        Material mat = ci.getType();
        EntityLiving ev = ((CraftLivingEntity) e).getHandle();
        if (plugin.dd.drop.isBoots(mat))
        {
            ev.setEquipment(1, ci.getHandle());
        }
        else if (plugin.dd.drop.isChestPlate(mat))
        {
            ev.setEquipment(3, ci.getHandle());
        }
        else if (plugin.dd.drop.isLeggings(mat))
        {
            ev.setEquipment(2, ci.getHandle());
        }
        else if (plugin.dd.drop.isHelmet(mat))
        {
            ev.setEquipment(4, ci.getHandle());
        }
        else
        {
            ev.setEquipment(0, ci.getHandle());
        }
    }

}
