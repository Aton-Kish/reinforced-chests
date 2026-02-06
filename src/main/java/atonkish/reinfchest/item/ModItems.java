package atonkish.reinfchest.item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfcore.item.ModItemGroup;
import atonkish.reinfcore.item.ModItemGroups;
import atonkish.reinfcore.util.ReinforcingMaterial;

public class ModItems {
  public static final Map<ReinforcingMaterial, Item> REINFORCED_CHEST_MAP = new LinkedHashMap<>();
  public static final Map<ReinforcingMaterial, Item.Settings> REINFORCED_CHEST_SETTINGS_MAP =
      new LinkedHashMap<>();

  public static Item registerMaterial(ReinforcingMaterial material, Item.Settings settings) {
    if (!REINFORCED_CHEST_SETTINGS_MAP.containsKey(material)) {
      REINFORCED_CHEST_SETTINGS_MAP.put(material, settings);
    }

    if (!REINFORCED_CHEST_MAP.containsKey(material)) {
      Item item =
          ModItems.register(
              ModBlocks.REINFORCED_CHEST_MAP.get(material),
              REINFORCED_CHEST_SETTINGS_MAP.get(material));
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
          .register(content -> content.add(item));
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE)
          .register(content -> content.add(item));
      ItemGroupEvents.modifyEntriesEvent(ModItemGroups.REINFORCED_STORAGE)
          .register(content -> content.add(item));
      REINFORCED_CHEST_MAP.put(material, item);
    }

    return REINFORCED_CHEST_MAP.get(material);
  }

  public static void registerMaterialItemGroupIcon(ReinforcingMaterial material) {
    Item item = REINFORCED_CHEST_MAP.get(material);
    ModItemGroup.setIcon(Registries.ITEM_GROUP.get(ModItemGroups.REINFORCED_STORAGE), item);
  }

  private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
    return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
  }

  public static Item register(Block block, Item.Settings settings) {
    return register(block, BlockItem::new, settings);
  }

  private static Item register(
      Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
    return register(
        keyOf(block.getRegistryEntry().registryKey()),
        itemSettings -> (Item) factory.apply(block, itemSettings),
        settings.useBlockPrefixedTranslationKey());
  }

  private static Item register(
      RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
    Item item = factory.apply(settings.registryKey(key));
    if (item instanceof BlockItem blockItem) {
      blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
    }

    return Registry.register(Registries.ITEM, key, item);
  }
}
