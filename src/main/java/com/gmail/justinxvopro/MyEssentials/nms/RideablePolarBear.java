package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

public class RideablePolarBear extends PolarBear {
	public RideablePolarBear(EntityType<? extends PolarBear> type, Level world) {
		super(type, world);

	}

	@Override
	public InteractionResult mobInteract(@NotNull Player human, @NotNull InteractionHand h) {
		if (this.getPassengers().isEmpty()) {
			human.startRiding(this);
			human.getBukkitEntity().sendMessage("Wee!");
			return InteractionResult.SUCCESS;
		} else {
			return super.mobInteract(human, h);
		}

	}

	@Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }

	@Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return ((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225f);
    }
}
