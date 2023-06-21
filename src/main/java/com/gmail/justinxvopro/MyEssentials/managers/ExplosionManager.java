package com.gmail.justinxvopro.MyEssentials.managers;

import com.gmail.justinxvopro.MyEssentials.Core;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class ExplosionManager implements Listener {

    private final Core core;

    public ExplosionManager(final Core core) {
        this.core = core;
        this.core.getServer().getPluginManager().registerEvents(this, this.core);
    }

    private void handleExplosionPhysics(final List<Block> blockList, final Location origin) {
        final var world = origin.getWorld();
        blockList.stream()
                .filter(block -> block.getType() != Material.TNT)
                .forEach(block -> {
                    var fallingBlock = world.spawnFallingBlock(block.getLocation(), block.getBlockData());
                    var dir = block.getLocation().toVector().subtract(origin.toVector()).normalize();
                    fallingBlock.setVelocity(dir.multiply(1.5f));
                });
    }

    @EventHandler
    public void onBlockExplosion(final BlockExplodeEvent event) {
        handleExplosionPhysics(event.blockList(), event.getBlock().getLocation());
        event.setYield(0f);
    }

    @EventHandler
    public void onEntityExplosion(final EntityExplodeEvent event) {
        handleExplosionPhysics(event.blockList(), event.getLocation());
        event.setYield(0f);
    }
}
