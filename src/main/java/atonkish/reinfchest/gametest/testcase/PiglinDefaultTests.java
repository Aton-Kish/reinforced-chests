package atonkish.reinfchest.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;

public class PiglinDefaultTests implements CustomTestMethodInvoker {
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

    private void test(TestContext context, Block chestBlock) {
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
        player.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));

        PiglinEntity piglin = context.spawnMob(EntityType.PIGLIN, blockPos.east(1));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        Map<String, Boolean> angryAtMap = new HashMap<String, Boolean>();
        String angryAtMapKeyBeforeAngryAtPlayer = "beforeAngryAtPlayer";
        String angryAtMapKeyAfterAngryAtPlayer = "afterAngryAtPlayer";

        long tickChestOpen = 20;
        context.runAtTick(tickChestOpen, () -> {
            angryAtMap.put(angryAtMapKeyBeforeAngryAtPlayer,
                    piglin.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ANGRY_AT,
                            player.getUuid()));

            context.useBlock(blockPos, player);

            futurePartialAct1.complete(null);
        });

        long tickAngryAtPlayer = 21;
        context.runAtTick(tickAngryAtPlayer, () -> {
            angryAtMap.put(angryAtMapKeyAfterAngryAtPlayer,
                    piglin.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ANGRY_AT,
                            player.getUuid()));

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.assertFalse(angryAtMap.get(angryAtMapKeyBeforeAngryAtPlayer),
                        Text.of("Expected that the piglin is not angry at player, but it has been already angry."));
                context.assertTrue(angryAtMap.get(angryAtMapKeyAfterAngryAtPlayer),
                        Text.of("Expected that the piglin is angry at player, but it has not been angry yet."));
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

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningCopperChest(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("copper")));
    }

    //
    // Iron Chest
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningIronChest(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("iron")));
    }

    //
    // Gold Chest
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGoldChest(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("gold")));
    }

    //
    // Diamond Chest
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningDiamondChest(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond")));
    }

    //
    // Netherite Chest
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningNetheriteChest(TestContext context) {
        this.test(context,
                ModBlocks.REINFORCED_CHEST_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite")));
    }
}
