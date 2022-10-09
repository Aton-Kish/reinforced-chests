
package atonkish.reinfchest.client.render.block.entity;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
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
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
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
        this.doubleChestRightBase = modelPart2.getChild(BASE);
        this.doubleChestRightLid = modelPart2.getChild(LID);
        this.doubleChestRightLatch = modelPart2.getChild(LATCH);
        ModelPart modelPart3 = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_RIGHT);
        this.doubleChestLeftBase = modelPart3.getChild(BASE);
        this.doubleChestLeftLid = modelPart3.getChild(LID);
        this.doubleChestLeftLatch = modelPart3.getChild(LATCH);
    }

    public static TexturedModelData getSingleTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(BASE, ModelPartBuilder.create().uv(0, 19).cuboid(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F),
                ModelTransform.NONE);
        modelPartData.addChild(LID, ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F),
                ModelTransform.pivot(0.0F, 9.0F, 1.0F));
        modelPartData.addChild(LATCH, ModelPartBuilder.create().uv(0, 0).cuboid(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F),
                ModelTransform.pivot(0.0F, 8.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getRightDoubleTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(BASE, ModelPartBuilder.create().uv(0, 19).cuboid(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F),
                ModelTransform.NONE);
        modelPartData.addChild(LID, ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F),
                ModelTransform.pivot(0.0F, 9.0F, 1.0F));
        modelPartData.addChild(LATCH, ModelPartBuilder.create().uv(0, 0).cuboid(15.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F),
                ModelTransform.pivot(0.0F, 8.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getLeftDoubleTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(BASE, ModelPartBuilder.create().uv(0, 19).cuboid(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F),
                ModelTransform.NONE);
        modelPartData.addChild(LID, ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F),
                ModelTransform.pivot(0.0F, 9.0F, 1.0F));
        modelPartData.addChild(LATCH, ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F),
                ModelTransform.pivot(0.0F, 8.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay) {
        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState()
                : (BlockState) Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE)
                ? (ChestType) blockState.get(ChestBlock.CHEST_TYPE)
                : ChestType.SINGLE;
        Block block = blockState.getBlock();
        if (block instanceof AbstractChestBlock) {
            ReinforcingMaterial material = ((ReinforcedChestBlockEntity) entity).getMaterial();
            AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock<?>) block;
            boolean bl2 = chestType != ChestType.SINGLE;
            matrices.push();
            float f = ((Direction) blockState.get(ChestBlock.FACING)).asRotation();
            matrices.translate(0.5D, 0.5D, 0.5D);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f));
            matrices.translate(-0.5D, -0.5D, -0.5D);
            DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource2;
            if (bl) {
                propertySource2 = abstractChestBlock.getBlockEntitySource(blockState, world, entity.getPos(), true);
            } else {
                propertySource2 = DoubleBlockProperties.PropertyRetriever::getFallback;
            }

            float g = ((Float2FloatFunction) propertySource2
                    .apply(ChestBlock.getAnimationProgressRetriever((LidOpenable) entity))).get(tickDelta);
            g = 1.0F - g;
            g = 1.0F - g * g * g;
            int i = ((Int2IntFunction) propertySource2.apply(new LightmapCoordinatesRetriever<>())).applyAsInt(light);
            SpriteIdentifier spriteIdentifier = ModTexturedRenderLayers.getReinforcedChestTexture(material, entity,
                    chestType, this.christmas);
            VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers,
                    RenderLayer::getEntityCutout);
            if (bl2) {
                if (chestType == ChestType.LEFT) {
                    this.render(matrices, vertexConsumer, this.doubleChestRightLid, this.doubleChestRightLatch,
                            this.doubleChestRightBase, g, i, overlay);
                } else {
                    this.render(matrices, vertexConsumer, this.doubleChestLeftLid, this.doubleChestLeftLatch,
                            this.doubleChestLeftBase, g, i, overlay);
                }
            } else {
                this.render(matrices, vertexConsumer, this.singleChestLid, this.singleChestLatch, this.singleChestBase,
                        g, i, overlay);
            }

            matrices.pop();
        }
    }

    private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base,
            float openFactor, int light, int overlay) {
        lid.pitch = -(openFactor * 1.5707964F);
        latch.pitch = lid.pitch;
        lid.render(matrices, vertices, light, overlay);
        latch.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
    }
}
