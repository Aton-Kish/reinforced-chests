package atonkish.reinfchest.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.ReinforcedChestBlock;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfchest.stat.ModStats;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class StatTests {
    private void testOpenReinforcedChest(TestContext context, ReinforcedChestBlock chestBlock) {
        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, chestBlock);

        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));

        Stat<Identifier> stat = Stats.CUSTOM
                .getOrCreateStat(ModStats.OPEN_REINFORCED_CHEST_MAP.get(chestBlock.getMaterial()));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        Map<String, Integer> statMap = new HashMap<String, Integer>();
        String statMapKeyBeforeOpening = "beforeOpening";
        String statMapKeyAfterOpening = "afterOpening";

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            statMap.put(statMapKeyBeforeOpening, player.getStatHandler().getStat(stat));

            context.useBlock(blockPos, player);

            futurePartialAct1.complete(null);
        });

        long tickChestOpening = 1;
        context.runAtTick(tickChestOpening, () -> {
            statMap.put(statMapKeyAfterOpening, player.getStatHandler().getStat(stat));

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.assertEquals(statMap.get(statMapKeyAfterOpening) - statMap.get(statMapKeyBeforeOpening), 1,
                        String.format("diff %s value", stat.getName()));
            } catch (Exception e) {
                ReinforcedChestsMod.LOGGER.error(e.getMessage());
                throw e;
            } finally {
                MockServerPlayerHelper.destroy(context, player);
            }

            context.complete();
        });
    }

    /*
     * batchId = ReinforcedChestsModGameTest.BATCH_ID_STAT
     */

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_STAT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testOpenCopperChest(TestContext context) {
        testOpenReinforcedChest(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_STAT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testOpenIronChest(TestContext context) {
        testOpenReinforcedChest(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_STAT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testOpenGoldChest(TestContext context) {
        testOpenReinforcedChest(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_STAT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testOpenDiamondChest(TestContext context) {
        testOpenReinforcedChest(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_STAT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testOpenNetheriteChest(TestContext context) {
        testOpenReinforcedChest(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")));
    }
}
