package atonkish.reinfchest.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.ReinforcedChestBlock;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfchest.stat.ModStats;

public class OpenDefaultTests implements CustomTestMethodInvoker {
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

    private void test(TestContext context, ReinforcedChestBlock chestBlock) {
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
                context.assertEquals(
                        statMap.get(statMapKeyAfterOpening) - statMap.get(statMapKeyBeforeOpening), 1,
                        Text.of(String.format("diff %s value", stat.getName())));
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

    @GameTest
    public void openCopperChest(TestContext context) {
        this.test(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("copper")));
    }

    //
    // Iron Chest
    //

    @GameTest
    public void openIronChest(TestContext context) {
        this.test(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("iron")));
    }

    //
    // Gold Chest
    //

    @GameTest
    public void openGoldChest(TestContext context) {
        this.test(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("gold")));
    }

    //
    // Diamond Chest
    //

    @GameTest
    public void openDiamondChest(TestContext context) {
        this.test(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond")));
    }

    //
    // Netherite Chest
    //

    @GameTest
    public void openNetheriteChest(TestContext context) {
        this.test(context,
                (ReinforcedChestBlock) ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite")));
    }
}
