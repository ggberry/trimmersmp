package me.gege.trimmersmp.block;

import me.gege.trimmersmp.TrimmerSmp;
import me.gege.trimmersmp.block.custom.CompressorBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.item.BlockItem;

public class ModBlocks {
    public static final Block COMPRESSOR = registerBlock("compressor",
            new CompressorBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).requiresTool().strength(50f).nonOpaque().sounds(BlockSoundGroup.WOOD)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(TrimmerSmp.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(TrimmerSmp.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        TrimmerSmp.LOGGER.info("Registering ModBlocks for " + TrimmerSmp.MOD_ID);
    }
}
