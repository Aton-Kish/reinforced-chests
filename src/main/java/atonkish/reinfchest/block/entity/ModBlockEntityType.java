package atonkish.reinfchest.block.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.ModBlocks;

public class ModBlockEntityType {
    public static final Map<ReinforcingMaterial, BlockEntityType<ReinforcedChestBlockEntity>> REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    public static BlockEntityType<ReinforcedChestBlockEntity> registerMaterial(String namespace,
            ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_MAP.containsKey(material)) {
            String id = material.getName() + "_chest";
            FabricBlockEntityTypeBuilder<ReinforcedChestBlockEntity> builder = FabricBlockEntityTypeBuilder
                    .create(createBlockEntityTypeFactory(material), ModBlocks.REINFORCED_CHEST_MAP.get(material));
            BlockEntityType<ReinforcedChestBlockEntity> blockEntityType = create(namespace, id, builder);
            REINFORCED_CHEST_MAP.put(material, blockEntityType);
        }

        return REINFORCED_CHEST_MAP.get(material);
    }

    private static BlockEntityType<ReinforcedChestBlockEntity> create(String namespace, String id,
            FabricBlockEntityTypeBuilder<ReinforcedChestBlockEntity> builder) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(namespace, id),
                builder.build(null));
    }

    private static FabricBlockEntityTypeBuilder.Factory<ReinforcedChestBlockEntity> createBlockEntityTypeFactory(
            ReinforcingMaterial material) {
        return (BlockPos blockPos, BlockState blockState) -> new ReinforcedChestBlockEntity(material, blockPos,
                blockState);
    }
}
