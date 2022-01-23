package atonkish.reinfchest.api;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.entity.ModBlockEntityType;
import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
import atonkish.reinfchest.item.ModItems;
import atonkish.reinfchest.stat.ModStats;

public class ReinforcedChestsRegistry {
    public static Identifier registerMaterialOpenStat(ReinforcingMaterial material) {
        return ModStats.registerMaterialOpen(material);
    }

    public static Block registerMaterialBlock(ReinforcingMaterial material, Block.Settings settings) {
        return ModBlocks.registerMaterial(material, settings);
    }

    public static BlockEntityType<ReinforcedChestBlockEntity> registerMaterialBlockEntityType(
            ReinforcingMaterial material) {
        return ModBlockEntityType.registerMaterial(material);
    }

    public static Item registerMaterialItem(ReinforcingMaterial material, Item.Settings settings) {
        return ModItems.registerMaterial(material, settings);
    }

    public static void registerMaterialItemGroupIcon(ReinforcingMaterial material) {
        ModItems.registerMaterialItemGroupIcon(material);
    }
}
