package atonkish.reinfchest.block.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;

import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.mixin.BlockEntityTypeAccessor;
import atonkish.reinfchest.mixin.BlockEntityTypeInvoker;

public class ModBlockEntityType {
    public static final Map<ReinforcingMaterial, BlockEntityType<ReinforcedChestBlockEntity>> REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    public static BlockEntityType<ReinforcedChestBlockEntity> registerMaterial(String namespace,
            ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_MAP.containsKey(material)) {
            String id = material.getName() + "_chest";
            Block block = ModBlocks.REINFORCED_CHEST_MAP.get(material);
            BlockEntityType<ReinforcedChestBlockEntity> blockEntityType = BlockEntityTypeInvoker.create(
                    Identifier.of(namespace, id).toString(),
                    (blockPos, blockState) -> new ReinforcedChestBlockEntity(material, blockPos, blockState),
                    block);
            REINFORCED_CHEST_MAP.put(material, blockEntityType);

            ((BlockEntityTypeAccessor) BlockEntityType.CHEST).getBlocks().add(block);
        }

        return REINFORCED_CHEST_MAP.get(material);
    }
}
