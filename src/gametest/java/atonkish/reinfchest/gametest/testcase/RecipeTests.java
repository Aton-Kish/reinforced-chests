package atonkish.reinfchest.gametest.testcase;

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
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

import java.util.Arrays;
import java.util.List;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfchest.gametest.util.VoidScreenHander;
import atonkish.reinfchest.item.ModItems;
import atonkish.reinfcore.util.ReinforcingMaterials;

public class RecipeTests {
    private <C extends Inventory, T extends Recipe<C>> void testCraftReinforcedChest(TestContext context,
            RecipeType<T> type, C inventory, ItemStack expected) {
        // Arrange
        ServerWorld world = context.getWorld();
        RecipeManager recipeManager = world.getRecipeManager();
        DynamicRegistryManager registryManager = world.getRegistryManager();
        T recipe = recipeManager.getFirstMatch(type, inventory, world).orElseThrow().value();

        // Act
        ItemStack actual = recipe.craft(inventory, registryManager);

        // Assert
        try {
            context.assertEquals(actual.getItem(), expected.getItem(), "recipe result item");
            context.assertEquals(actual.getCount(), expected.getCount(), "recipe result count");
        } catch (Exception e) {
            ReinforcedChestsMod.LOGGER.error(e.getMessage());
            throw e;
        }

        context.complete();
    }

    /*
     * batchId = ReinforcedChestsModGameTest.BATCH_ID_RECIPE
     */

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCraftCopperChest(TestContext context) {
        ItemStack chest = new ItemStack(Items.CHEST);
        ItemStack material = new ItemStack(Items.COPPER_INGOT);

        RecipeInputInventory inventory = new CraftingInventory(new VoidScreenHander(), 3, 3);
        List<ItemStack> inputStacks = Arrays.asList(
                material, material, material,
                material, chest, material,
                material, material, material);
        for (int i = 0; i < inputStacks.size(); i++) {
            inventory.setStack(i, inputStacks.get(i));
        }

        ItemStack result = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));

        testCraftReinforcedChest(context, RecipeType.CRAFTING, inventory, result);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCraftIronChest(TestContext context) {
        ItemStack chest = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));
        ItemStack material = new ItemStack(Items.IRON_INGOT);

        RecipeInputInventory inventory = new CraftingInventory(new VoidScreenHander(), 3, 3);
        List<ItemStack> inputStacks = Arrays.asList(
                material, material, material,
                material, chest, material,
                material, material, material);
        for (int i = 0; i < inputStacks.size(); i++) {
            inventory.setStack(i, inputStacks.get(i));
        }

        ItemStack result = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));

        testCraftReinforcedChest(context, RecipeType.CRAFTING, inventory, result);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCraftGoldChest(TestContext context) {
        ItemStack chest = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));
        ItemStack material = new ItemStack(Items.GOLD_INGOT);

        RecipeInputInventory inventory = new CraftingInventory(new VoidScreenHander(), 3, 3);
        List<ItemStack> inputStacks = Arrays.asList(
                material, material, material,
                material, chest, material,
                material, material, material);
        for (int i = 0; i < inputStacks.size(); i++) {
            inventory.setStack(i, inputStacks.get(i));
        }

        ItemStack result = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));

        testCraftReinforcedChest(context, RecipeType.CRAFTING, inventory, result);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCraftDiamondChest(TestContext context) {
        ItemStack chest = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));
        ItemStack material = new ItemStack(Items.DIAMOND);

        RecipeInputInventory inventory = new CraftingInventory(new VoidScreenHander(), 3, 3);
        List<ItemStack> inputStacks = Arrays.asList(
                material, material, material,
                material, chest, material,
                material, material, material);
        for (int i = 0; i < inputStacks.size(); i++) {
            inventory.setStack(i, inputStacks.get(i));
        }

        ItemStack result = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));

        testCraftReinforcedChest(context, RecipeType.CRAFTING, inventory, result);
    }

    @GameTest(batchId = ReinforcedChestsModGameTest.BATCH_ID_RECIPE, templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testSmithingNetheriteChest(TestContext context) {
        ItemStack template = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        ItemStack chest = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));
        ItemStack material = new ItemStack(Items.NETHERITE_INGOT);

        Inventory inventory = new SimpleInventory(template, chest, material);

        ItemStack result = new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")));

        testCraftReinforcedChest(context, RecipeType.SMITHING, inventory, result);
    }
}
