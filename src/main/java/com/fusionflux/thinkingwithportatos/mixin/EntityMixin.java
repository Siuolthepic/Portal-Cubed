package com.fusionflux.thinkingwithportatos.mixin;

import com.fusionflux.thinkingwithportatos.accessor.VelocityTransfer;
import com.fusionflux.thinkingwithportatos.blocks.RepulsionGel;
import com.fusionflux.thinkingwithportatos.blocks.ThinkingWithPortatosBlocks;
import com.fusionflux.thinkingwithportatos.entity.EntityAttachments;
import com.fusionflux.thinkingwithportatos.sound.ThinkingWithPortatosSounds;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAttachments, VelocityTransfer {
    @Shadow
    public World world;
    @Shadow
    public int age;
    @Shadow
    public boolean horizontalCollision;
    @Shadow
    public boolean verticalCollision;
    @Unique
    private int timeinblock = 1;
    @Unique
    private double maxFallSpeed = 0;
    @Unique
    private double storeVelocity1 = 0;
    @Unique
    private double storeVelocity2 = 0;
    @Unique
    private double speedTransformApply = 0;
    @Unique
    private boolean recentlyTouchedPortal;


    @Override
    public double getMaxFallSpeed() {
        return maxFallSpeed;
    }

    @Override
    public void setMaxFallSpeed(double maxFallSpeed) {
        this.maxFallSpeed = maxFallSpeed;
    }

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public abstract Box getBoundingBox();

    @Shadow
    public abstract boolean equals(Object o);

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);


    @Shadow
    public abstract EntityPose getPose();

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract boolean isSneaking();




    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {








        if (maxFallSpeed == 10 && world.getBlockState(this.getBlockPos()).getBlock() == ThinkingWithPortatosBlocks.PROPULSION_GEL) {
            maxFallSpeed = 10;
        } else {
            if (maxFallSpeed > 0) {
                maxFallSpeed = maxFallSpeed - 1;
            }
        }


        if (world.getBlockState(this.getBlockPos()).getBlock() == ThinkingWithPortatosBlocks.REPULSION_GEL) {
            BlockState state = world.getBlockState(this.getBlockPos());
            Vec3d direction = new Vec3d(0, 0, 0);
            if (this.verticalCollision) {
                if (state.get(RepulsionGel.UP)) {
                    direction = direction.add(0, -2, 0);
                }

                if (state.get(RepulsionGel.DOWN)) {
                    direction = direction.add(0, 2, 0);
                }

            }
            if (this.horizontalCollision) {
                if (state.get(RepulsionGel.NORTH)) {
                    direction = direction.add(0, 0, 1);
                }

                if (state.get(RepulsionGel.SOUTH)) {
                    direction = direction.add(0, 0, -1);
                }

                if (state.get(RepulsionGel.EAST)) {
                    direction = direction.add(-1, 0, 0);
                }

                if (state.get(RepulsionGel.WEST)) {
                    direction = direction.add(1, 0, 0);
                }
                direction = direction.add(0, 0.5, 0);
            }
            if (!this.isSneaking() && !direction.equals(new Vec3d(0, 0, 0))) {
                if (world.isClient && (Object) this instanceof PlayerEntity) {
                    world.playSound((PlayerEntity) (Object) this, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), ThinkingWithPortatosSounds.GEL_BOUNCE_EVENT, SoundCategory.BLOCKS, .3F, 1F);
                } else {
                    world.playSound(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), ThinkingWithPortatosSounds.GEL_BOUNCE_EVENT, SoundCategory.BLOCKS, .3F, 1F);
                }
                this.setVelocity(this.getVelocity().multiply(1.4));
                this.setVelocity(this.getVelocity().add(direction.x, direction.y, direction.z));
            }
        } else if (world.getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ())).getBlock() == ThinkingWithPortatosBlocks.REPULSION_GEL) {
            BlockState state = world.getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ()));
            Vec3d direction = new Vec3d(0, 0, 0);
            if (this.verticalCollision) {
                if (state.get(RepulsionGel.UP)) {
                    direction = direction.add(0, -1, 0);
                }

                if (state.get(RepulsionGel.DOWN)) {
                    direction = direction.add(0, 1, 0);
                }

            }
            if (this.horizontalCollision) {
                if (state.get(RepulsionGel.NORTH)) {
                    direction = direction.add(0, 0, 1);
                }

                if (state.get(RepulsionGel.SOUTH)) {
                    direction = direction.add(0, 0, -1);
                }

                if (state.get(RepulsionGel.EAST)) {
                    direction = direction.add(-1, 0, 0);
                }

                if (state.get(RepulsionGel.WEST)) {
                    direction = direction.add(1, 0, 0);
                }
                direction = direction.add(0, 0.45, 0);
            }
            if (!this.isSneaking() && !direction.equals(new Vec3d(0, 0, 0))) {
                if (world.isClient && (Object) this instanceof PlayerEntity) {
                    world.playSound((PlayerEntity) (Object) this, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), ThinkingWithPortatosSounds.GEL_BOUNCE_EVENT, SoundCategory.BLOCKS, .3F, 1F);
                } else {
                    world.playSound(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), ThinkingWithPortatosSounds.GEL_BOUNCE_EVENT, SoundCategory.BLOCKS, .3F, 1F);
                }
                this.setVelocity(this.getVelocity().add(direction.x, direction.y, direction.z));
            }
        }
        if (world.isClient) {
            storeVelocity2 = storeVelocity1;
            storeVelocity1 = this.getVelocity().length();

            /*-----------
            if (storeVelocity3 > storeVelocity1 && storeVelocity3 > storeVelocity2) {
                speedTransformApply=storeVelocity3;
            }
            -----------*/
        }

        speedTransformApply = Math.max(storeVelocity1, storeVelocity2);
    }

    @Override
    public double getVelocityTransfer() {
        return this.speedTransformApply;
    }

    @Override
    public void setVelocityTransfer(double speedValueTransferDuck) {
        this.speedTransformApply = speedValueTransferDuck;
    }




    /*----------
    @Inject(method = "calculateDimensions", at = @At("TAIL"))
    public void calculateDimensions(CallbackInfo ci) {
        EntityPose entityPose2 = this.getPose();
        EntityDimensions entityDimensions3 = this.getDimensions(entityPose2);
        this.standingEyeHeight = this.getEyeHeight(entityPose2, entityDimensions3) - 1;
    }
    ----------*/


}
