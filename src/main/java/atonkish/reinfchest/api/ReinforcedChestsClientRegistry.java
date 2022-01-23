package atonkish.reinfchest.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedChestsClientRegistry {
    public static Identifier registerMaterialAtlasTexture(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialAtlasTexture(material);
    }

    public static RenderLayer registerMaterialRenderLayer(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialRenderLayer(material);
    }

    public static SpriteIdentifier registerMaterialSingleSprite(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialSingleSprite(material);
    }

    public static SpriteIdentifier registerMaterialLeftSprite(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialLeftSprite(material);
    }

    public static SpriteIdentifier registerMaterialRightSprite(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialRightSprite(material);
    }
}
