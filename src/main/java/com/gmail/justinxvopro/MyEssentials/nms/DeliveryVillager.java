package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DeliveryVillager extends Villager {
	private boolean done = false;

	public DeliveryVillager(EntityType<? extends Villager> entitytypes, Level world) {
		super(EntityType.VILLAGER, world);

		this.setCustomName(Component.literal(RandomUtils.chooseRandomly("UberEats", "DoorDash", "GrubHub")));
		this.setCustomNameVisible(true);
	}

	public void clearPathfinders() {
		this.goalSelector.removeAllGoals();
	}

	public void deliverTo(Entity target) {
		this.goalSelector.addGoal(0, new PathFinderFollowEntityLiving(this, target, (pathfinder) -> {
			if (pathfinder.target instanceof Player) {
				((Player) pathfinder.target).getBukkitEntity().sendMessage("Your food has arrived!");
			}
		}));
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ANVIL_PLACE;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.ANVIL_DESTROY;
	}

	@Override
	public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
		if (!done) {
			done = true;
			org.bukkit.entity.Player bukkitPlayer = (org.bukkit.entity.Player) player.getBukkitEntity();

			bukkitPlayer.sendMessage("You have received food!");
			Material foodType = RandomUtils.chooseRandomly(Material.COOKED_BEEF, Material.COOKED_CHICKEN,
					Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT, Material.MUSHROOM_STEW,
					Material.BREAD);
			bukkitPlayer.getInventory().addItem(new ItemStack(foodType, RandomUtils.chooseRandomly(1, 2, 3)));
			this.clearPathfinders();
			this.goalSelector.addGoal(0, new PathFinderWalkToLocation(this,
					new Location(bukkitPlayer.getWorld(), this.getX() + 10, this.getY(), this.getZ() + 10), (pathFinder) -> {
						this.clearPathfinders();
					}));
		}
		
		return InteractionResult.SUCCESS;
	}

}
