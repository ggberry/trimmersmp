package me.gege.trimmersmp.mixin;

import me.gege.trimmersmp.util.TrimCounter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class SnoutTrimMechanic extends LivingEntity{

    protected SnoutTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "applyDamage")
    public void snoutTrimMechanicNausea(DamageSource source, float amount, CallbackInfo ci) {
        int trims = new TrimCounter().equippedTrims(this.getArmorItems(), "snout");

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
    public void snoutTrimMechanicJump(CallbackInfo ci) {
        int trims = new TrimCounter().equippedTrims(this.getArmorItems(), "snout");

        if (trims < 2 && this.hasStatusEffect(StatusEffects.JUMP_BOOST) && this.getStatusEffect(StatusEffects.JUMP_BOOST).getDuration() == -1) {
            this.removeStatusEffect(StatusEffects.JUMP_BOOST);
        }

        if (trims < 4 && this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE) && this.getStatusEffect(StatusEffects.DOLPHINS_GRACE).getDuration() == -1) {
            this.removeStatusEffect(StatusEffects.DOLPHINS_GRACE);
        }

        if (trims == 2 || trims == 3) {
            this.removeStatusEffect(StatusEffects.JUMP_BOOST);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, -1, 0, false, false, false));
        } else if (trims == 4) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, -1, 1, false, false, false));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, -1, 0, false, false, false));
        }
    }
}
