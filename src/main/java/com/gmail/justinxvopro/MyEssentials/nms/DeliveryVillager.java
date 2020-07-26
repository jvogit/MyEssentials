package com.gmail.justinxvopro.MyEssentials.nms;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_14_R1.ChatComponentText;
import net.minecraft.server.v1_14_R1.DamageSource;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EntityVillager;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_14_R1.SoundEffect;
import net.minecraft.server.v1_14_R1.SoundEffects;
import net.minecraft.server.v1_14_R1.World;

public class DeliveryVillager extends EntityVillager {
	private boolean done = false;

	public DeliveryVillager(EntityTypes<? extends EntityVillager> entitytypes, World world) {
		super(EntityTypes.VILLAGER, world);

		this.setCustomName(new ChatComponentText(RandomUtils.chooseRandomly("UberEats", "DoorDash")));
		this.setCustomNameVisible(true);
	}

	public void clearPathfinders() {
		ReflectUtils.getPrivateField("d", PathfinderGoalSelector.class, this.goalSelector, Set.class).clear();
		ReflectUtils.getPrivateField("f", PathfinderGoalSelector.class, this.goalSelector, Set.class).clear();
	}

	public void deliverTo(EntityLiving target) {
		this.goalSelector.a(0, new PathFinderFollowEntityLiving(this, target, (pathfinder) -> {
			if (pathfinder.target instanceof EntityHuman) {
				((EntityHuman) pathfinder.target).getBukkitEntity().sendMessage("Your food has arrived!");
			}
		}));
	}

	@Override
	protected SoundEffect getSoundHurt(DamageSource source) {
		return SoundEffects.BLOCK_ANVIL_PLACE;
	}

	@Override
	protected SoundEffect getSoundDeath() {
		return SoundEffects.BLOCK_ANVIL_DESTROY;
	}

	@Override
	public boolean a(EntityHuman human, EnumHand hand) {
		if (!done) {
			done = true;
			Player player = (Player) human.getBukkitEntity();

			player.sendMessage("You have received food!");
			Material foodType = RandomUtils.chooseRandomly(Material.COOKED_BEEF, Material.COOKED_CHICKEN,
					Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT, Material.MUSHROOM_STEW,
					Material.BREAD);
			player.getInventory().addItem(new ItemStack(foodType, RandomUtils.chooseRandomly(1, 2, 3)));
			this.clearPathfinders();
			this.goalSelector.a(0, new PathFinderWalkToLocation(this,
					new Location(player.getWorld(), this.locX + 10, this.locY, this.locZ + 10), (pathFinder) -> {
						this.clearPathfinders();
					}));
		}
		return true;
	}

}
