package me.gege.trimmersmp.screen;

import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
    public static ScreenHandlerType<CrystalCompressorScreenHandler> CRYSTAL_COMPRESSOR_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        CRYSTAL_COMPRESSOR_SCREEN_HANDLER = new ScreenHandlerType<>(CrystalCompressorScreenHandler::new, FeatureSet.empty());
    }
}
