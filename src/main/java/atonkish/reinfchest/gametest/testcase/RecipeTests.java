package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.util.VoidScreenHander;
import atonkish.reinfchest.item.ModItems;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class RecipeTests {
    private static final String BATCH_ID = String.format("%s:RecipeBatch",
            ReinforcedChestsMod.MOD_ID);

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Chest
            {
                ItemStack baseChest = new ItemStack(Items.CHEST);
                ItemStack material = new ItemStack(Items.COPPER_INGOT);
                ItemStack chest = new ItemStack(
                        ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));

                add(RecipeTests.createTest(
                        "Craft Copper Chest",
                        RecipeType.CRAFTING,
                        RecipeTests.create3x3CraftingInventory(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material),
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
                        RecipeTests.create3x3CraftingInventory(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material),
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
                        RecipeTests.create3x3CraftingInventory(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material),
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
                        RecipeTests.create3x3CraftingInventory(
                                material, material, material,
                                material, baseChest, material,
                                material, material, material),
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
                        new SimpleInventory(template, baseChest, material),
                        chest));
            }
        }
    };

    private static <C extends Inventory, T extends Recipe<C>> TestFunction createTest(String name, RecipeType<T> type,
            C inventory, ItemStack expected) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                RecipeTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                RecipeTests.BATCH_ID,
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
                    ServerWorld world = context.getWorld();
                    RecipeManager recipeManager = world.getRecipeManager();
                    DynamicRegistryManager registryManager = world.getRegistryManager();
                    T recipe = recipeManager.getFirstMatch(type, inventory, world).orElseThrow().value();

                    // Act
                    ItemStack actual = recipe.craft(inventory, registryManager);

                    // Assert
                    try {
                        context.assertTrue(ItemStack.areEqual(actual, expected),
                                "Recipe result differs from expected.");
                    } catch (Exception e) {
                        ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }

    private static RecipeInputInventory create3x3CraftingInventory(
            ItemStack stack1, ItemStack stack2, ItemStack stack3,
            ItemStack stack4, ItemStack stack5, ItemStack stack6,
            ItemStack stack7, ItemStack stack8, ItemStack stack9) {
        RecipeInputInventory inventory = new CraftingInventory(new VoidScreenHander(), 3, 3);
        inventory.setStack(0, stack1);
        inventory.setStack(1, stack2);
        inventory.setStack(2, stack3);
        inventory.setStack(3, stack4);
        inventory.setStack(4, stack5);
        inventory.setStack(5, stack6);
        inventory.setStack(6, stack7);
        inventory.setStack(7, stack8);
        inventory.setStack(8, stack9);
        return inventory;
    }
}
