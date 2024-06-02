package atonkish.reinfchest.gametest.testcase;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class PiglinTests {
    private void testPiglinAngerAfterOpeningReinforcedChest(TestContext context, Block chestBlock) {
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
                    piglin.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ANGRY_AT, player.getUuid()));

            context.useBlock(blockPos, player);

            futurePartialAct1.complete(null);
        });

        long tickAngryAtPlayer = 21;
        context.runAtTick(tickAngryAtPlayer, () -> {
            angryAtMap.put(angryAtMapKeyAfterAngryAtPlayer,
                    piglin.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ANGRY_AT, player.getUuid()));

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
                ReinforcedChestsMod.LOGGER.error(e.getMessage());
                throw e;
            } finally {
                MockServerPlayerHelper.destroy(context, player);
            }

            context.complete();
        });
    }

    /*
     * batchId = ReinforcedChestsModGameTest.BATCH_ID_PIGLIN
     */

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_PIGLIN, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPiglinAngerAfterOpeningCopperChest(TestContext context) {
        testPiglinAngerAfterOpeningReinforcedChest(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_PIGLIN, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPiglinAngerAfterOpeningIronChest(TestContext context) {
        testPiglinAngerAfterOpeningReinforcedChest(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_PIGLIN, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPiglinAngerAfterOpeningGoldChest(TestContext context) {
        testPiglinAngerAfterOpeningReinforcedChest(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_PIGLIN, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPiglinAngerAfterOpeningDiamondChest(TestContext context) {
        testPiglinAngerAfterOpeningReinforcedChest(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_PIGLIN, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testPiglinAngerAfterOpeningNetheriteChest(TestContext context) {
        testPiglinAngerAfterOpeningReinforcedChest(context,
                ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")));
    }
}
