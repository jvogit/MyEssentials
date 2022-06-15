package com.gmail.justinxvopro.MyEssentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class SilkTouchSpawnerListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.SPAWNER)
			return;
		if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
			event.setDropItems(false);
			ItemStack drop = new ItemStack(event.getBlock().getType(), 1);
			CreatureSpawner ogSMeta = (CreatureSpawner) event.getBlock().getState();
			BlockStateMeta bMeta = (BlockStateMeta) drop.getItemMeta();
			CreatureSpawner sMeta = (CreatureSpawner) bMeta.getBlockState();
			sMeta.setSpawnedType(ogSMeta.getSpawnedType());
			bMeta.setBlockState(sMeta);
			bMeta.setDisplayName(ChatColor.GREEN + ogSMeta.getSpawnedType().toString() + " Spawner");
			drop.setItemMeta(bMeta);
			event.setExpToDrop(0);
			event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onSpawnerPlace(BlockPlaceEvent event) {
		if (event.getBlock().getType() != Material.SPAWNER) {
			return;
		}

		ItemStack stack = event.getItemInHand();
		BlockStateMeta bMeta = (BlockStateMeta) stack.getItemMeta();
		CreatureSpawner sMeta = (CreatureSpawner) bMeta.getBlockState();
		CreatureSpawner placedMeta = (CreatureSpawner) event.getBlock().getState();
		placedMeta.setSpawnedType(sMeta.getSpawnedType());
		placedMeta.update();
	}

}
