
package atonkish.reinfchest.client.render.block.entity;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LidOpenable;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.block.entity.model.ChestBlockModel;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.ChestBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import atonkish.reinfcore.util.ReinforcingMaterial;

import atonkish.reinfchest.block.ReinforcedChestBlock;
import atonkish.reinfchest.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedChestBlockEntityRenderer<T extends BlockEntity & LidOpenable>
        implements BlockEntityRenderer<T, ChestBlockEntityRenderState> {
    private final SpriteHolder materials;
    private final ChestBlockModel singleChest;
    private final ChestBlockModel doubleChestLeft;
    private final ChestBlockModel doubleChestRight;
    private final boolean christmas;

    public ReinforcedChestBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.materials = context.spriteHolder();
        this.christmas = ChestBlockEntityRenderer.isAroundChristmas();
        this.singleChest = new ChestBlockModel(context.getLayerModelPart(EntityModelLayers.CHEST));
        this.doubleChestLeft = new ChestBlockModel(context.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_LEFT));
        this.doubleChestRight = new ChestBlockModel(context.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_RIGHT));
    }

    public ChestBlockEntityRenderState createRenderState() {
        return new ChestBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T blockEntity, ChestBlockEntityRenderState chestBlockEntityRenderState, float f,
            Vec3d vec3d, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource = DoubleBlockProperties.PropertyRetriever::getFallback;

        BlockEntityRenderState.updateBlockEntityRenderState(blockEntity, chestBlockEntityRenderState,
                crumblingOverlayCommand);
        boolean bl = blockEntity.getWorld() != null;
        BlockState blockState = bl
                ? blockEntity.getCachedState()
                : (BlockState) Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        chestBlockEntityRenderState.chestType = blockState.contains(ChestBlock.CHEST_TYPE)
                ? (ChestType) blockState.get(ChestBlock.CHEST_TYPE)
                : ChestType.SINGLE;
        chestBlockEntityRenderState.yaw = ((Direction) blockState.get(ChestBlock.FACING))
                .getPositiveHorizontalDegrees();
        chestBlockEntityRenderState.variant = this.getVariant(blockEntity, this.christmas);
        if (bl) {
            Block block = blockState.getBlock();
            if (block instanceof ReinforcedChestBlock) {
                ReinforcedChestBlock chestBlock = (ReinforcedChestBlock) block;
                propertySource = chestBlock.getBlockEntitySource(blockState, blockEntity.getWorld(),
                        blockEntity.getPos(), true);
            }
        }

        chestBlockEntityRenderState.lidAnimationProgress = ((Float2FloatFunction) propertySource
                .apply(ChestBlock.getAnimationProgressRetriever((LidOpenable) blockEntity))).get(f);
        if (chestBlockEntityRenderState.chestType != ChestType.SINGLE) {
            chestBlockEntityRenderState.lightmapCoordinates = ((Int2IntFunction) propertySource
                    .apply(new LightmapCoordinatesRetriever<>()))
                    .applyAsInt(chestBlockEntityRenderState.lightmapCoordinates);
        }

    }

    public void render(ChestBlockEntityRenderState chestBlockEntityRenderState, MatrixStack matrixStack,
            OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-chestBlockEntityRenderState.yaw));
        matrixStack.translate(-0.5F, -0.5F, -0.5F);
        float f = chestBlockEntityRenderState.lidAnimationProgress;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        ReinforcingMaterial material = ((ReinforcedChestBlock) chestBlockEntityRenderState.blockState.getBlock())
                .getMaterial();
        SpriteIdentifier spriteIdentifier = ModTexturedRenderLayers.getReinforcedChestTextureId(material,
                chestBlockEntityRenderState.variant,
                chestBlockEntityRenderState.chestType);
        RenderLayer renderLayer = spriteIdentifier.getRenderLayer(RenderLayer::getEntityCutout);
        Sprite sprite = this.materials.getSprite(spriteIdentifier);
        if (chestBlockEntityRenderState.chestType != ChestType.SINGLE) {
            if (chestBlockEntityRenderState.chestType == ChestType.LEFT) {
                orderedRenderCommandQueue.submitModel(this.doubleChestLeft, f, matrixStack, renderLayer,
                        chestBlockEntityRenderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, -1, sprite, 0,
                        chestBlockEntityRenderState.crumblingOverlay);
            } else {
                orderedRenderCommandQueue.submitModel(this.doubleChestRight, f, matrixStack, renderLayer,
                        chestBlockEntityRenderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, -1, sprite, 0,
                        chestBlockEntityRenderState.crumblingOverlay);
            }
        } else {
            orderedRenderCommandQueue.submitModel(this.singleChest, f, matrixStack, renderLayer,
                    chestBlockEntityRenderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, -1, sprite, 0,
                    chestBlockEntityRenderState.crumblingOverlay);
        }

        matrixStack.pop();
    }

    private ChestBlockEntityRenderState.Variant getVariant(BlockEntity blockEntity, boolean christmas) {
        if (christmas) {
            return ChestBlockEntityRenderState.Variant.CHRISTMAS;
        } else {
            return ChestBlockEntityRenderState.Variant.REGULAR;
        }
    }
}
