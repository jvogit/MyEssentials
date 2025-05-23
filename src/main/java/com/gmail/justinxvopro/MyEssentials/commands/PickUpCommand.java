package com.gmail.justinxvopro.MyEssentials.commands;

import com.gmail.justinxvopro.MyEssentials.Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.UUID;

public class PickUpCommand implements CommandExecutor, Listener {

    private Core core;
    private PassiveCommand passiveCommand;
    private HashSet<UUID> active = new HashSet<UUID>();
    private static final String PICK_UP_METADATA_KEY = UUID.randomUUID().toString();

    public PickUpCommand(Core core, PassiveCommand passiveCommand) {
        this.core = core;
        this.passiveCommand = passiveCommand;
        this.core.getServer().getPluginManager().registerEvents(this, this.core);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (!(cs instanceof Player player)) return true;

        if (active.contains(player.getUniqueId())) {
            active.remove(player.getUniqueId());
            player.sendMessage("Pick up someone is disabled!");

            return true;
        }

        active.add(player.getUniqueId());
        player.sendMessage("Right click to pick up someone!");

        return true;
    }

    @EventHandler
    public void onRightClickEntity(final PlayerInteractEntityEvent event) {
        if (!active.contains(event.getPlayer().getUniqueId())) return;

        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof LivingEntity || event.getRightClicked() instanceof Vehicle) {
            player.setMetadata(PICK_UP_METADATA_KEY, new FixedMetadataValue(core, ""));
            player.addPassenger(event.getRightClicked());
            event.setCancelled(true);
            active.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPassengerDamage(final EntityDamageEvent event) {
        if (event.getEntity().getVehicle() instanceof Player) event.setCancelled(true);
    }

    @EventHandler
    public void onMount(final EntityMountEvent event) {
        if (event.getEntity() instanceof Player player && active.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDismount(final EntityDismountEvent event) {
        if (event.getDismounted() instanceof Player dismountedPlayer && dismountedPlayer.hasMetadata(PICK_UP_METADATA_KEY)) {
            if (event.getEntity() instanceof Player player && !passiveCommand.isInPassiveMode(player)) {
                event.setCancelled(true);
                return;
            }

            dismountedPlayer.removeMetadata(PICK_UP_METADATA_KEY, core);
        }
    }

    private boolean throwPassengers(final Player player) {
        var passengers = player.getPassengers();
        player.removeMetadata(PICK_UP_METADATA_KEY, core);
        var ejected = player.eject();
        if (ejected) {
            passengers.forEach(passenger -> {
                passenger.sendMessage("Yeet");
                passenger.setVelocity(player.getEyeLocation().getDirection().multiply(1));
            });
        }

        return ejected;
    }

    @EventHandler
    public void onThrowInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        if (throwPassengers(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onThrowDamageEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && player.getPassengers().contains(event.getEntity())) {
            if (throwPassengers(player)) event.setCancelled(true);
        }
    }
}
