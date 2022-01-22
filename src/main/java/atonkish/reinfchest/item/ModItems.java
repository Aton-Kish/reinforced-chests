package atonkish.reinfchest.item;

import java.util.HashMap;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.item.ItemGroupInterface;
import atonkish.reinfcore.item.ModItemGroup;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfchest.block.ModBlocks;

public class ModItems {
    public static final HashMap<ReinforcingMaterial, Item> REINFORCED_CHEST_MAP;

    public static void init() {
        if (!FabricLoader.getInstance().isModLoaded("reinfshulker")) {
            Item iconItem = REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite"));
            ((ItemGroupInterface) ModItemGroup.REINFORCED_STORAGE).setIcon(iconItem);
        }
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
        switch (material.getName()) {
            default:
            case "copper":
            case "iron":
            case "gold":
            case "diamond":
                break;
            case "netherite":
                settings = settings.fireproof();
                break;
        }
        return settings;
    }

    static {
        REINFORCED_CHEST_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
            Item item = register(
                    new BlockItem(ModBlocks.REINFORCED_CHEST_MAP.get(material), createMaterialSettings(material)));
            REINFORCED_CHEST_MAP.put(material, item);
        }
    }
}
