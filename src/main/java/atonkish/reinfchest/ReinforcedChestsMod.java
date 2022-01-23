package atonkish.reinfchest;

import net.fabricmc.loader.api.FabricLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atonkish.reinfchest.api.ReinforcedChestsModInitializer;
import atonkish.reinfchest.api.ReinforcedChestsRegistry;
import atonkish.reinfchest.util.ReinforcingMaterialSettings;
import atonkish.reinfcore.api.ReinforcedCoreModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

public class ReinforcedChestsMod implements ReinforcedCoreModInitializer, ReinforcedChestsModInitializer {
	public static final String MOD_ID = "reinfchest";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitializeReinforcedCore() {
		// init Reinforced Core
		initializeReinforcedCore();

		// entrypoint: "reinfchest"
		FabricLoader.getInstance()
				.getEntrypoints(MOD_ID, ReinforcedChestsModInitializer.class)
				.forEach(ReinforcedChestsModInitializer::onInitializeReinforcedChests);
	}

	@Override
	public void onInitializeReinforcedChests() {
		// init Reinforced Chests
		initializeReinforcedChests();
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
			ReinforcedChestsRegistry.registerMaterialOpenStat(material);

			// Blocks
			ReinforcedChestsRegistry.registerMaterialBlock(material, materialSettings.getBlockSettings());
			ReinforcedChestsRegistry.registerMaterialBlockEntityType(material);

			// Items
			ReinforcedChestsRegistry.registerMaterialItem(material, materialSettings.getItemSettings());
		}

		// Item Group Icon
		if (!FabricLoader.getInstance().isModLoaded("reinfshulker")) {
			ReinforcedChestsRegistry.registerMaterialItemGroupIcon(ReinforcingMaterialSettings.NETHERITE.getMaterial());
		}
	}
}
