package atonkish.reinfchest.gametest.util;

import java.util.Locale;

import net.minecraft.util.Identifier;

public class TestIdentifier {
    public static Identifier of(String namespace, Class<?> testClass, String name) {
        return Identifier.of(namespace,
                camelToSnake(String.format("%s/%s", testClass.getSimpleName(), name).replace(" ", "_")));
    }

    private static String camelToSnake(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);
    }
}
