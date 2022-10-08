package atonkish.reinfchest;

import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atonkish.reinfcore.api.ReinforcedCoreModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.api.ReinforcedChestsModInitializer;
import atonkish.reinfchest.api.ReinforcedChestsRegistry;
import atonkish.reinfchest.util.ReinforcingMaterialSettings;

public class ReinforcedChestsMod implements ReinforcedCoreModInitializer {
	public static final String MOD_ID = "reinfchest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeReinforcedCore() {
		// init Reinforced Core
		initializeReinforcedCore();

		// init Reinforced Chests
		initializeReinforcedChests();

		// entrypoint: "reinfchest"
		FabricLoader.getInstance()
				.getEntrypoints(MOD_ID, ReinforcedChestsModInitializer.class)
				.forEach(ReinforcedChestsModInitializer::onInitializeReinforcedChests);
	}

	private static void initializeReinforcedCore() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Reinforced Storage Screen Model
			ReinforcedCoreRegistry.registerMaterialSingleBlockScreenModel(material);
			ReinforcedCoreRegistry.registerMaterialDoubleBlockScreenModel(material);

			// Reinforced Storage Screen Handler
			ReinforcedCoreRegistry.registerMaterialSingleBlockScreenHandler(material);
			ReinforcedCoreRegistry.registerMaterialDoubleBlockScreenHandler(material);
		}
	}

	private static void initializeReinforcedChests() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Stats
			ReinforcedChestsRegistry.registerMaterialOpenStat(MOD_ID, material);

			// Blocks
			ReinforcedChestsRegistry.registerMaterialBlock(MOD_ID, material, materialSettings.getBlockSettings());
			ReinforcedChestsRegistry.registerMaterialBlockEntityType(MOD_ID, material);

			// Items
			ReinforcedChestsRegistry.registerMaterialItem(MOD_ID, material, materialSettings.getItemSettings());
		}

		// Item Group Icon
		if (!FabricLoader.getInstance().isModLoaded("reinfshulker")) {
			ReinforcedChestsRegistry.registerMaterialItemGroupIcon(MOD_ID,
					ReinforcingMaterialSettings.NETHERITE.getMaterial());
		}
	}
}
