package com.gmail.justinxvopro.MyEssentials.commands;

import com.gmail.justinxvopro.MyEssentials.Core;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.kyori.adventure.text.Component.text;

public class OpTridentCommand implements CommandExecutor, Listener {

    private Core core;

    public OpTridentCommand(Core core) {
        this.core = core;
        this.LIGHTNING_LOYALTY = buildLightningLoyalty();
        this.RIPTIDE_RIDE = buildRiptideRide();

        this.core.getServer().getPluginManager().registerEvents(this, this.core);
    }

    private final ItemStack LIGHTNING_LOYALTY;
    private final ItemStack RIPTIDE_RIDE;

    private ItemStack buildLightningLoyalty() {
        ItemStack trident = new ItemStack(Material.TRIDENT, 1);
        trident.addEnchantment(Enchantment.LOYALTY, 1);
        ItemMeta meta = trident.getItemMeta();

        meta.displayName(text("Lightning Loyalty"));
        meta.lore(Arrays.asList(
                text("When the trident strikes, ")
                        .append(text("LIGHTNING", NamedTextColor.GOLD))
                        .append(text(" strikes as well.")),
                text("You are teleported to the trident's strike position as well.")
        ));

        trident.setItemMeta(meta);

        return trident;
    }

    private ItemStack buildRiptideRide() {
        ItemStack trident = new ItemStack(Material.TRIDENT, 1);
        trident.addEnchantment(Enchantment.LOYALTY, 1);
        ItemMeta meta = trident.getItemMeta();

        meta.displayName(text("Riptide Ride"));
        meta.lore(Arrays.asList(
                text("When the trident is thrown you ride the trident as well.")
        ));

        trident.setItemMeta(meta);

        return trident;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length < 1) {
            player.sendMessage("Please specify trident: ll, rr");
            return true;
        }

        if (args[0].equals("ll")) {
            player.getInventory().addItem(this.LIGHTNING_LOYALTY);
            player.sendMessage("You have received lightning loyalty trident.");
        } else if (args[0].equals("rr")) {
            player.getInventory().addItem(this.RIPTIDE_RIDE);
            player.sendMessage("You have received riptide ride trident.");
        }

        return true;
    }

    @EventHandler
    public void onTridentThrow(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Trident trident && trident.getItemStack().equals(this.RIPTIDE_RIDE)
            && trident.getShooter() instanceof Player player)) return;

        trident.addPassenger(player);
    }

    @EventHandler
    public void onRiptideRideLand(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident && trident.getItemStack().equals(this.RIPTIDE_RIDE)))
            return;

        trident.eject();
    }

    @EventHandler
    public void onLoyaltyLightningLand(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident && trident.getItemStack().equals(this.LIGHTNING_LOYALTY)
            && trident.getShooter() instanceof Player player)) return;

        World world = event.getEntity().getWorld();
        world.strikeLightning(event.getEntity().getLocation());

        player.teleport(event.getEntity().getLocation().setDirection(player.getLocation().getDirection()), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
    }
}
