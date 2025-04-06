package atonkish.reinfchest.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.item.ModItems;

public class RecipeDefaultTests implements CustomTestMethodInvoker {
    public void invokeTestMethod(TestContext context, Method method) {
        try {
            method.invoke(this, context);
        } catch (InvocationTargetException e) {
            // Ensure that any GameTestException are propagated without wrapping
            if (e.getTargetException() instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            throw new RuntimeException("Failed to invoke test method", e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to invoke test method", e);
        }
    }

    private <I extends RecipeInput, T extends Recipe<I>> void test(TestContext context,
            RecipeType<T> type, I input, ItemStack expected) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        ServerWorld world = context.getWorld();
        ServerRecipeManager recipeManager = world.getRecipeManager();
        DynamicRegistryManager registryManager = world.getRegistryManager();
        T recipe = recipeManager.getFirstMatch(type, input, world).orElseThrow().value();

        // Act
        ItemStack actual = recipe.craft(input, registryManager);

        // Assert
        try {
            context.assertTrue(ItemStack.areEqual(actual, expected),
                    Text.of("Recipe result differs from expected."));
        } catch (Exception e) {
            ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
        }

        context.complete();
    }

    //
    // Copper Chest
    //

    @GameTest
    public void craftCopperChest(TestContext context) {
        ItemStack baseChest = new ItemStack(Items.CHEST);
        ItemStack material = new ItemStack(Items.COPPER_INGOT);
        ItemStack chest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));
        test(context,
                RecipeType.CRAFTING,
                CraftingRecipeInput.create(3, 3,
                        List.of(material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                chest);
    }

    //
    // Iron Chest
    //

    @GameTest
    public void craftIronChest(TestContext context) {
        ItemStack baseChest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")));
        ItemStack material = new ItemStack(Items.IRON_INGOT);
        ItemStack chest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));
        test(context,
                RecipeType.CRAFTING,
                CraftingRecipeInput.create(3, 3,
                        List.of(material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                chest);
    }

    //
    // Gold Chest
    //

    @GameTest
    public void craftGoldChest(TestContext context) {
        ItemStack baseChest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")));
        ItemStack material = new ItemStack(Items.GOLD_INGOT);
        ItemStack chest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));
        test(context,
                RecipeType.CRAFTING,
                CraftingRecipeInput.create(3, 3,
                        List.of(material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                chest);
    }

    //
    // Diamond Chest
    //

    @GameTest
    public void craftDiamondChest(TestContext context) {
        ItemStack baseChest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")));
        ItemStack material = new ItemStack(Items.DIAMOND);
        ItemStack chest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));
        test(context,
                RecipeType.CRAFTING,
                CraftingRecipeInput.create(3, 3,
                        List.of(material, material, material,
                                material, baseChest, material,
                                material, material, material)),
                chest);
    }

    //
    // Netherite Chest
    //

    @GameTest
    public void smithNetheriteChest(TestContext context) {
        ItemStack template = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        ItemStack baseChest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")));
        ItemStack material = new ItemStack(Items.NETHERITE_INGOT);
        ItemStack chest = new ItemStack(
                ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")));
        test(context,
                RecipeType.SMITHING,
                new SmithingRecipeInput(template, baseChest, material),
                chest);
    }
}
