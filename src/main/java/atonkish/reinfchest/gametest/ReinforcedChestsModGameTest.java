package atonkish.reinfchest.gametest;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.test.CustomTestProvider;
import net.minecraft.test.TestFunction;

import atonkish.reinfchest.gametest.testcase.AdvancementTests;
import atonkish.reinfchest.gametest.testcase.InventoryTests;
import atonkish.reinfchest.gametest.testcase.LootTableTests;
import atonkish.reinfchest.gametest.testcase.OpenTests;
import atonkish.reinfchest.gametest.testcase.PiglinTests;
import atonkish.reinfchest.gametest.testcase.RecipeTests;

public class ReinforcedChestsModGameTest {
    @CustomTestProvider
    public Collection<TestFunction> registerTests() {
        Collection<TestFunction> testFunctions = new ArrayList<>();

        if (System.getProperty(this.getClass().getPackageName()) == null) {
            return testFunctions;
        }

        testFunctions.addAll(AdvancementTests.TEST_FUNCTIONS);
        testFunctions.addAll(InventoryTests.TEST_FUNCTIONS);
        testFunctions.addAll(LootTableTests.TEST_FUNCTIONS);
        testFunctions.addAll(OpenTests.TEST_FUNCTIONS);
        testFunctions.addAll(PiglinTests.TEST_FUNCTIONS);
        testFunctions.addAll(RecipeTests.TEST_FUNCTIONS);

        return testFunctions;
    }
}
