package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfcore.util.ReinforcingMaterials;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class PiglinTests {
    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>();

    static {
        // Copper Chest
        PiglinTests.register(
                "Piglin get angry after opening Copper Chest",
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));

        // Iron Chest
        PiglinTests.register(
                "Piglin get angry after opening Iron Chest",
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));

        // Gold Chest
        PiglinTests.register(
                "Piglin get angry after opening Gold Chest",
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));

        // Diamond Chest
        PiglinTests.register(
                "Piglin get angry after opening Diamond Chest",
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));

        // Netherite Chest
        PiglinTests.register(
                "Piglin get angry after opening Netherite Chest",
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")));
    }

    private static void register(String name, Block chestBlock) {
        String testName = String.format("%s: %s", PiglinTests.class.getSimpleName(), name);
        TEST_FUNCTIONS.add(new TestFunction(
                ReinforcedChestsModGameTest.BATCH_ID_PIGLIN,
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

                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));
                    ArmorItem armor = (ArmorItem) Items.GOLDEN_CHESTPLATE;
                    player.equipStack(armor.getSlotType(), new ItemStack(armor));

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
                                    "Expected that the piglin is not angry at player, but it has been already angry.");
                            context.assertTrue(angryAtMap.get(angryAtMapKeyAfterAngryAtPlayer),
                                    "Expected that the piglin is angry at player, but it has not been angry yet.");
                        } catch (Exception e) {
                            ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                            throw e;
                        } finally {
                            MockServerPlayerHelper.destroy(context, player);
                        }

                        context.complete();
                    });
                }));
    }
}
