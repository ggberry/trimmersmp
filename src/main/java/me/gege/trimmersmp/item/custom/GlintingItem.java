package me.gege.trimmersmp.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GlintingItem extends Item {
    public GlintingItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
