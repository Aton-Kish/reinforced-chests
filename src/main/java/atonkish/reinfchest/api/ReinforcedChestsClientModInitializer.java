package atonkish.reinfchest.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ReinforcedChestsClientModInitializer {
    void onInitializeReinforcedChestsClient();
}
