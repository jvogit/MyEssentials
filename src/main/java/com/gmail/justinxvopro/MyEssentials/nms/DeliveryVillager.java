package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.mojang.serialization.Dynamic;

public class DeliveryVillager extends Villager {
	private boolean done = false;
	private Vec3 lastPosition;
	private Goal followPlayer;
	private Goal leavePlayer;

	public DeliveryVillager(Level world) {
		super(EntityType.VILLAGER, world);

		this.setCustomName(Component.literal(RandomUtils.chooseRandomly("UberEats", "DoorDash", "GrubHub", "Villedo")));
		this.setCustomNameVisible(true);
		lastPosition = this.position();
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        Brain<Villager> brain = this.brainProvider().makeBrain(dynamic);
        return brain;
	}

	public void deliverTo(Entity target) {
		lastPosition = this.position();
		followPlayer = new PathFinderFollowEntityLiving(this, target, 
			(pathfinder) -> {
				if (pathfinder.target instanceof Player) {
					((Player) pathfinder.target).getBukkitEntity().playSound(
						Sound.sound(org.bukkit.Sound.BLOCK_BELL_USE, Source.AMBIENT, 1.f, 1.f),
						position().x,
						position().y,
						position().z
					);
					((Player) pathfinder.target).getBukkitEntity().sendMessage("Your food has arrived!");
				}
			}
		);
		this.goalSelector.addGoal(0, followPlayer);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.PLAYER_HURT;
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
			Material foodType = RandomUtils.chooseRandomly(
				Material.COOKED_BEEF,
				Material.COOKED_CHICKEN,
				Material.COOKED_MUTTON,
				Material.COOKED_PORKCHOP,
				Material.COOKED_RABBIT,
				Material.BREAD
			);
			bukkitPlayer.getInventory().addItem(new ItemStack(foodType, RandomUtils.chooseRandomly(2, 3, 4)));
			this.goalSelector.removeGoal(followPlayer);
			leavePlayer = new PathFinderWalkToLocation(this, this.lastPosition, 
				(pathFinder) -> {
					this.setHealth(0f);
				}
			);
			this.goalSelector.addGoal(0, leavePlayer);
		} else {
			this.setHealth(0f);
		}
		
		return InteractionResult.PASS;
	}

}
