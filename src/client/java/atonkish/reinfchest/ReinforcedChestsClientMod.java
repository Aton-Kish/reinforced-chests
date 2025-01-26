package atonkish.reinfchest;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import atonkish.reinfcore.api.ReinforcedCoreClientModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreClientRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

import atonkish.reinfchest.api.ReinforcedChestsClientModInitializer;
import atonkish.reinfchest.api.ReinforcedChestsClientRegistry;
import atonkish.reinfchest.block.entity.ModBlockEntityType;
import atonkish.reinfchest.client.render.block.entity.ReinforcedChestBlockEntityRenderer;
import atonkish.reinfchest.util.ReinforcingMaterialSettings;

@Environment(EnvType.CLIENT)
public class ReinforcedChestsClientMod implements ReinforcedCoreClientModInitializer {
	@Override
	public void onInitializeReinforcedCoreClient() {
		// init Reinforced Core
		initializeReinforcedCoreClient();

		// init Reinforced Chests
		initializeReinforcedChestsClient();

		// entrypoint: "reinfchest-client"
		FabricLoader.getInstance()
				.getEntrypoints(String.format("%s-client", ReinforcedChestsMod.MOD_ID),
						ReinforcedChestsClientModInitializer.class)
				.forEach(ReinforcedChestsClientModInitializer::onInitializeReinforcedChestsClient);
	}

	private static void initializeReinforcedCoreClient() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Reinforced Storage Screen
			ReinforcedCoreClientRegistry.registerMaterialSingleBlockScreen(material);
			ReinforcedCoreClientRegistry.registerMaterialDoubleBlockScreen(material);
		}
	}

	private static void initializeReinforcedChestsClient() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Textured Render Layers
			ReinforcedChestsClientRegistry.registerMaterialSingleSprite(ReinforcedChestsMod.MOD_ID, material);
			ReinforcedChestsClientRegistry.registerMaterialLeftSprite(ReinforcedChestsMod.MOD_ID, material);
			ReinforcedChestsClientRegistry.registerMaterialRightSprite(ReinforcedChestsMod.MOD_ID, material);

			// Block Entity Renderer
			BlockEntityRendererFactories
					.register(ModBlockEntityType.REINFORCED_CHEST_MAP.get(material),
							ReinforcedChestBlockEntityRenderer::new);
		}
	}
}
