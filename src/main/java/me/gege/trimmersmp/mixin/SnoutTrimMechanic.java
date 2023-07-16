package me.gege.trimmersmp.mixin;

import me.gege.trimmersmp.trim_mechanics.ModTrimMechanics;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SnoutTrimMechanic extends LivingEntity{

    protected SnoutTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "applyDamage")
    public void silenceTrimMechanicNausea(DamageSource source, float amount, CallbackInfo ci) {
        int trims = new ModTrimMechanics().equippedTrims(this.getArmorItems(), "snout");

        if (source.getAttacker() != null && source.getAttacker().isPlayer()) {
            LivingEntity attacker = (LivingEntity)source.getAttacker();

            if (trims == 2 || trims == 3) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, 0, false, false, false));
            } else if (trims == 4) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, 1, false, false, false));
            }
            System.out.println(attacker.getStatusEffects());
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void silenceTrimMechanicJump(CallbackInfo ci) {
        int trims = new ModTrimMechanics().equippedTrims(this.getArmorItems(), "snout");

        if (trims >= 2 && trims < 4) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 300, 0, false, false, false));
        } else if (trims == 4) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 300, 0, false, false, false));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 300, 0, false, false, false));
        }
    }
}
