package me.gege.trimmersmp.mixin;

import me.gege.trimmersmp.util.TrimCounter;
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
public abstract class EyeTrimMechanic extends LivingEntity{

    protected EyeTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "applyDamage")
    public void eyeTrimMechanic(DamageSource source, float amount, CallbackInfo ci) {
        int trims = new TrimCounter().equippedTrims(this.getArmorItems(), "eye");

        if (source.getAttacker() != null && source.getAttacker().isPlayer() && this.getPose().toString().contains("CROUCHING")) {
            LivingEntity attacker = (LivingEntity)source.getAttacker();

            if (trims == 2 || trims == 3) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 0, false, false, false));
            } else if (trims == 4) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0, false, false, false));
            }
        }
    }
}
