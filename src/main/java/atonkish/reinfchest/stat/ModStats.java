package atonkish.reinfchest.stat;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;

public class ModStats {
    public static final Map<ReinforcingMaterial, Identifier> OPEN_REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialOpen(String namespace, ReinforcingMaterial material) {
        if (!OPEN_REINFORCED_CHEST_MAP.containsKey(material)) {
            String id = "open_" + material.getName() + "_chest";
            Identifier identifier = ModStats.register(namespace, id, StatFormatter.DEFAULT);
            OPEN_REINFORCED_CHEST_MAP.put(material, identifier);
        }

        return OPEN_REINFORCED_CHEST_MAP.get(material);
    }

    private static Identifier register(String namespace, String id, StatFormatter formatter) {
        Identifier identifier = Identifier.of(namespace, id);
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
