package atonkish.reinfchest.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.inventory.Inventory;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.ReinforcedChestBlock;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class InventoryTests {
    private void testSingleReinforcedChestInventorySize(TestContext context,
            ReinforcedChestBlock chestBlock, int size) {
        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, chestBlock);

        // Act
        Inventory inventory = ChestBlock.getInventory(chestBlock, context.getBlockState(blockPos), context.getWorld(),
                context.getAbsolutePos(blockPos), false);

        // Assert
        try {
            context.assertEquals(inventory.size(), size,
                    String.format("%s single inventory size", chestBlock));
        } catch (Exception e) {
            ReinforcedChestsMod.LOGGER.error(e.getMessage());
            throw e;
        }

        context.complete();
    }

    private void testDoubleReinforcedChestInventorySize(TestContext context,
            ReinforcedChestBlock chestBlock, int size) {
        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos,
                chestBlock.getDefaultState().with(ChestBlock.CHEST_TYPE, ChestType.LEFT));
        context.setBlockState(blockPos.east(1),
                chestBlock.getDefaultState().with(ChestBlock.CHEST_TYPE, ChestType.RIGHT));

        // Act
        Inventory inventory = ChestBlock.getInventory(chestBlock, context.getBlockState(blockPos), context.getWorld(),
                context.getAbsolutePos(blockPos), false);

        // Assert
        try {
            context.assertEquals(inventory.size(), size,
                    String.format("%s double inventory size", chestBlock));
        } catch (Exception e) {
            ReinforcedChestsMod.LOGGER.error(e.getMessage());
            throw e;
        }

        context.complete();
    }

    /*
     * batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY
     */

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSingleCopperChestInventorySize(TestContext context) {
        testSingleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                45);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testDoubleCopperChestInventorySize(TestContext context) {
        testDoubleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                90);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSingleIronChestInventorySize(TestContext context) {
        testSingleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                54);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testDoubleIronChestInventorySize(TestContext context) {
        testDoubleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                108);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSingleGoldChestInventorySize(TestContext context) {
        testSingleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                81);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testDoubleGoldChestInventorySize(TestContext context) {
        testDoubleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                162);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSingleDiamondChestInventorySize(TestContext context) {
        testSingleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                108);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testDoubleDiamondChestInventorySize(TestContext context) {
        testDoubleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                216);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSingleNetheriteChestInventorySize(TestContext context) {
        testSingleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                108);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_INVENTORY, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testDoubleNetheriteChestInventorySize(TestContext context) {
        testDoubleReinforcedChestInventorySize(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                216);
    }
}
