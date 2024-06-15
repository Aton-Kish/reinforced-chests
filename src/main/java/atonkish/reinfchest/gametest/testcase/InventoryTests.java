package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.inventory.Inventory;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class InventoryTests {
    private static final String BATCH_ID = String.format("%s:InventoryBatch",
            ReinforcedChestsMod.MOD_ID);

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Chest
            add(InventoryTests.createTestSingleChest(
                    "Single Copper Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                    45));
            add(InventoryTests.createTestDoubleChest(
                    "Double Copper Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                    90));

            // Iron Chest
            add(InventoryTests.createTestSingleChest(
                    "Single Iron Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                    54));
            add(InventoryTests.createTestDoubleChest(
                    "Double Iron Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                    108));

            // Gold Chest
            add(InventoryTests.createTestSingleChest(
                    "Single Gold Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                    81));
            add(InventoryTests.createTestDoubleChest(
                    "Double Gold Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                    162));

            // Diamond Chest
            add(InventoryTests.createTestSingleChest(
                    "Single Diamond Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                    108));
            add(InventoryTests.createTestDoubleChest(
                    "Double Diamond Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                    216));

            // Netherite Chest
            add(InventoryTests.createTestSingleChest(
                    "Single Netherite Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                    108));
            add(InventoryTests.createTestDoubleChest(
                    "Double Netherite Chest inventory size",
                    (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                    216));
        }
    };

    private static TestFunction createTestSingleChest(String name, ChestBlock chestBlock, int size) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                InventoryTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                InventoryTests.BATCH_ID,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                100,
                0L,
                true,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos = BlockPos.ORIGIN;
                    context.setBlockState(blockPos, chestBlock);

                    // Act
                    Inventory inventory = ChestBlock.getInventory(chestBlock, context.getBlockState(blockPos),
                            context.getWorld(), context.getAbsolutePos(blockPos), false);

                    // Assert
                    try {
                        context.assertEquals(inventory.size(), size,
                                String.format("%s single inventory size", chestBlock));
                    } catch (Exception e) {
                        ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }

    private static TestFunction createTestDoubleChest(String name, ChestBlock chestBlock, int size) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                InventoryTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                InventoryTests.BATCH_ID,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                100,
                0L,
                true,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos = BlockPos.ORIGIN;
                    context.setBlockState(blockPos,
                            chestBlock.getDefaultState().with(ChestBlock.CHEST_TYPE, ChestType.LEFT));
                    context.setBlockState(blockPos.east(1),
                            chestBlock.getDefaultState().with(ChestBlock.CHEST_TYPE, ChestType.RIGHT));

                    // Act
                    Inventory inventory = ChestBlock.getInventory(chestBlock, context.getBlockState(blockPos),
                            context.getWorld(), context.getAbsolutePos(blockPos), false);

                    // Assert
                    try {
                        context.assertEquals(inventory.size(), size,
                                String.format("%s double inventory size", chestBlock));
                    } catch (Exception e) {
                        ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }
}
