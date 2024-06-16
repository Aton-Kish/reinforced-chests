package atonkish.reinfchest.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;

import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

public enum ReinforcingMaterialSettings {
    COPPER(ReinforcedCoreRegistry.registerReinforcingMaterial("copper", 45, Items.COPPER_INGOT),
            AbstractBlock.Settings
                    .create()
                    .mapColor(MapColor.ORANGE)
                    .strength(2.5F, 6.0F)
                    .sounds(BlockSoundGroup.COPPER),
            new Item.Settings()),
    IRON(ReinforcedCoreRegistry.registerReinforcingMaterial("iron", 54, Items.IRON_INGOT),
            AbstractBlock.Settings
                    .create()
                    .mapColor(MapColor.IRON_GRAY)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(2.5F, 6.0F)
                    .sounds(BlockSoundGroup.METAL),
            new Item.Settings()),
    GOLD(ReinforcedCoreRegistry.registerReinforcingMaterial("gold", 81, Items.GOLD_INGOT),
            AbstractBlock.Settings
                    .create()
                    .mapColor(MapColor.GOLD)
                    .instrument(NoteBlockInstrument.BELL)
                    .strength(2.5F, 6.0F)
                    .sounds(BlockSoundGroup.METAL),
            new Item.Settings()),
    DIAMOND(ReinforcedCoreRegistry.registerReinforcingMaterial("diamond", 108, Items.DIAMOND),
            AbstractBlock.Settings
                    .create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .strength(2.5F, 6.0F)
                    .sounds(BlockSoundGroup.METAL),
            new Item.Settings()),
    NETHERITE(ReinforcedCoreRegistry.registerReinforcingMaterial("netherite", 108, Items.NETHERITE_INGOT),
            AbstractBlock.Settings
                    .create()
                    .mapColor(MapColor.BLACK)
                    .strength(2.5F, 1200.0F)
                    .sounds(BlockSoundGroup.NETHERITE),
            new Item.Settings().fireproof());

    private final ReinforcingMaterial material;
    private final Block.Settings blockSettings;
    private final Item.Settings itemSettings;

    private ReinforcingMaterialSettings(ReinforcingMaterial material, Block.Settings blockSettings,
            Item.Settings itemSettings) {
        this.material = material;
        this.blockSettings = blockSettings;
        this.itemSettings = itemSettings;
    }

    public ReinforcingMaterial getMaterial() {
        return this.material;
    }

    public Block.Settings getBlockSettings() {
        return this.blockSettings;
    }

    public Item.Settings getItemSettings() {
        return this.itemSettings;
    }
}
