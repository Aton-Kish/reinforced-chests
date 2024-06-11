package atonkish.reinfchest.gametest;

import java.util.Collection;

import atonkish.reinfchest.gametest.testcase.AdvancementTests;
import atonkish.reinfchest.gametest.testcase.InventoryTests;
import atonkish.reinfchest.gametest.testcase.LootTableTests;
import atonkish.reinfchest.gametest.testcase.PiglinTests;
import atonkish.reinfchest.gametest.testcase.RecipeTests;
import atonkish.reinfchest.gametest.testcase.StatTests;
import net.minecraft.test.CustomTestProvider;
import net.minecraft.test.TestFunction;

public class ReinforcedChestsModGameTest {
    public static final String BATCH_ID_ADVANCEMENT = "advancementBatch";
    public static final String BATCH_ID_INVENTORY = "inventoryBatch";
    public static final String BATCH_ID_LOOT_TABLE = "lootTableBatch";
    public static final String BATCH_ID_PIGLIN = "piglinBatch";
    public static final String BATCH_ID_RECIPE = "recipeBatch";
    public static final String BATCH_ID_STAT = "statBatch";

    @CustomTestProvider
    public Collection<TestFunction> registerAdvancementTests() {
        return AdvancementTests.TEST_FUNCTIONS;
    }

    @CustomTestProvider
    public Collection<TestFunction> registerInventoryTests() {
        return InventoryTests.TEST_FUNCTIONS;
    }

    @CustomTestProvider
    public Collection<TestFunction> registerLootTableTests() {
        return LootTableTests.TEST_FUNCTIONS;
    }

    @CustomTestProvider
    public Collection<TestFunction> registerPiglinTests() {
        return PiglinTests.TEST_FUNCTIONS;
    }

    @CustomTestProvider
    public Collection<TestFunction> registerRecipeTests() {
        return RecipeTests.TEST_FUNCTIONS;
    }

    @CustomTestProvider
    public Collection<TestFunction> registerStatTests() {
        return StatTests.TEST_FUNCTIONS;
    }
}
