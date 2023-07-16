package me.gege.trimmersmp.mixin;

import me.gege.trimmersmp.trim_mechanics.ModTrimMechanics;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class RaiserTrimMechanic extends LivingEntity{
    protected RaiserTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Inject(at = @At("HEAD"), method = "tick")
    private void ribTrimMechanic(CallbackInfo ci) {
        int trims = new ModTrimMechanics().equippedTrims(this.getArmorItems(), "raiser");

        if (trims == 2 || trims == 3) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0, false, false, false));
        } else if (trims == 4) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1, false, false, false));
        }
    }

    @Inject(at = @At("HEAD"), method = "applyDamage")
    public void eyeTrimMechanic(DamageSource source, float amount, CallbackInfo ci) {
        int trims = new ModTrimMechanics().equippedTrims(this.getArmorItems(), "raiser");

        if (source.getAttacker() != null && source.getAttacker().isPlayer()) {
            LivingEntity attacker = (LivingEntity)source.getAttacker();

            if (trims == 4) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 100, 0, false, false, false));
            }
        }
    }
}
