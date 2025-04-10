package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfchest.gametest.util.TestIdentifier;
import atonkish.reinfchest.item.ModItems;

public class AdvancementTests {
    private static final String TEST_ENVIRONMENT_DEFAULT = String.format("%s:advancement/default",
            ReinforcedChestsMod.MOD_ID);
    private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Chest
            add(AdvancementTests.createTest(
                    "Obtain Copper Chest recipe advancement by having Chest",
                    Items.CHEST,
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/copper_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Copper Chest recipe advancement by having Copper Ingot",
                    Items.COPPER_INGOT,
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/copper_chest")));

            // Iron Chest
            add(AdvancementTests.createTest(
                    "Obtain Iron Chest recipe advancement by having Copper Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/iron_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Iron Chest recipe advancement by having Iron Ingot",
                    Items.IRON_INGOT,
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/iron_chest")));

            // Gold Chest
            add(AdvancementTests.createTest(
                    "Obtain Gold Chest recipe advancement by having Iron Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/gold_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Gold Chest recipe advancement by having Gold Ingot",
                    Items.GOLD_INGOT,
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/gold_chest")));

            // Diamond Chest
            add(AdvancementTests.createTest(
                    "Obtain Diamond Chest recipe advancement by having Gold Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/diamond_chest")));
            add(AdvancementTests.createTest(
                    "Obtain Diamond Chest recipe advancement by having Diamond",
                    Items.DIAMOND,
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/diamond_chest")));

            // Netherite Chest
            add(AdvancementTests.createTest(
                    "Obtain Netherite Chest recipe advancement by having Diamond Chest",
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/netherite_chest_smithing")));
            add(AdvancementTests.createTest(
                    "Obtain Netherite Chest recipe advancement by having Netherite Ingot",
                    Items.NETHERITE_INGOT,
                    Identifier.of(ReinforcedChestsMod.MOD_ID, "recipes/decorations/netherite_chest_smithing")));
        }
    };

    private static TestFunction createTest(String name, Item item, Identifier advancementId) {
        Identifier testIdentifier = TestIdentifier.of(ReinforcedChestsMod.MOD_ID,
                AdvancementTests.class,
                name);

        return new TestFunction(
                testIdentifier,
                AdvancementTests.TEST_ENVIRONMENT_DEFAULT,
                AdvancementTests.TEST_STRUCTURE_EMPTY,
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
                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL,
                            Vec3d.of(BlockPos.ORIGIN));
                    AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader().get(advancementId);
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
                            context.assertFalse(progressMap.get(progressMapKeyBeforeHavingItem),
                                    Text.of(String.format(
                                            "Expected that advancement %s has not been done yet, but it has been already done.",
                                            entry)));
                            context.assertTrue(progressMap.get(progressMapKeyAfterHavingItem),
                                    Text.of(String.format(
                                            "Expected that advancement %s has been done, but it has not been done yet.",
                                            entry)));
                        } catch (Exception e) {
                            ReinforcedChestsMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                            throw e;
                        } finally {
                            MockServerPlayerHelper.destroy(context, player);
                        }

                        context.complete();
                    });
                });
    }
}
