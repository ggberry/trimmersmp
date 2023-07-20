package me.gege.trimmersmp.mixin;

import com.google.common.collect.Multimap;
import me.gege.trimmersmp.util.TrimCounter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class VexTrimMechanic extends LivingEntity{
    protected VexTrimMechanic(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Inject(at = @At("HEAD"), method = "getAttackCooldownProgressPerTick", cancellable = true)
    public void vexTrimMechanic(CallbackInfoReturnable<Float> cir) {
        int trims = new TrimCounter().equippedTrims(this.getArmorItems(), "vex");
        Multimap<EntityAttribute, EntityAttributeModifier> handItem = this.getStackInHand(this.getActiveHand()).getAttributeModifiers(EquipmentSlot.MAINHAND);

        int DEFAULT_ATTACK_SPEED = 4;
        String split1 = ", operation=ADDITION, name='Tool modifier', id=fa233e1c-4180-4865-b01b-bcce9785aca3";
        String split2 = "amount=";

        try {
            float handItemAttackSpeed = Float.parseFloat(handItem.toString().split(split1)[0].split(split2)[2]) + DEFAULT_ATTACK_SPEED;

            if (trims == 2 || trims == 3) {
                cir.setReturnValue((float)(11.8 + handItemAttackSpeed * 2));
            } else if (trims == 4) {
                cir.setReturnValue((float)(8.8 + handItemAttackSpeed * 2));
            }
        } catch (Exception ignored) {}
    }
}
