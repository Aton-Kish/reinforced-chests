package atonkish.reinfchest.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.gametest.ReinforcedChestsModGameTest;
import atonkish.reinfchest.gametest.util.VoidScreenHander;
import atonkish.reinfchest.item.ModItems;
import atonkish.reinfcore.util.ReinforcingMaterials;
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

public class RecipeTests {
    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>();

    static {
        // Copper Chest
        RecipeTests.register(
                "Craft Copper Chest",
                RecipeType.CRAFTING,
                RecipeTests.create3x3CraftingInventory(
                        new ItemStack(Items.COPPER_INGOT),
                        new ItemStack(Items.COPPER_INGOT),
                        new ItemStack(Items.COPPER_INGOT),
                        new ItemStack(Items.COPPER_INGOT),
                        new ItemStack(Items.CHEST),
                        new ItemStack(Items.COPPER_INGOT),
                        new ItemStack(Items.COPPER_INGOT),
                        new ItemStack(Items.COPPER_INGOT),
                        new ItemStack(Items.COPPER_INGOT)),
                new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper"))));

        // Iron Chest
        RecipeTests.register(
                "Craft Iron Chest",
                RecipeType.CRAFTING,
                RecipeTests.create3x3CraftingInventory(
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper"))),
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.IRON_INGOT)),
                new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron"))));

        // Gold Chest
        RecipeTests.register(
                "Craft Gold Chest",
                RecipeType.CRAFTING,
                RecipeTests.create3x3CraftingInventory(
                        new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron"))),
                        new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(Items.GOLD_INGOT)),
                new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold"))));

        // Diamond Chest
        RecipeTests.register(
                "Craft Diamond Chest",
                RecipeType.CRAFTING,
                RecipeTests.create3x3CraftingInventory(
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold"))),
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.DIAMOND)),
                new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond"))));

        // Netherite Chest
        RecipeTests.register(
                "Smithing Netherite Chest",
                RecipeType.SMITHING,
                new SimpleInventory(
                        new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond"))),
                        new ItemStack(Items.NETHERITE_INGOT)),
                new ItemStack(ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite"))));
    }

    private static <C extends Inventory, T extends Recipe<C>> void register(
            String name, RecipeType<T> type, C inventory, ItemStack expected) {
        String testName = String.format("%s: %s", RecipeTests.class.getSimpleName(), name);
        TEST_FUNCTIONS.add(new TestFunction(
                ReinforcedChestsModGameTest.BATCH_ID_RECIPE,
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
                        context.assertEquals(actual.getItem(), expected.getItem(), "recipe result item");
                        context.assertEquals(actual.getCount(), expected.getCount(), "recipe result count");
                    } catch (Exception e) {
                        ReinforcedChestsMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                        throw e;
                    }

                    context.complete();
                }));
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
