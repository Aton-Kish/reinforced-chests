package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.gametest.util.MockServerPlayerHelper;
import atonkish.reinfchest.gametest.util.TestIdentifier;
import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class LootTableTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:loot_table/default", ReinforcedChestsMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Copper Chest
          add(
              LootTableTests.createTest(
                  "Break Copper Chest with Netherite Axe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                  Items.NETHERITE_AXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Copper Chest with Netherite Pickaxe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                  Items.NETHERITE_PICKAXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Copper Chest without tools",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                  Items.AIR,
                  true));

          // Iron Chest
          add(
              LootTableTests.createTest(
                  "Break Iron Chest with Netherite Axe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                  Items.NETHERITE_AXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Iron Chest with Netherite Pickaxe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                  Items.NETHERITE_PICKAXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Iron Chest without tools",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                  Items.AIR,
                  true));

          // Gold Chest
          add(
              LootTableTests.createTest(
                  "Break Gold Chest with Netherite Axe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                  Items.NETHERITE_AXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Gold Chest with Netherite Pickaxe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                  Items.NETHERITE_PICKAXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Gold Chest without tools",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                  Items.AIR,
                  true));

          // Diamond Chest
          add(
              LootTableTests.createTest(
                  "Break Diamond Chest with Netherite Axe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                  Items.NETHERITE_AXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Diamond Chest with Netherite Pickaxe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                  Items.NETHERITE_PICKAXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Diamond Chest without tools",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                  Items.AIR,
                  true));

          // Netherite Chest
          add(
              LootTableTests.createTest(
                  "Break Netherite Chest with Netherite Axe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                  Items.NETHERITE_AXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Netherite Chest with Netherite Pickaxe",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                  Items.NETHERITE_PICKAXE,
                  true));
          add(
              LootTableTests.createTest(
                  "Break Netherite Chest without tools",
                  ModBlocks.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                  Items.AIR,
                  true));
        }
      };

  private static TestFunction createTest(
      String name, Block chestBlock, Item tool, boolean shouldDrop) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedChestsMod.MOD_ID, LootTableTests.class, name);

    return new TestFunction(
        testIdentifier,
        LootTableTests.TEST_ENVIRONMENT_DEFAULT,
        LootTableTests.TEST_STRUCTURE_EMPTY,
        100,
        0,
        true,
        BlockRotation.NONE,
        false,
        1,
        1,
        false,
        (context) -> {
          // Arrange
          BlockPos blockPos = BlockPos.ORIGIN;
          context.setBlockState(blockPos, chestBlock);

          ServerPlayerEntity player =
              MockServerPlayerHelper.spawn(context, GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));
          player.setStackInHand(Hand.MAIN_HAND, new ItemStack(tool));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          long tickOrigin = 0;
          context.runAtTick(
              tickOrigin,
              () -> {
                player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos),
                    PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                    Direction.NORTH,
                    context.getWorld().getHeight(),
                    0);

                futurePartialAct1.complete(null);
              });

          long tickBlockBreaking =
              (long)
                  Math.ceil(
                      1.0D
                          / context
                              .getBlockState(blockPos)
                              .calcBlockBreakingDelta(player, context.getWorld(), blockPos));
          context.runAtTick(
              tickBlockBreaking,
              () -> {
                player.interactionManager.processBlockBreakingAction(
                    context.getAbsolutePos(blockPos),
                    PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                    Direction.NORTH,
                    context.getWorld().getHeight(),
                    0);

                futurePartialAct2.complete(null);
              });

          ReinforcedChestsMod.LOGGER.info(
              "[{}] {} can be mined in {} ticks by {}",
              testIdentifier,
              chestBlock.getName().getString(),
              tickBlockBreaking,
              tool.getName().getString());

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.expectBlock(Blocks.AIR, blockPos);
                      context.expectItemsAt(chestBlock.asItem(), blockPos, 1, shouldDrop ? 1 : 0);
                    } catch (Exception e) {
                      ReinforcedChestsMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                      throw e;
                    } finally {
                      // MockServerPlayerHelper.destroy(context, player);
                    }

                    context.complete();
                  });
        });
  }
}
