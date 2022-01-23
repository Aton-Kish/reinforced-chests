package atonkish.reinfchest.client.render;

import java.util.LinkedHashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.ReinforcedChestsMod;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
    public static final LinkedHashMap<ReinforcingMaterial, Identifier> REINFORCED_CHEST_ATLAS_TEXTURE_MAP = new LinkedHashMap<>();
    private static final LinkedHashMap<ReinforcingMaterial, RenderLayer> REINFORCED_CHEST_RENDER_LAYER_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_SINGLE_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_LEFT_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_RIGHT_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialAtlasTexture(ReinforcingMaterial material) {
        Identifier identifier = new Identifier(ReinforcedChestsMod.MOD_ID,
                "textures/atlas/" + material.getName() + "_chest.png");

        REINFORCED_CHEST_ATLAS_TEXTURE_MAP.put(material, identifier);

        return identifier;
    }

    public static RenderLayer registerMaterialRenderLayer(ReinforcingMaterial material) {
        RenderLayer renderLayer = RenderLayer.getEntityCutout(REINFORCED_CHEST_ATLAS_TEXTURE_MAP.get(material));

        REINFORCED_CHEST_RENDER_LAYER_MAP.put(material, renderLayer);

        return renderLayer;
    }

    public static SpriteIdentifier registerMaterialSingleSprite(ReinforcingMaterial material) {
        SpriteIdentifier identifier = getReinforcedChestTextureId(material, "single");

        REINFORCED_CHEST_SINGLE_MAP.put(material, identifier);

        return identifier;
    }

    public static SpriteIdentifier registerMaterialLeftSprite(ReinforcingMaterial material) {
        SpriteIdentifier identifier = getReinforcedChestTextureId(material, "left");

        REINFORCED_CHEST_LEFT_MAP.put(material, identifier);

        return identifier;
    }

    public static SpriteIdentifier registerMaterialRightSprite(ReinforcingMaterial material) {
        SpriteIdentifier identifier = getReinforcedChestTextureId(material, "right");

        REINFORCED_CHEST_RIGHT_MAP.put(material, identifier);

        return identifier;
    }

    public static RenderLayer getReinforcedChest(ReinforcingMaterial material) {
        return REINFORCED_CHEST_RENDER_LAYER_MAP.get(material);
    }

    private static SpriteIdentifier getReinforcedChestTextureId(ReinforcingMaterial material, String variant) {
        Identifier atlasTexture = REINFORCED_CHEST_ATLAS_TEXTURE_MAP.get(material);
        Identifier textureId = new Identifier(ReinforcedChestsMod.MOD_ID,
                "entity/reinforced_chest/" + material.getName() + "/" + variant);
        return new SpriteIdentifier(atlasTexture, textureId);
    }

    public static SpriteIdentifier getReinforcedChestTexture(ReinforcingMaterial material, BlockEntity blockEntity,
            ChestType type, boolean christmas) {
        if (christmas) {
            return getReinforcedChestTexture(type, TexturedRenderLayers.CHRISTMAS, TexturedRenderLayers.CHRISTMAS_LEFT,
                    TexturedRenderLayers.CHRISTMAS_RIGHT);
        } else {
            SpriteIdentifier single = REINFORCED_CHEST_SINGLE_MAP.get(material);
            SpriteIdentifier left = REINFORCED_CHEST_LEFT_MAP.get(material);
            SpriteIdentifier right = REINFORCED_CHEST_RIGHT_MAP.get(material);
            return getReinforcedChestTexture(type, single, left, right);
        }
    }

    private static SpriteIdentifier getReinforcedChestTexture(ChestType type, SpriteIdentifier single,
            SpriteIdentifier left, SpriteIdentifier right) {
        switch (type) {
            case LEFT:
                return left;
            case RIGHT:
                return right;
            case SINGLE:
            default:
                return single;
        }
    }
}
