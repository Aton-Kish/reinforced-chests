package atonkish.reinfchest.stat;

import java.util.HashMap;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.ReinforcedChestsMod;

public class ModStats {
    public static final HashMap<ReinforcingMaterial, Identifier> OPEN_REINFORCED_CHEST_MAP;

    public static void init() {
    }

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(ReinforcedChestsMod.MOD_ID, id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    static {
        OPEN_REINFORCED_CHEST_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterial.values()) {
            Identifier identifier = register("open_" + material.getName() + "_chest", StatFormatter.DEFAULT);
            OPEN_REINFORCED_CHEST_MAP.put(material, identifier);
        }
    }
}
