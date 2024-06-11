package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfcore.util.ReinforcingMaterials;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.inventory.Inventory;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.math.BlockPos;

public class InventoryTests {
    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>();

    static {
        // Copper Chest
        InventoryTests.registerSingleChest(
                "Single Copper Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                45);
        InventoryTests.registerDoubleChest(
                "Double Copper Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                90);

        // Iron Chest
        InventoryTests.registerSingleChest(
                "Single Iron Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                54);
        InventoryTests.registerDoubleChest(
                "Double Iron Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                108);

        // Gold Chest
        InventoryTests.registerSingleChest(
                "Single Gold Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                81);
        InventoryTests.registerDoubleChest(
                "Double Gold Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                162);

        // Diamond Chest
        InventoryTests.registerSingleChest(
                "Single Diamond Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                108);
        InventoryTests.registerDoubleChest(
                "Double Diamond Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                216);

        // Netherite Chest
        InventoryTests.registerSingleChest(
                "Single Netherite Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                108);
        InventoryTests.registerDoubleChest(
                "Double Netherite Chest inventory size",
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                216);
    }

    private static void registerSingleChest(String name, ChestBlock chestBlock, int size) {
        String testName = String.format("%s: %s", InventoryTests.class.getSimpleName(), name);
        TEST_FUNCTIONS.add(new TestFunction(
                ReinforcedChestsModGameTest.BATCH_ID_INVENTORY,
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
                }));
    }

    private static void registerDoubleChest(String name, ChestBlock chestBlock, int size) {
        String testName = String.format("%s: %s", InventoryTests.class.getSimpleName(), name);
        TEST_FUNCTIONS.add(new TestFunction(
                ReinforcedChestsModGameTest.BATCH_ID_INVENTORY,
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
                }));
    }
}
