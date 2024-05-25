package atonkish.reinfchest.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.PositionedException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.concurrent.CompletableFuture;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class LootTableTests {
    private void testBreakReinforcedChestWithTool(TestContext context, Block chestBlock, Item tool,
            boolean shouldDrop) {
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
                    Direction.NORTH, context.getWorld().getTopY(), 0);

            futurePartialAct1.complete(null);
        });

        long tickBlockBreaking = (long) Math.ceil(
                1.0D / context.getBlockState(blockPos).calcBlockBreakingDelta(player,
                        context.getWorld(), blockPos));
        context.runAtTick(tickBlockBreaking, () -> {
            player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos),
                    PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                    Direction.NORTH, context.getWorld().getTopY(), 0);

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.expectBlock(Blocks.AIR, blockPos);
                context.expectEntitiesAround(EntityType.ITEM, blockPos, shouldDrop ? 1 : 0, 1);
            } catch (PositionedException e) {
                ReinforcedChestsMod.LOGGER.error(e.getMessage());
                throw e;
            } finally {
                MockServerPlayerHelper.destroy(context, player);
            }

            context.complete();
        });
    }

    /*
     * batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE
     */

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakCopperChestWithAxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                Items.NETHERITE_AXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakCopperChestWithPickaxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                Items.NETHERITE_PICKAXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakCopperChestWithoutTool(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                Items.AIR, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakIronChestWithAxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                Items.NETHERITE_AXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakIronChestWithPickaxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                Items.NETHERITE_PICKAXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakIronChestWithoutTool(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                Items.AIR, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakGoldChestWithAxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                Items.NETHERITE_AXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakGoldChestWithPickaxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                Items.NETHERITE_PICKAXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakGoldChestWithoutTool(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                Items.AIR, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakDiamondChestWithAxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                Items.NETHERITE_AXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakDiamondChestWithPickaxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                Items.NETHERITE_PICKAXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakDiamondChestWithoutTool(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                Items.AIR, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakNetheriteChestWithAxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                Items.NETHERITE_AXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakNetheriteChestWithPickaxe(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                Items.NETHERITE_PICKAXE, true);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_LOOT_TABLE, templateName = FabricGameTest.EMPTY_STRUCTURE, tickLimit = 1000)
    public void testBreakNetheriteChestWithoutTool(TestContext context) {
        testBreakReinforcedChestWithTool(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                Items.AIR, true);
    }
}
