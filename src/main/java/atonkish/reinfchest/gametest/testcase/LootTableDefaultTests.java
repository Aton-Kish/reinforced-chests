package atonkish.reinfchest.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;

public class LootTableDefaultTests implements CustomTestMethodInvoker {

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

    private void test(TestContext context, Block chestBlock, Item tool, boolean shouldDrop) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, chestBlock);

        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));
        player.setStackInHand(Hand.MAIN_HAND, new ItemStack(tool));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos), PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                    Direction.NORTH, context.getWorld().getHeight(), 0);

            futurePartialAct1.complete(null);
        });

        long tickBlockBreaking = (long) Math.ceil(
                1.0D / context.getBlockState(blockPos).calcBlockBreakingDelta(player,
                        context.getWorld(), blockPos));
        context.runAtTick(tickBlockBreaking, () -> {
            player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos),
                    PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                    Direction.NORTH, context.getWorld().getHeight(), 0);

            futurePartialAct2.complete(null);
        });

        ReinforcedChestsMod.LOGGER.info("[{}] {} can be mined in {} ticks by {}",
                testName,
                chestBlock.getName().getString(),
                tickBlockBreaking,
                tool.getName().getString());

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.expectBlock(Blocks.AIR, blockPos);
                context.expectItemsAt(chestBlock.asItem(), blockPos, 1, shouldDrop ? 1 : 0);
            } catch (Exception e) {
                ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                throw e;
            } finally {
                MockServerPlayerHelper.destroy(context, player);
            }

            context.complete();
        });
    }

    //
    // Copper Chest
    //

    @GameTest(maxTicks = 1000)
    public void breakCopperChestWithNetheriteAxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCopperChestWithNetheritePickaxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCopperChestWithoutTools(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                Items.AIR,
                true);
    }

    //
    // Iron Chest
    //

    @GameTest(maxTicks = 1000)
    public void breakIronChestWithNetheriteAxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakIronChestWithNetheritePickaxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakIronChestWithoutTools(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                Items.AIR,
                true);
    }

    //
    // Gold Chest
    //

    @GameTest(maxTicks = 1000)
    public void breakGoldChestWithNetheriteAxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGoldChestWithNetheritePickaxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGoldChestWithoutTools(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                Items.AIR,
                true);
    }

    //
    // Diamond Chest
    //

    @GameTest(maxTicks = 1000)
    public void breakDiamondChestWithNetheriteAxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakDiamondChestWithNetheritePickaxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakDiamondChestWithoutTools(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                Items.AIR,
                true);
    }

    //
    // Netherite Chest
    //

    @GameTest(maxTicks = 1000)
    public void breakNetheriteChestWithNetheriteAxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakNetheriteChestWithNetheritePickaxe(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakNetheriteChestWithoutTools(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                Items.AIR,
                true);
    }
}
