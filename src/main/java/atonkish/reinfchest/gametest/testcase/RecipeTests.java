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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.util.TestIdentifier;
import atonkish.reinfchest.item.ModItems;

public class RecipeTests {
    private static final String TEST_ENVIRONMENT_DEFAULT = String.format("%s:recipe/default",
            ReinforcedChestsMod.MOD_ID);
    private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Chest (Vanilla)
            {
                ItemStack baseChest = new ItemStack(Items.CHEST);
                ItemStack material = new ItemStack(Items.COPPER_INGOT);
                ItemStack chest = new ItemStack(Items.COPPER_CHEST);

                add(RecipeTests.createTest(
                        "Craft Vanilla Copper Chest",
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                        Identifier.ofVanilla("copper_chest"),
                        chest));
            }

            // Copper Chest
            {
                ItemStack baseChest = new ItemStack(Items.CHEST);
                ItemStack material = new ItemStack(Items.COPPER_INGOT);
                ItemStack chest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));

                add(RecipeTests.createTest(
                        "Craft Copper Chest",
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                        Identifier.of(ReinforcedChestsMod.MOD_ID, "copper_chest"),
                        chest));
            }

            // Iron Chest
            {
                ItemStack baseChest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));
                ItemStack material = new ItemStack(Items.IRON_INGOT);
                ItemStack chest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));

                add(RecipeTests.createTest(
                        "Craft Iron Chest",
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                        Identifier.of(ReinforcedChestsMod.MOD_ID, "iron_chest"),
                        chest));
            }

            // Gold Chest
            {
                ItemStack baseChest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));
                ItemStack material = new ItemStack(Items.GOLD_INGOT);
                ItemStack chest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));

                add(RecipeTests.createTest(
                        "Craft Gold Chest",
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                        Identifier.of(ReinforcedChestsMod.MOD_ID, "gold_chest"),
                        chest));
            }

            // Diamond Chest
            {
                ItemStack baseChest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));
                ItemStack material = new ItemStack(Items.DIAMOND);
                ItemStack chest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));

                add(RecipeTests.createTest(
                        "Craft Diamond Chest",
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                        Identifier.of(ReinforcedChestsMod.MOD_ID, "diamond_chest"),
                        chest));
            }

            // Netherite Chest
            {
                ItemStack template = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                ItemStack baseChest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));
                ItemStack material = new ItemStack(Items.NETHERITE_INGOT);
                ItemStack chest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")));

                add(RecipeTests.createTest(
                        "Smithing Netherite Chest",
                        RecipeType.SMITHING,
                        new SmithingRecipeInput(template, baseChest, material),
                        Identifier.of(ReinforcedChestsMod.MOD_ID, "netherite_chest_smithing"),
                        chest));
            }
        }
    };

    private static <I extends RecipeInput, T extends Recipe<I>> TestFunction createTest(String name,
            RecipeType<T> type, I input, Identifier recipeId, ItemStack expected) {
        Identifier testIdentifier = TestIdentifier.of(ReinforcedChestsMod.MOD_ID,
                RecipeTests.class,
                name);

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
                    T recipe = recipeManager
                            .getFirstMatch(type, input, world, RegistryKey.of(RegistryKeys.RECIPE, recipeId))
                            .orElseThrow()
                            .value();

                    // Act
                    ItemStack actual = recipe.craft(input, registryManager);

                    // Assert
                    try {
                        context.assertTrue(ItemStack.areEqual(actual, expected),
                                Text.of("Recipe result differs from expected."));
                    } catch (Exception e) {
                        ReinforcedChestsMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }
}
