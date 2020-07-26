package com.gmail.justinxvopro.MyEssentials.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.justinxvopro.MyEssentials.Core;

public class MinecartManager implements Listener, CommandExecutor {

	private static boolean ENABLE = true;

	public MinecartManager(Core core) {
		Bukkit.getServer().getPluginManager().registerEvents(this, core);
	}

	@EventHandler
	public void onCartRide(VehicleMoveEvent event) {
		if (!ENABLE)
			return;
		RideableMinecart cart = validMinecart(event.getVehicle(), true);
		if (cart == null)
			return;

		if (!(cart.getLocation().getBlock().getBlockData() instanceof Rail)) {
			return;
		}
		pushEntities(cart);
	}

	@EventHandler
	public void onCartCreate(VehicleCreateEvent e) {
		if (!ENABLE)
			return;

		RideableMinecart cart = validMinecart(e.getVehicle(), false);

		if (cart == null)
			return;

		cart.setMaxSpeed(0.4D * 1.2);
	}

	@EventHandler
	public void onCollision(VehicleEntityCollisionEvent event) {
		RideableMinecart cart = this.validMinecart(event.getVehicle(), true);
		if (cart == null || !ENABLE) {
			return;
		}

		event.setCancelled(true);
		event.setCollisionCancelled(true);
		event.setPickupCancelled(true);

		if ((event.getEntity() instanceof Vehicle)) {
			if (!(zero(event.getEntity().getVelocity()) && zero(event.getVehicle().getVelocity()))) {
				return;
			}
			RideableMinecart entity = this.validMinecart((Vehicle) event.getEntity(), true);

			if (entity == null) {
				return;
			}

			cart.setVelocity(new Vector(0, 0, 0));
			entity.setVelocity(new Vector(0, 0, 0));
			entity.getWorld().createExplosion(entity.getLocation().getBlockX(), entity.getLocation().getBlockY(),
					entity.getLocation().getBlockZ(), 1, false, false);
		}
	}

	private boolean zero(Vector vec) {
		return vec.distanceSquared(new Vector(0.0, -0.0, 0.0)) == 0;
	}

	public void pushEntities(RideableMinecart cart) {
		Vector cartVector = cart.getVelocity();

		cart.getNearbyEntities(2, 1, 2).forEach(entity -> {
			if (!entity.equals(cart.getPassengers().get(0))) {
				if (entity instanceof Minecart) {
					Minecart empty = (Minecart) entity;
					if (empty.getPassengers().size() == 0) {
						empty.getWorld().createExplosion(empty.getLocation().getBlockX(),
								empty.getLocation().getBlockY(), empty.getLocation().getBlockZ(), 1, false, false);
						empty.getWorld().dropItemNaturally(empty.getLocation(), new ItemStack(Material.MINECART));
						empty.remove();
					}
				} else if (!(entity instanceof Item)
						&& !(entity.getVehicle() != null && entity.getVehicle() instanceof RideableMinecart)) {
					Vector entityVector = entity.getVelocity().normalize();
					Vector between = cartVector.subtract(entityVector).normalize();

					entity.setVelocity(between.multiply(2));
				}
			}
		});
	}

	public RideableMinecart validMinecart(Vehicle vehicle, boolean passenger) {
		RideableMinecart to = null;

		if (vehicle instanceof RideableMinecart) {
			to = (RideableMinecart) vehicle;
			if (to.getPassengers().size() == 0 || !(to.getPassengers().get(0) instanceof Player)) {
				if (passenger) {
					return null;
				}
			}
		}

		return to;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		ENABLE = !ENABLE;

		arg0.sendMessage("Minecart Manager Status Enable : " + ENABLE);

		return true;
	}

}
