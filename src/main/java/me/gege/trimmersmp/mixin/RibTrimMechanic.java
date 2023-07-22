package me.gege.trimmersmp.mixin;

import me.gege.trimmersmp.util.TrimCounter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
public abstract class RibTrimMechanic extends LivingEntity{
    protected RibTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Inject(at = @At("HEAD"), method = "applyDamage")
    private void ribTrimMechanic(CallbackInfo ci) {
        int trims = new TrimCounter().equippedTrims(this.getArmorItems(), "rib");

        if (trims == 2 || trims == 3) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 618, 0, false, false, false));
        } else if (trims == 4) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 618, 1, false, false, false));
        }
    }
}
