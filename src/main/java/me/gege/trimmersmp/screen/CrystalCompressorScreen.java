package me.gege.trimmersmp.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.gege.trimmersmp.TrimmerSmp;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CrystalCompressorScreen extends HandledScreen<CrystalCompressorScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(TrimmerSmp.MOD_ID, "textures/gui/crystal_compressor_gui.png");

    public CrystalCompressorScreen(CrystalCompressorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(context, x, y);
        renderNoRecipe(context, x, y);
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if(handler.isCrafting()) {
            context.drawTexture(TEXTURE, x + 101, y + 39, 176, 0, handler.getScaledProgress(), 15);
        }
    }

    private void renderNoRecipe(DrawContext context, int x, int y) {
        if(!handler.isCrafting()) {
            context.drawTexture(TEXTURE, x + 101, y + 39, 176, 15, 22, 15);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
