package me.gege.trimmersmp.mixin;

import me.gege.trimmersmp.trim_mechanics.ModTrimMechanics;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SilenceTrimMechanic extends LivingEntity {
    protected SilenceTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Inject(at = @At("HEAD"), method = "attack")
    private void silenceTrimMechanic(Entity target, CallbackInfo ci) {
        int trims = new ModTrimMechanics().equippedTrims(this.getArmorItems(), "silence");

        if (target != null && this.getPose().toString().contains("CROUCHING") && target.isPlayer()) {
            if (trims == 2 || trims == 3) {
                this.heal(1);
                this.damageArmor(this.getDamageSources().generic(), 25);
            } else if (trims == 4) {
                this.heal(2);
                this.damageArmor(this.getDamageSources().generic(), 45);
            }
        }
    }
}
