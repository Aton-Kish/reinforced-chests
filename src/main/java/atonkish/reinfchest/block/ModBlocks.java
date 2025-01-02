package atonkish.reinfchest.block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
            Block block = ModBlocks.register(Identifier.of(namespace, material.getName() + "_chest"),
                    (abstractBlockSettings) -> new ReinforcedChestBlock(material,
                            () -> ModBlockEntityType.REINFORCED_CHEST_MAP.get(material),
                            abstractBlockSettings),
                    REINFORCED_CHEST_SETTINGS_MAP.get(material));
            REINFORCED_CHEST_MAP.put(material, block);
        }

        return REINFORCED_CHEST_MAP.get(material);
    }

    private static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings) {
        Block block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static RegistryKey<Block> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.BLOCK, id);
    }

    private static Block register(Identifier id, Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings) {
        return register(keyOf(id), factory, settings);
    }
}
