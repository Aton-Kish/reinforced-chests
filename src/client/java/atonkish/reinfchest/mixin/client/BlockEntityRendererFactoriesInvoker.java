package atonkish.reinfchest.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

@Environment(EnvType.CLIENT)
@Mixin(BlockEntityRendererFactories.class)
public interface BlockEntityRendererFactoriesInvoker {
    @Invoker("register")
    public static <T extends BlockEntity> void invokeRegister(
            BlockEntityType<? extends T> type,
            BlockEntityRendererFactory<T> factory) {
        throw new AssertionError();
    };
}
