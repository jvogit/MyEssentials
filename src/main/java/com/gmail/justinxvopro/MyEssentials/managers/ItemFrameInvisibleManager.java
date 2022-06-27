package com.gmail.justinxvopro.MyEssentials.managers;

import com.gmail.justinxvopro.MyEssentials.Core;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ItemFrameInvisibleManager implements Listener {

    private final Core core;

    public ItemFrameInvisibleManager(Core core) {
        this.core = core;
        this.core.getServer().getPluginManager().registerEvents(this, this.core);
    }

    @EventHandler
    public void onItemFrameInvisible(final PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame itemFrame && event.getPlayer().isSneaking())) return;

        itemFrame.setVisible(!itemFrame.isVisible());
        event.getPlayer().sendMessage("Item frame is visible: " + itemFrame.isVisible());
        event.setCancelled(true);
    }
}
