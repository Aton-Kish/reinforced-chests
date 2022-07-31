package atonkish.reinfchest.client.render;

import java.util.LinkedHashMap;
import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
    public static final Map<ReinforcingMaterial, Identifier> REINFORCED_CHEST_ATLAS_TEXTURE_MAP = new LinkedHashMap<>();
    private static final Map<ReinforcingMaterial, RenderLayer> REINFORCED_CHEST_RENDER_LAYER_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_SINGLE_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_LEFT_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, SpriteIdentifier> REINFORCED_CHEST_RIGHT_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialAtlasTexture(String namespace, ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_ATLAS_TEXTURE_MAP.containsKey(material)) {
            Identifier identifier = new Identifier(namespace, "textures/atlas/" + material.getName() + "_chest.png");

            ClientSpriteRegistryCallback.event(identifier).register((atlasTexture, registry) -> {
                String singlePath = "entity/reinforced_chest/" + material.getName() + "/single";
                String leftPath = "entity/reinforced_chest/" + material.getName() + "/left";
                String rightPath = "entity/reinforced_chest/" + material.getName() + "/right";
                registry.register(new Identifier(namespace, singlePath));
                registry.register(new Identifier(namespace, leftPath));
                registry.register(new Identifier(namespace, rightPath));
            });

            REINFORCED_CHEST_ATLAS_TEXTURE_MAP.put(material, identifier);
        }

        return REINFORCED_CHEST_ATLAS_TEXTURE_MAP.get(material);
    }

    public static RenderLayer registerMaterialRenderLayer(ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_RENDER_LAYER_MAP.containsKey(material)) {
            RenderLayer renderLayer = RenderLayer.getEntityCutout(REINFORCED_CHEST_ATLAS_TEXTURE_MAP.get(material));
            REINFORCED_CHEST_RENDER_LAYER_MAP.put(material, renderLayer);
        }

        return REINFORCED_CHEST_RENDER_LAYER_MAP.get(material);
    }

    public static SpriteIdentifier registerMaterialSingleSprite(String namespace, ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_SINGLE_MAP.containsKey(material)) {
            SpriteIdentifier identifier = getReinforcedChestTextureId(namespace, material, "single");
            REINFORCED_CHEST_SINGLE_MAP.put(material, identifier);
        }

        return REINFORCED_CHEST_SINGLE_MAP.get(material);
    }

    public static SpriteIdentifier registerMaterialLeftSprite(String namespace, ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_LEFT_MAP.containsKey(material)) {
            SpriteIdentifier identifier = getReinforcedChestTextureId(namespace, material, "left");
            REINFORCED_CHEST_LEFT_MAP.put(material, identifier);
        }

        return REINFORCED_CHEST_LEFT_MAP.get(material);
    }

    public static SpriteIdentifier registerMaterialRightSprite(String namespace, ReinforcingMaterial material) {
        if (!REINFORCED_CHEST_RIGHT_MAP.containsKey(material)) {
            SpriteIdentifier identifier = getReinforcedChestTextureId(namespace, material, "right");
            REINFORCED_CHEST_RIGHT_MAP.put(material, identifier);
        }

        return REINFORCED_CHEST_RIGHT_MAP.get(material);
    }

    public static RenderLayer getReinforcedChest(ReinforcingMaterial material) {
        return REINFORCED_CHEST_RENDER_LAYER_MAP.get(material);
    }

    private static SpriteIdentifier getReinforcedChestTextureId(String namespace, ReinforcingMaterial material,
            String variant) {
        Identifier atlasTexture = REINFORCED_CHEST_ATLAS_TEXTURE_MAP.get(material);
        Identifier textureId = new Identifier(namespace,
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
