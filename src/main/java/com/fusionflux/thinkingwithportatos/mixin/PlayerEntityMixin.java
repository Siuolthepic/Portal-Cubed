package com.fusionflux.thinkingwithportatos.mixin;

import com.fusionflux.thinkingwithportatos.ThinkingWithPortatos;
import com.fusionflux.thinkingwithportatos.items.ThinkingWithPortatosItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Unique
    private Vec3d storeVelocity1;
    @Unique
    private Vec3d storeVelocity2;
    private boolean recentlyTouchedPortal;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError(
                ThinkingWithPortatos.MODID + "'s PlayerEntityMixin dummy constructor was called, " +
                        "something is very wrong here!"
        );
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow
    public abstract boolean isSwimming();

    @Shadow
    public abstract boolean isCreative();

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (damageSource == DamageSource.FALL && (itemStack5.getItem().equals(ThinkingWithPortatosItems.LONG_FALL_BOOTS))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getFallSound", at = @At("HEAD"), cancellable = true)
    protected void getFallSound(int distance, CallbackInfoReturnable<SoundEvent> cir) {
        ItemStack itemFeet = this.getEquippedStack(EquipmentSlot.FEET);
        if (itemFeet.getItem().equals(ThinkingWithPortatosItems.LONG_FALL_BOOTS)) {
            cir.setReturnValue(SoundEvents.BLOCK_WOOL_FALL);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {

        /*List<Portal> globalPortals = (List<Portal>) McHelper.getNearbyPortals(((PlayerEntity) (Object) this),50);
        Vec3d expand = this.getVelocity().multiply(2);
        Box streachedBB = this.getBoundingBox().stretch(expand);

        for (Portal globalPortal : globalPortals) {
            if (streachedBB.intersects(globalPortal.getBoundingBox())) {
                this.setVelocity(this.getVelocity().multiply(5, 5, 5));
            }
        }*/

  /*      double reduceGravity = .06666666666;
        double gravityBalancer = 1;
            if (this.isTouchingWater()) {
                gravityBalancer = gravityBalancer * .02;
                if (this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                    gravityBalancer = gravityBalancer * .01;
                }
            }
if(!this.isFallFlying()) {
    if (!this.isSwimming()) {
        this.setVelocity(this.getVelocity().add(0, reduceGravity * gravityBalancer, 0));
    }
}*/

        storeVelocity2 = storeVelocity1;
        storeVelocity1 = this.getVelocity();

        ItemStack itemFeet = this.getEquippedStack(EquipmentSlot.FEET);
        if (storeVelocity2 != null && storeVelocity1 != null) {
            if (itemFeet.getItem().equals(ThinkingWithPortatosItems.LONG_FALL_BOOTS)) {
                if (!this.isOnGround()) {
                    this.setVelocity(this.getVelocity().x * 1.045, this.getVelocity().y, this.getVelocity().z * 1.035);
                }
            }
        }
    }



}




