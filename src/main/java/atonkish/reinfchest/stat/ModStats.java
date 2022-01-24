package atonkish.reinfchest.stat;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;

public class ModStats {
    public static final Map<ReinforcingMaterial, Identifier> OPEN_REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialOpen(String namespace, ReinforcingMaterial material) {
        if (!OPEN_REINFORCED_CHEST_MAP.containsKey(material)) {
            String id = "open_" + material.getName() + "_chest";
            Identifier identifier = register(namespace, id, StatFormatter.DEFAULT);
            OPEN_REINFORCED_CHEST_MAP.put(material, identifier);
        }

        return OPEN_REINFORCED_CHEST_MAP.get(material);
    }

    private static Identifier register(String namespace, String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(namespace, id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
