package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.util.TestIdentifier;

public class InventoryTests {
    private static final String TEST_ENVIRONMENT_DEFAULT = String.format("%s:inventory/default",
            ReinforcedChestsMod.MOD_ID);
    private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

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
        Identifier testIdentifier = TestIdentifier.of(ReinforcedChestsMod.MOD_ID,
                InventoryTests.class,
                name);

        return new TestFunction(
                testIdentifier,
                InventoryTests.TEST_ENVIRONMENT_DEFAULT,
                InventoryTests.TEST_STRUCTURE_EMPTY,
                20,
                0,
                true,
                BlockRotation.NONE,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos = BlockPos.ORIGIN;
                    context.setBlockState(blockPos, chestBlock);

                    // Act
                    Inventory inventory = ChestBlock.getInventory(chestBlock,
                            context.getBlockState(blockPos),
                            context.getWorld(),
                            context.getAbsolutePos(blockPos),
                            false);

                    // Assert
                    try {
                        context.assertEquals(inventory.size(), size,
                                Text.of(String.format("%s single inventory size", chestBlock)));
                    } catch (Exception e) {
                        ReinforcedChestsMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }

    private static TestFunction createTestDoubleChest(String name, ChestBlock chestBlock, int size) {
        Identifier testIdentifier = TestIdentifier.of(ReinforcedChestsMod.MOD_ID,
                InventoryTests.class,
                name);

        return new TestFunction(
                testIdentifier,
                InventoryTests.TEST_ENVIRONMENT_DEFAULT,
                InventoryTests.TEST_STRUCTURE_EMPTY,
                20,
                0,
                true,
                BlockRotation.NONE,
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
                    Inventory inventory = ChestBlock.getInventory(chestBlock,
                            context.getBlockState(blockPos),
                            context.getWorld(),
                            context.getAbsolutePos(blockPos),
                            false);

                    // Assert
                    try {
                        context.assertEquals(inventory.size(), size,
                                Text.of(String.format("%s double inventory size", chestBlock)));
                    } catch (Exception e) {
                        ReinforcedChestsMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }
}
