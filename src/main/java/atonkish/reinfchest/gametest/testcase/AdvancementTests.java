package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfchest.item.ModItems;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class AdvancementTests {
    private static final String BATCH_ID = String.format("%s:AdvancementBatch",
            ReinforcedChestsMod.MOD_ID);

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Chest
            add(AdvancementTests.createTest(
                    "Obtain Copper Chest recipe advancement by having Chest",
                    Items.CHEST,
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/copper_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Copper Chest recipe advancement by having Copper Ingot",
                    Items.COPPER_INGOT,
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/copper_chest")));

            // Iron Chest
            add(AdvancementTests.createTest(
                    "Obtain Iron Chest recipe advancement by having Copper Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/iron_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Iron Chest recipe advancement by having Iron Ingot",
                    Items.IRON_INGOT,
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/iron_chest")));

            // Gold Chest
            add(AdvancementTests.createTest(
                    "Obtain Gold Chest recipe advancement by having Iron Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/gold_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Gold Chest recipe advancement by having Gold Ingot",
                    Items.GOLD_INGOT,
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/gold_chest")));

            // Diamond Chest
            add(AdvancementTests.createTest(
                    "Obtain Diamond Chest recipe advancement by having Gold Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/diamond_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Diamond Chest recipe advancement by having Diamond",
                    Items.DIAMOND,
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/diamond_chest")));

            // Netherite Chest
            add(AdvancementTests.createTest(
                    "Obtain Netherite Chest recipe advancement by having Diamond Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/netherite_chest_smithing")));
            add(AdvancementTests.createTest(
                    "Obtain Netherite Chest recipe advancement by having Netherite Ingot",
                    Items.NETHERITE_INGOT,
                    new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/netherite_chest_smithing")));
        }
    };

    private static TestFunction createTest(String name, Item item, Identifier advancementId) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                AdvancementTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                AdvancementTests.BATCH_ID,
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
                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL, Vec3d.of(BlockPos.ORIGIN));
                    AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                            .get(advancementId);
                    AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);

                    // Act
                    CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
                    CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

                    Map<String, Boolean> progressMap = new HashMap<String, Boolean>();
                    String progressMapKeyBeforeHavingItem = "beforeHavingItem";
                    String progressMapKeyAfterHavingItem = "afterHavingItem";

                    long tickOrigin = 0;
                    context.runAtTick(tickOrigin, () -> {
                        progressMap.put(progressMapKeyBeforeHavingItem, progress.isDone());

                        player.giveItemStack(new ItemStack(item));

                        futurePartialAct1.complete(null);
                    });

                    long tickObtained = 1;
                    context.runAtTick(tickObtained, () -> {
                        progressMap.put(progressMapKeyAfterHavingItem, progress.isDone());

                        futurePartialAct2.complete(null);
                    });

                    // Assert
                    CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
                        try {
                            context.assertFalse(progressMap.get(progressMapKeyBeforeHavingItem), String.format(
                                    "Expected that advancement %s has not been done yet, but it has been already done.",
                                    entry));
                            context.assertTrue(progressMap.get(progressMapKeyAfterHavingItem), String.format(
                                    "Expected that advancement %s has been done, but it has not been done yet.",
                                    entry));
                        } catch (Exception e) {
                            ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                            throw e;
                        } finally {
                            MockServerPlayerHelper.destroy(context, player);
                        }

                        context.complete();
                    });
                });
    }
}
