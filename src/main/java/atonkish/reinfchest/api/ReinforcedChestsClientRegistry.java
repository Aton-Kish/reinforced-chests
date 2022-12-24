package atonkish.reinfchest.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedChestsClientRegistry {
    @Deprecated
    public static Identifier registerMaterialAtlasTexture(String namespace, ReinforcingMaterial material) {
        return TexturedRenderLayers.CHEST_ATLAS_TEXTURE;
    }

    @Deprecated
    public static RenderLayer registerMaterialRenderLayer(String namespace, ReinforcingMaterial material) {
        return TexturedRenderLayers.getChest();
    }

    public static SpriteIdentifier registerMaterialSingleSprite(String namespace, ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialSingleSprite(namespace, material);
    }

    public static SpriteIdentifier registerMaterialLeftSprite(String namespace, ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialLeftSprite(namespace, material);
    }

    public static SpriteIdentifier registerMaterialRightSprite(String namespace, ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialRightSprite(namespace, material);
    }
}
