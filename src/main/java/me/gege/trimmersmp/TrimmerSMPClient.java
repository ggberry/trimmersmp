package me.gege.trimmersmp;

import me.gege.trimmersmp.screen.CompressorScreen;
import me.gege.trimmersmp.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class TrimmerSMPClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.COMPRESSOR_SCREEN_HANDLER, CompressorScreen::new);
    }
}
