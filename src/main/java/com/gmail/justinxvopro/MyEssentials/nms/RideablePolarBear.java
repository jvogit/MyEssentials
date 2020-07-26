package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityPolarBear;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.GenericAttributes;
import net.minecraft.server.v1_14_R1.MathHelper;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;

public class RideablePolarBear extends EntityPolarBear {
	private boolean bD = true;
	private int bE = 0;
	private int bF = 0;

	public RideablePolarBear(EntityTypes<? extends EntityPolarBear> type, World world) {
		super(EntityTypes.POLAR_BEAR, world);

	}

	@Override
	public void initPathfinder() {

	}

	@Override
	public boolean a(EntityHuman human, EnumHand hand) {
		if (this.getPassengers().isEmpty()) {
			human.startRiding(this);
			human.getBukkitEntity().sendMessage("Wee!");
			return true;
		} else {
			return super.a(human, hand);
		}

	}

	@Override
	public boolean dD() {
		Entity entity = this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
		if (!(entity instanceof EntityHuman)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void e(Vec3D vec3d) {
		if (this.isAlive()) {
			Entity entity = this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);

			if (dD()) {
				this.yaw = entity.yaw;
				this.lastYaw = this.yaw;
				this.pitch = entity.pitch * 0.5F;
				this.setYawPitch(this.yaw, this.pitch);
				this.aK = this.yaw;
				this.aM = this.yaw;
				this.K = 1.0F;
				this.aO = this.da() * 0.1F;

				if (this.bZ()) {
					float f = (float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * 0.225F;

					if (this.bD) {
						f += f * 1.15F * MathHelper.sin((float) this.bE / (float) this.bF * 3.1415927F);
					}

					this.o(f);
					super.e(new Vec3D(0.0D, 0.0D, 1.0D));
				} else {
					this.setMot(Vec3D.a);
				}

				this.aE = this.aF;
				double d0 = this.locX - this.lastX;
				double d1 = this.locZ - this.lastZ;
				float f1 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

				if (f1 > 1.0F) {
					f1 = 1.0F;
				}

				this.aF += (f1 - this.aF) * 0.4F;
				this.aG += this.aF;
			} else {
				this.K = 0.5F;
				this.aO = 0.02F;
				super.e(vec3d);
			}
		}
	}

}
