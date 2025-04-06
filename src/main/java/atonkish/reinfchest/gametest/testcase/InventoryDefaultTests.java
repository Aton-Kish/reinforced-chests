package atonkish.reinfchest.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.inventory.Inventory;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;

public class InventoryDefaultTests implements CustomTestMethodInvoker {
    public void invokeTestMethod(TestContext context, Method method) {
        try {
            method.invoke(this, context);
        } catch (InvocationTargetException e) {
            // Ensure that any GameTestException are propagated without wrapping
            if (e.getTargetException() instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            throw new RuntimeException("Failed to invoke test method", e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to invoke test method", e);
        }
    }

    private void singleChestTest(TestContext context, ChestBlock chestBlock, int size) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, chestBlock);

        // Act
        Inventory inventory = ChestBlock.getInventory(chestBlock, context.getBlockState(blockPos),
                context.getWorld(), context.getAbsolutePos(blockPos), false);

        // Assert
        try {
            context.assertEquals(inventory.size(), size,
                    Text.of(String.format("%s single inventory size", chestBlock)));
        } catch (Exception e) {
            ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
        }

        context.complete();
    }

    private void doubleChestTest(TestContext context, ChestBlock chestBlock, int size) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

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
                    Text.of(String.format("%s double inventory size", chestBlock)));
        } catch (Exception e) {
            ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
        }

        context.complete();
    }

    //
    // Copper Chest
    //

    @GameTest
    public void singleCopperChestInventorySize(TestContext context) {
        this.singleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                45);
    }

    @GameTest
    public void doubleCopperChestInventorySize(TestContext context) {
        this.doubleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                90);
    }

    //
    // Iron Chest
    //

    @GameTest
    public void singleIronChestInventorySize(TestContext context) {
        this.singleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                54);
    }

    @GameTest
    public void doubleIronChestInventorySize(TestContext context) {
        this.doubleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                108);
    }

    //
    // Gold Chest
    //

    @GameTest
    public void singleGoldChestInventorySize(TestContext context) {
        this.singleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                81);
    }

    @GameTest
    public void doubleGoldChestInventorySize(TestContext context) {
        this.doubleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                162);
    }

    //
    // Diamond Chest
    //

    @GameTest
    public void singleDiamondChestInventorySize(TestContext context) {
        this.singleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                108);
    }

    @GameTest
    public void doubleDiamondChestInventorySize(TestContext context) {
        this.doubleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                216);
    }

    //
    // Netherite Chest
    //

    @GameTest
    public void singleNetheriteChestInventorySize(TestContext context) {
        this.singleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                108);
    }

    @GameTest
    public void doubleNetheriteChestInventorySize(TestContext context) {
        this.doubleChestTest(context,
                (ChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                216);
    }
}
