package atonkish.reinfchest.item;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.item.ItemGroupInterface;
import atonkish.reinfcore.item.ModItemGroup;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.ModBlocks;

public class ModItems {
    public static final Map<ReinforcingMaterial, Item> REINFORCED_CHEST_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, Item.Settings> REINFORCED_CHEST_SETTINGS_MAP = new LinkedHashMap<>();

    public static Item registerMaterial(ReinforcingMaterial material, Item.Settings settings) {
        if (!REINFORCED_CHEST_SETTINGS_MAP.containsKey(material)) {
            REINFORCED_CHEST_SETTINGS_MAP.put(material, settings);
        }

        if (!REINFORCED_CHEST_MAP.containsKey(material)) {
            Item item = register(
                    new BlockItem(ModBlocks.REINFORCED_CHEST_MAP.get(material),
                            REINFORCED_CHEST_SETTINGS_MAP.get(material)));
            REINFORCED_CHEST_MAP.put(material, item);
        }

        return REINFORCED_CHEST_MAP.get(material);
    }

    public static void registerMaterialItemGroupIcon(ReinforcingMaterial material) {
        Item item = REINFORCED_CHEST_MAP.get(material);
        ((ItemGroupInterface) ModItemGroup.REINFORCED_STORAGE).setIcon(item);
    }

    private static Item register(BlockItem item) {
        return register(item.getBlock(), (Item) item);
    }

    protected static Item register(Block block, Item item) {
        return register(Registry.BLOCK.getId(block), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registry.ITEM, id, item);
    }
}
