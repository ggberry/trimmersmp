package me.gege.trimmersmp.mixin;

import me.gege.trimmersmp.util.TrimCounter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
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

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class RaiserTrimMechanic extends LivingEntity{
    protected RaiserTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Inject(at = @At("HEAD"), method = "tick")
    private void raiserTrimMechanic(CallbackInfo ci) {
        int trims = new TrimCounter().equippedTrims(this.getArmorItems(), "raiser");

        if (trims < 2 && this.hasStatusEffect(StatusEffects.STRENGTH) && this.getStatusEffect(StatusEffects.STRENGTH).getDuration() == -1) {
            this.removeStatusEffect(StatusEffects.STRENGTH);
        }

        if (trims == 2 || trims == 3) {
            this.removeStatusEffect(StatusEffects.STRENGTH);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, -1, 0, false, false, false));
        } else if (trims == 4) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, -1, 1, false, false, false));
        }
    }
}
