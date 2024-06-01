package atonkish.reinfchest.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.PositionedException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfchest.item.ModItems;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class AdvancementTests {
    private void testObtainReinforcedChestAdvancement(TestContext context, Item item, AdvancementEntry entry) {
        // Arrange
        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(BlockPos.ORIGIN));
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
                        "Expected that advancement %s has not been done yet, but it has been already done.", entry));
                context.assertTrue(progressMap.get(progressMapKeyAfterHavingItem), String.format(
                        "Expected that advancement %s has been done, but it has not been done yet.", entry));
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
     * batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT
     */

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainCopperChestRecipeAdvancementByChest(TestContext context) {
        Item item = Items.CHEST;
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/copper_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainCopperChestRecipeAdvancementByCopperIngot(TestContext context) {
        Item item = Items.COPPER_INGOT;
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/copper_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainIronChestRecipeAdvancementByCopperChest(TestContext context) {
        Item item = ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper"));
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/iron_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainIronChestRecipeAdvancementByIronIngot(TestContext context) {
        Item item = Items.IRON_INGOT;
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/iron_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainGoldChestRecipeAdvancementByIronChest(TestContext context) {
        Item item = ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron"));
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/gold_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainGoldChestRecipeAdvancementByGoldIngot(TestContext context) {
        Item item = Items.GOLD_INGOT;
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/gold_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainDiamondChestRecipeAdvancementByGoldChest(TestContext context) {
        Item item = ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold"));
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/diamond_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainDiamondChestRecipeAdvancementByDiamond(TestContext context) {
        Item item = Items.DIAMOND;
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/diamond_chest"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainNetheriteChestRecipeAdvancementByDiamondChest(TestContext context) {
        Item item = ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond"));
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/netherite_chest_smithing"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_ADVANCEMENT, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testObtainNetheriteChestRecipeAdvancementByNetheriteIngot(TestContext context) {
        Item item = Items.NETHERITE_INGOT;
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader()
                .get(new Identifier(ReinforcedChestsMod.MOD_ID, "recipes/decorations/netherite_chest_smithing"));

        testObtainReinforcedChestAdvancement(context, item, entry);
    }
}
