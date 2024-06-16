package atonkish.reinfchest.block;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.entity.ModBlockEntityType;

public class ModBlocks {
    public static final Map<ReinforcingMaterial, Block> REINFORCED_CHEST_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, Block.Settings> REINFORCED_CHEST_SETTINGS_MAP = new LinkedHashMap<>();

    public static Block registerMaterial(String namespace, ReinforcingMaterial material, Block.Settings settings) {
        if (!REINFORCED_CHEST_SETTINGS_MAP.containsKey(material)) {
            REINFORCED_CHEST_SETTINGS_MAP.put(material, settings);
        }

        if (!REINFORCED_CHEST_MAP.containsKey(material)) {
            Block block = ModBlocks.register(namespace, material.getName() + "_chest",
                    new ReinforcedChestBlock(material, REINFORCED_CHEST_SETTINGS_MAP.get(material), () -> {
                        return ModBlockEntityType.REINFORCED_CHEST_MAP.get(material);
                    }));
            REINFORCED_CHEST_MAP.put(material, block);
        }

        return REINFORCED_CHEST_MAP.get(material);
    }

    private static Block register(String namespace, String id, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(namespace, id), block);
    }
}
