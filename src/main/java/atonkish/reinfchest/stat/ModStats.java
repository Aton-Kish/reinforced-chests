package atonkish.reinfchest.stat;

import java.util.LinkedHashMap;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.ReinforcedChestsMod;

public class ModStats {
    public static final LinkedHashMap<ReinforcingMaterial, Identifier> OPEN_REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialOpen(ReinforcingMaterial material) {
        String id = "open_" + material.getName() + "_chest";
        Identifier identifier = register(id, StatFormatter.DEFAULT);

        OPEN_REINFORCED_CHEST_MAP.put(material, identifier);

        return identifier;
    }

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(ReinforcedChestsMod.MOD_ID, id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
