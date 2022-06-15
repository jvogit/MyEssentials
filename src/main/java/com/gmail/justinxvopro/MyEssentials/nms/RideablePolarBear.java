package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RideablePolarBear extends PolarBear {
	private boolean bD = true;
	private int bE = 0;
	private int bF = 0;

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
	
//	public boolean dD() {
//		Entity entity = this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
//		if (!(entity instanceof EntityHuman)) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	@Override
//	public void f(Vec3D vec3d) {
//		if (this.isAlive()) {
//			Entity entity = this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
//
//			if (dD()) {
//				this.yaw = entity.yaw;
//				this.lastYaw = this.yaw;
//				this.pitch = entity.pitch * 0.5F;
//				this.setYawPitch(this.yaw, this.pitch);
//				this.aK = this.yaw;
//				//this.aN = this.yaw;
//				//this.K = 1.0F;
//				//this.aO = this.db() * 0.1F;
//
//				if (true) {
//					float f = (float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * 0.225F;
//
//					if (this.bD) {
//						f += f * 1.15F * MathHelper.sin((float) this.bE / (float) this.bF * 3.1415927F);
//					}
//
//					this.q(f);
//					super.f(new Vec3D(0.0D, 0.0D, 1.0D));
//				} else {
//					this.setMot(Vec3D.a);
//				}
//
//				//this.aE = this.aF;
//				double d0 = this.locX() - this.lastX;
//				double d1 = this.locZ() - this.lastZ;
//				float f1 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
//
//				if (f1 > 1.0F) {
//					f1 = 1.0F;
//				}
//
//				this.aH += (f1 - this.aF) * 0.4F;
//				this.aI += this.aF;
//			} else {
//				/* this.K = 0.5F;
//				this.aO = 0.02F; */
//				super.f(vec3d);
//			}
//		}
//	}

}
