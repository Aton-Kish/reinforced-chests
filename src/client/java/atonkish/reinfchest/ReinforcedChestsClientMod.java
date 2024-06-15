package atonkish.reinfchest;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import atonkish.reinfchest.api.ReinforcedChestsClientModInitializer;
import atonkish.reinfchest.api.ReinforcedChestsClientRegistry;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.ReinforcedChestBlock;
import atonkish.reinfchest.block.entity.ModBlockEntityType;
import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
import atonkish.reinfchest.client.render.block.entity.ReinforcedChestBlockEntityRenderer;
import atonkish.reinfchest.util.ReinforcingMaterialSettings;
import atonkish.reinfcore.api.ReinforcedCoreClientModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreClientRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

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

			// Item Renderer
			Block block = ModBlocks.REINFORCED_CHEST_MAP.get(material);
			BuiltinItemRendererRegistry.INSTANCE.register(block,
					(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices,
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
