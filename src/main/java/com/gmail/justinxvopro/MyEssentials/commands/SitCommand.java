package com.gmail.justinxvopro.MyEssentials.commands;

import com.gmail.justinxvopro.MyEssentials.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.UUID;

public class SitCommand implements CommandExecutor, Listener {

    private final Core core;

    public static final String SIT_METADATA_TAG = UUID.randomUUID().toString();

    public SitCommand(final Core core) {
        this.core = core;
        this.core.getServer().getPluginManager().registerEvents(this, this.core);
    }

    private boolean isValidSitLocation(final Player player) {
        var playerLocationBlock = player.getLocation().getBlock();

        return playerLocationBlock.isSolid() || playerLocationBlock.getRelative(BlockFace.DOWN).isSolid();
    }

    private Location getSitLocation(final Player player) {
        // indicates block at their feet
        var playerLocationBlock = player.getLocation().getBlock();
        // block directly underneath their feet
        var playerLocationBlockDown = playerLocationBlock.getRelative(BlockFace.DOWN);

        // if player is standing on a half slab/ standing on stair's ledge
        if (playerLocationBlock.getBlockData() instanceof Stairs ||
                playerLocationBlock.getBlockData() instanceof Slab ||
                playerLocationBlock.getBlockData() instanceof Bed) {

            return playerLocationBlock.getLocation().add(0.5, -0.5, 0.5).setDirection(player.getLocation().getDirection());
        }

        // chest, bed, any other block slightly shorter that is solid...
        if (playerLocationBlock.isSolid()) {
            return playerLocationBlock.getLocation().add(0.5, 0.1, 0.5).setDirection(player.getLocation().getDirection());
        }

        // if player is directly standing on stairs at the top
        if (playerLocationBlockDown.getBlockData() instanceof Stairs) {
            return playerLocationBlockDown.getLocation().add(0.5, -0.5, 0.5).setDirection(player.getLocation().getDirection());
        }

        // otherwise player is sitting on a solid block placed BELOW their feet
        return playerLocationBlockDown.getLocation().add(0.5, 0.1, 0.5).setDirection(player.getLocation().getDirection());
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player player)) return true;

        if (!isValidSitLocation(player)) {
            player.sendMessage("You are not sitting on a solid block!");
            return true;
        }

        player.getWorld().spawnEntity(
                getSitLocation(player),
                EntityType.PIG,
                CreatureSpawnEvent.SpawnReason.COMMAND,
                (entity) -> {
                    final Pig pig = (Pig) entity;
                    pig.setInvisible(true);
                    pig.setInvulnerable(true);
                    pig.setAI(false);
                    pig.setSaddle(true);
                    pig.setMetadata(SIT_METADATA_TAG, new FixedMetadataValue(this.core, ""));

                    pig.addPassenger(player);
                }
        );

        return true;
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getDismounted().hasMetadata(SIT_METADATA_TAG)) {
            event.getDismounted().remove();
            Bukkit.getScheduler().runTaskLater(this.core, () -> event.getEntity().setVelocity(new Vector(0, 0.5, 0)), 1L);
        }
    }

    @EventHandler
    public void onPigDeath(EntityDeathEvent event) {
        if (!event.getEntity().hasMetadata(SIT_METADATA_TAG)) return;

        event.getDrops().clear();
        event.setDroppedExp(0);
    }
}
