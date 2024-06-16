package atonkish.reinfchest.block.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.ModBlocks;

public class ModBlockEntityType {
    public static final Map<ReinforcingMaterial, BlockEntityType<ReinforcedChestBlockEntity>> REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    public static BlockEntityType<ReinforcedChestBlockEntity> registerMaterial(String namespace,
            ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_MAP.containsKey(material)) {
            String id = material.getName() + "_chest";
            BlockEntityType.Builder<ReinforcedChestBlockEntity> builder = BlockEntityType.Builder
                    .create(createBlockEntityTypeFactory(material), ModBlocks.REINFORCED_CHEST_MAP.get(material));
            BlockEntityType<ReinforcedChestBlockEntity> blockEntityType = ModBlockEntityType
                    .create(namespace, id, builder);
            REINFORCED_CHEST_MAP.put(material, blockEntityType);
        }

        return REINFORCED_CHEST_MAP.get(material);
    }

    private static BlockEntityType<ReinforcedChestBlockEntity> create(String namespace, String id,
            BlockEntityType.Builder<ReinforcedChestBlockEntity> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(namespace, id), builder.build(null));
    }

    private static BlockEntityType.BlockEntityFactory<ReinforcedChestBlockEntity> createBlockEntityTypeFactory(
            ReinforcingMaterial material) {
        return (BlockPos blockPos, BlockState blockState) -> new ReinforcedChestBlockEntity(material, blockPos,
                blockState);
    }
}
