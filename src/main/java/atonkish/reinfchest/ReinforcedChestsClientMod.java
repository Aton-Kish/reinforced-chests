package atonkish.reinfchest;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
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
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.ReinforcedChestBlock;
import atonkish.reinfchest.block.entity.ModBlockEntityType;
import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
import atonkish.reinfchest.client.render.block.entity.ReinforcedChestBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class ReinforcedChestsClientMod implements ReinforcedCoreClientModInitializer {
	@Override
	public void onInitializeReinforcedCoreClient() {
		// init Reinforced Core
		initializeReinforcedCoreClient();

		// Block Entity Renderer
		registerBlockEntityRenderer();

		// Item Renderer
		registerBuiltinItemRenderer();
	}

	private static void initializeReinforcedCoreClient() {
		for (ReinforcingMaterial material : ReinforcedChestsMod.MATERIALS) {
			// Reinforced Storage Screen
			ReinforcedCoreClientRegistry.registerSingleBlockScreen(material);
			ReinforcedCoreClientRegistry.registerDoubleBlockScreen(material);
		}
	}

	private static void registerBlockEntityRenderer() {
		for (BlockEntityType<ReinforcedChestBlockEntity> blockEntityType : ModBlockEntityType.REINFORCED_CHEST_MAP
				.values()) {
			BlockEntityRendererRegistry.register(blockEntityType, ReinforcedChestBlockEntityRenderer::new);
		}
	}

	private static void registerBuiltinItemRenderer() {
		for (Block block : ModBlocks.REINFORCED_CHEST_MAP.values()) {
			ReinforcingMaterial material = ((ReinforcedChestBlock) block).getMaterial();
			BuiltinItemRendererRegistry.INSTANCE.register(block, (ItemStack stack, ModelTransformation.Mode mode,
					MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) -> {
				BlockEntity blockEntity = new ReinforcedChestBlockEntity(material, BlockPos.ORIGIN,
						block.getDefaultState().with(ReinforcedChestBlock.FACING, Direction.SOUTH));
				MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(blockEntity, matrices,
						vertexConsumers, light, overlay);
			});
		}
	}
}
