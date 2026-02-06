package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.util.TestIdentifier;
import atonkish.reinfchest.item.ModItems;
import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class RecipeTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:recipe/default", ReinforcedChestsMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Iron Chest
          {
            ItemStack baseChest;
            ItemStack material = new ItemStack(Items.IRON_INGOT);
            ItemStack chest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));

            // from Modded Copper Chest (for backward compatible)
            baseChest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Copper Chest
            baseChest = new ItemStack(Items.COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Exposed Copper Chest
            baseChest = new ItemStack(Items.EXPOSED_COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Exposed Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Weathered Copper Chest
            baseChest = new ItemStack(Items.WEATHERED_COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Weathered Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Oxidized Copper Chest
            baseChest = new ItemStack(Items.OXIDIZED_COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Oxidized Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Waxed Copper Chest
            baseChest = new ItemStack(Items.WAXED_COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Waxed Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Waxed Exposed Copper Chest
            baseChest = new ItemStack(Items.WAXED_EXPOSED_COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Waxed Exposed Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Waxed Weathered Copper Chest
            baseChest = new ItemStack(Items.WAXED_WEATHERED_COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Waxed Weathered Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));

            // from Waxed Oxidized Copper Chest
            baseChest = new ItemStack(Items.WAXED_OXIDIZED_COPPER_CHEST);
            add(
                RecipeTests.createTest(
                    "Craft Iron Chest from Waxed Oxidized Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));
          }

          // Gold Chest
          {
            ItemStack baseChest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));
            ItemStack material = new ItemStack(Items.GOLD_INGOT);
            ItemStack chest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));

            add(
                RecipeTests.createTest(
                    "Craft Gold Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));
          }

          // Diamond Chest
          {
            ItemStack baseChest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));
            ItemStack material = new ItemStack(Items.DIAMOND);
            ItemStack chest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));

            add(
                RecipeTests.createTest(
                    "Craft Diamond Chest",
                    RecipeType.CRAFTING,
                    CraftingRecipeInput.create(
                        3,
                        3,
                        List.of(
                            material, material, material, material, baseChest, material, material,
                            material, material)),
                    chest));
          }

          // Netherite Chest
          {
            ItemStack template = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
            ItemStack baseChest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));
            ItemStack material = new ItemStack(Items.NETHERITE_INGOT);
            ItemStack chest =
                new ItemStack(
                    ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")));

            add(
                RecipeTests.createTest(
                    "Smithing Netherite Chest",
                    RecipeType.SMITHING,
                    new SmithingRecipeInput(template, baseChest, material),
                    chest));
          }
        }
      };

  private static <I extends RecipeInput, T extends Recipe<I>> TestFunction createTest(
      String name, RecipeType<T> type, I input, ItemStack expected) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedChestsMod.MOD_ID, RecipeTests.class, name);

    return new TestFunction(
        testIdentifier,
        RecipeTests.TEST_ENVIRONMENT_DEFAULT,
        RecipeTests.TEST_STRUCTURE_EMPTY,
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
          ServerWorld world = context.getWorld();
          ServerRecipeManager recipeManager = world.getRecipeManager();
          DynamicRegistryManager registryManager = world.getRegistryManager();
          T recipe = recipeManager.getFirstMatch(type, input, world).orElseThrow().value();

          // Act
          ItemStack actual = recipe.craft(input, registryManager);

          // Assert
          try {
            context.assertTrue(
                ItemStack.areEqual(actual, expected),
                Text.of("Recipe result differs from expected."));
          } catch (Exception e) {
            ReinforcedChestsMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
            throw e;
          }

          context.complete();
        });
  }
}
