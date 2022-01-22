package atonkish.reinfchest.client.render;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfchest.ReinforcedChestsMod;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
    public static final HashMap<ReinforcingMaterial, Identifier> REINFORCED_CHEST_ATLAS_TEXTURE_MAP;
    private static final HashMap<ReinforcingMaterial, RenderLayer> REINFORCED_CHEST_RENDER_LAYER_MAP;
    public static final HashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_SINGLE_MAP;
    public static final HashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_LEFT_MAP;
    public static final HashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_RIGHT_MAP;

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

    static {
        REINFORCED_CHEST_ATLAS_TEXTURE_MAP = new HashMap<>();
        REINFORCED_CHEST_RENDER_LAYER_MAP = new HashMap<>();
        REINFORCED_CHEST_SINGLE_MAP = new HashMap<>();
        REINFORCED_CHEST_LEFT_MAP = new HashMap<>();
        REINFORCED_CHEST_RIGHT_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
            // Atlas Texture
            Identifier atlasTexture = new Identifier(ReinforcedChestsMod.MOD_ID,
                    "textures/atlas/" + material.getName() + "_chest.png");
            REINFORCED_CHEST_ATLAS_TEXTURE_MAP.put(material, atlasTexture);

            // Render Layer
            RenderLayer renderLayer = RenderLayer.getEntityCutout(atlasTexture);
            REINFORCED_CHEST_RENDER_LAYER_MAP.put(material, renderLayer);

            // Sprite Identifiers
            SpriteIdentifier single = getReinforcedChestTextureId(material, "single");
            SpriteIdentifier left = getReinforcedChestTextureId(material, "left");
            SpriteIdentifier right = getReinforcedChestTextureId(material, "right");
            REINFORCED_CHEST_SINGLE_MAP.put(material, single);
            REINFORCED_CHEST_LEFT_MAP.put(material, left);
            REINFORCED_CHEST_RIGHT_MAP.put(material, right);
        }
    }
}
