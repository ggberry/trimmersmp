package me.gege.trimmersmp.block.entity;

import me.gege.trimmersmp.TrimmerSmp;
import me.gege.trimmersmp.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

public class ModBlockEntities {
    public static BlockEntityType<CrystalCompressorBlockEntity> CRYSTAL_COMPRESSOR_ENTITY;

    public static void registerBlockEntities() {
        CRYSTAL_COMPRESSOR_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TrimmerSmp.MOD_ID, "crystal_compressor"),
                FabricBlockEntityTypeBuilder.create(CrystalCompressorBlockEntity::new , ModBlocks.CRYSTAL_COMPRESSOR).build(null));
    }
}
