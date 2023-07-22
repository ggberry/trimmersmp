package me.gege.trimmersmp.screen;

import me.gege.trimmersmp.TrimmerSmp;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static ScreenHandlerType<CompressorScreenHandler> COMPRESSOR_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        COMPRESSOR_SCREEN_HANDLER = new ScreenHandlerType<>(CompressorScreenHandler::new, FeatureSet.empty());

        COMPRESSOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(TrimmerSmp.MOD_ID, "crystal_compressor"),
                CompressorScreenHandler::new);
    }
}
