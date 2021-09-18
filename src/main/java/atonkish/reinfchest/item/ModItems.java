package atonkish.reinfchest.item;

import java.util.HashMap;

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
    public static final HashMap<ReinforcingMaterial, Item> REINFORCED_CHEST_MAP;

    public static void init() {
        Item iconItem = REINFORCED_CHEST_MAP.get(ReinforcingMaterial.NETHERITE);
        ((ItemGroupInterface) ModItemGroup.REINFORCED_STORAGE).setIcon(iconItem);
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

    private static Item.Settings createMaterialSettings(ReinforcingMaterial material) {
        Item.Settings settings = new Item.Settings().group(ModItemGroup.REINFORCED_STORAGE);
        switch (material) {
            default:
            case COPPER:
            case IRON:
            case GOLD:
            case DIAMOND:
                break;
            case NETHERITE:
                settings = settings.fireproof();
                break;
        }
        return settings;
    }

    static {
        REINFORCED_CHEST_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterial.values()) {
            Item item = register(
                    new BlockItem(ModBlocks.REINFORCED_CHEST_MAP.get(material), createMaterialSettings(material)));
            REINFORCED_CHEST_MAP.put(material, item);
        }
    }
}
