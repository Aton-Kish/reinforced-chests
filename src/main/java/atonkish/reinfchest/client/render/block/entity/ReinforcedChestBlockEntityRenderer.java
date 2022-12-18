
package atonkish.reinfchest.client.render.block.entity;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import java.util.Calendar;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LidOpenable;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
import atonkish.reinfchest.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedChestBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private static final String BASE = "bottom";
    private static final String LID = "lid";
    private static final String LATCH = "lock";
    private final ModelPart singleChestLid;
    private final ModelPart singleChestBase;
    private final ModelPart singleChestLatch;
    private final ModelPart doubleChestRightLid;
    private final ModelPart doubleChestRightBase;
    private final ModelPart doubleChestRightLatch;
    private final ModelPart doubleChestLeftLid;
    private final ModelPart doubleChestLeftBase;
    private final ModelPart doubleChestLeftLatch;
    private boolean christmas;

    public ReinforcedChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
            this.christmas = true;
        }

        ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.CHEST);
        this.singleChestBase = modelPart.getChild(BASE);
        this.singleChestLid = modelPart.getChild(LID);
        this.singleChestLatch = modelPart.getChild(LATCH);
        ModelPart modelPart2 = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_LEFT);
        this.doubleChestLeftBase = modelPart2.getChild(BASE);
        this.doubleChestLeftLid = modelPart2.getChild(LID);
        this.doubleChestLeftLatch = modelPart2.getChild(LATCH);
        ModelPart modelPart3 = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_RIGHT);
        this.doubleChestRightBase = modelPart3.getChild(BASE);
        this.doubleChestRightLid = modelPart3.getChild(LID);
        this.doubleChestRightLatch = modelPart3.getChild(LATCH);
    }

    public static TexturedModelData getSingleTexturedModelData() {
        return ChestBlockEntityRenderer.getSingleTexturedModelData();
    }

    public static TexturedModelData getRightDoubleTexturedModelData() {
        return ChestBlockEntityRenderer.getRightDoubleTexturedModelData();
    }

    public static TexturedModelData getLeftDoubleTexturedModelData() {
        return ChestBlockEntityRenderer.getLeftDoubleTexturedModelData();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay) {
        World world = ((BlockEntity) entity).getWorld();
        boolean bl = world != null;
        BlockState blockState = bl
                ? ((BlockEntity) entity).getCachedState()
                : (BlockState) Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE)
                ? blockState.get(ChestBlock.CHEST_TYPE)
                : ChestType.SINGLE;
        Block block = blockState.getBlock();
        if (!(block instanceof AbstractChestBlock)) {
            return;
        }
        ReinforcingMaterial material = ((ReinforcedChestBlockEntity) entity).getMaterial();
        AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock<?>) block;
        boolean bl2 = chestType != ChestType.SINGLE;
        matrices.push();
        float f = blockState.get(ChestBlock.FACING).asRotation();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrices.translate(-0.5f, -0.5f, -0.5f);
        DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource = bl
                ? abstractChestBlock.getBlockEntitySource(blockState, world, ((BlockEntity) entity).getPos(), true)
                : DoubleBlockProperties.PropertyRetriever::getFallback;
        float g = propertySource.apply(ChestBlock.getAnimationProgressRetriever((LidOpenable) entity)).get(tickDelta);
        g = 1.0f - g;
        g = 1.0f - g * g * g;
        int i = ((Int2IntFunction) propertySource.apply(new LightmapCoordinatesRetriever<>())).applyAsInt(light);
        SpriteIdentifier spriteIdentifier = ModTexturedRenderLayers.getReinforcedChestTexture(material, entity,
                chestType, this.christmas);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers,
                RenderLayer::getEntityCutout);
        if (bl2) {
            if (chestType == ChestType.LEFT) {
                this.render(matrices, vertexConsumer, this.doubleChestLeftLid, this.doubleChestLeftLatch,
                        this.doubleChestLeftBase, g, i, overlay);
            } else {
                this.render(matrices, vertexConsumer, this.doubleChestRightLid, this.doubleChestRightLatch,
                        this.doubleChestRightBase, g, i, overlay);
            }
        } else {
            this.render(matrices, vertexConsumer, this.singleChestLid, this.singleChestLatch, this.singleChestBase, g,
                    i, overlay);
        }
        matrices.pop();
    }

    private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base,
            float openFactor, int light, int overlay) {
        latch.pitch = lid.pitch = -(openFactor * 1.5707964f);
        lid.render(matrices, vertices, light, overlay);
        latch.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
    }
}
