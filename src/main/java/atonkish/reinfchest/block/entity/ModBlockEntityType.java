package atonkish.reinfchest.block.entity;

import java.util.HashMap;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;

public class ModBlockEntityType {
    public static final HashMap<ReinforcingMaterial, BlockEntityType<ReinforcedChestBlockEntity>> REINFORCED_CHEST_MAP;

    public static void init() {
    }

    private static BlockEntityType<ReinforcedChestBlockEntity> create(String id,
            FabricBlockEntityTypeBuilder<ReinforcedChestBlockEntity> builder) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ReinforcedChestsMod.MOD_ID, id),
                builder.build(null));
    }

    private static FabricBlockEntityTypeBuilder.Factory<ReinforcedChestBlockEntity> createBlockEntityTypeFactory(
            ReinforcingMaterial material) {
        return (BlockPos blockPos, BlockState blockState) -> new ReinforcedChestBlockEntity(material, blockPos,
                blockState);
    }

    static {
        REINFORCED_CHEST_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
            BlockEntityType<ReinforcedChestBlockEntity> blockEntityType = create(material.getName() + "_chest",
                    FabricBlockEntityTypeBuilder.create(createBlockEntityTypeFactory(material),
                            ModBlocks.REINFORCED_CHEST_MAP.get(material)));
            REINFORCED_CHEST_MAP.put(material, blockEntityType);
        }
    }
}
