package atonkish.reinfchest;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import atonkish.reinfcore.api.ReinforcedCoreClientModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreClientRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.api.ReinforcedChestsClientModInitializer;
import atonkish.reinfchest.api.ReinforcedChestsClientRegistry;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.ReinforcedChestBlock;
import atonkish.reinfchest.block.entity.ModBlockEntityType;
import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
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

		// entrypoint: "reinfchestclient"
		FabricLoader.getInstance()
				.getEntrypoints(ReinforcedChestsMod.MOD_ID + "client", ReinforcedChestsClientModInitializer.class)
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
			ReinforcedChestsClientRegistry.registerMaterialAtlasTexture(ReinforcedChestsMod.MOD_ID, material);
			ReinforcedChestsClientRegistry.registerMaterialRenderLayer(ReinforcedChestsMod.MOD_ID, material);
			ReinforcedChestsClientRegistry.registerMaterialSingleSprite(ReinforcedChestsMod.MOD_ID, material);
			ReinforcedChestsClientRegistry.registerMaterialLeftSprite(ReinforcedChestsMod.MOD_ID, material);
			ReinforcedChestsClientRegistry.registerMaterialRightSprite(ReinforcedChestsMod.MOD_ID, material);

			// Block Entity Renderer
			BlockEntityRendererRegistry
					.register(ModBlockEntityType.REINFORCED_CHEST_MAP.get(material),
							ReinforcedChestBlockEntityRenderer::new);

			// Item Renderer
			Block block = ModBlocks.REINFORCED_CHEST_MAP.get(material);
			BuiltinItemRendererRegistry.INSTANCE.register(block,
					(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices,
							VertexConsumerProvider vertexConsumers, int light, int overlay) -> {
						BlockEntity blockEntity = new ReinforcedChestBlockEntity(material, BlockPos.ORIGIN,
								block.getDefaultState().with(ReinforcedChestBlock.FACING, Direction.SOUTH));
						MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(blockEntity,
								matrices,
								vertexConsumers, light, overlay);
					});
		}
	}
}
