package atonkish.reinfchest.mixin.client;

import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Inject(at = @At("HEAD"), method = "addDefaultTextures")
    private static void addDefaultTextures(Consumer<SpriteIdentifier> adder, CallbackInfo info) {
        for (ReinforcingMaterial material : ModBlocks.REINFORCED_CHEST_MAP.keySet()) {
            adder.accept(ModTexturedRenderLayers.REINFORCED_CHEST_SINGLE_MAP.get(material));
            adder.accept(ModTexturedRenderLayers.REINFORCED_CHEST_LEFT_MAP.get(material));
            adder.accept(ModTexturedRenderLayers.REINFORCED_CHEST_RIGHT_MAP.get(material));
        }
    }
}
