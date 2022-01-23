package atonkish.reinfchest.block;

import java.util.LinkedHashMap;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.entity.ModBlockEntityType;

public class ModBlocks {
    public static final LinkedHashMap<ReinforcingMaterial, Block> REINFORCED_CHEST_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, Block.Settings> REINFORCED_CHEST_SETTING_MAP = new LinkedHashMap<>();

    public static Block registerMaterial(ReinforcingMaterial material) {
        Block block = register(material.getName() + "_chest",
                new ReinforcedChestBlock(material, REINFORCED_CHEST_SETTING_MAP.get(material), () -> {
                    return ModBlockEntityType.REINFORCED_CHEST_MAP.get(material);
                }));

        REINFORCED_CHEST_MAP.put(material, block);

        return block;
    }

    public static Block.Settings registerMaterialSetting(ReinforcingMaterial material, Block.Settings settings) {
        REINFORCED_CHEST_SETTING_MAP.put(material, settings);

        return settings;
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(ReinforcedChestsMod.MOD_ID, id), block);
    }
}
