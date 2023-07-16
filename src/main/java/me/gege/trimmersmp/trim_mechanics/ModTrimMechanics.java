package me.gege.trimmersmp.trim_mechanics;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ModTrimMechanics {
    public int equippedTrims(Iterable<ItemStack> armorItems, String target) {
        ArrayList<String> trims = new ArrayList<>();

        for (ItemStack armorItem : armorItems) {
            if (armorItem.getNbt() != null) {
                try {
                    String potential_trim = armorItem.getNbt().toString().split("trimmersmp:")[1].replace("\"}}", "");
                    if (potential_trim.contains("harnessed_" + target)) {
                        trims.add(potential_trim);
                    }
                } catch (Exception ignored) {

                }
            }
        }

        return trims.size();
    }
}
