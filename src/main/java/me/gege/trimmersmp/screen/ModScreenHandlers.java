package me.gege.trimmersmp.screen;

import me.gege.trimmersmp.TrimmerSmp;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static ScreenHandlerType<CrystalCompressorScreenHandler> CRYSTAL_COMPRESSOR_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        CRYSTAL_COMPRESSOR_SCREEN_HANDLER = new ScreenHandlerType<>(CrystalCompressorScreenHandler::new, FeatureSet.empty());

        CRYSTAL_COMPRESSOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(TrimmerSmp.MOD_ID, "crystal_compressor"),
                CrystalCompressorScreenHandler::new);
    }
}
