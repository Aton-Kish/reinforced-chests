package atonkish.reinfchest.mixin.datafixer.schema;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.schema.Schema1460;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reinfchest.ReinforcedChestsMod;

@Mixin(Schema1460.class)
public class Schema1460Mixin {
    @Inject(at = @At("TAIL"), method = "registerBlockEntities", cancellable = true)
    private void registerBlockEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
        Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();

        // TODO: materials should be able to be resolved dynamically.
        for (String material : List.of("copper", "iron", "gold", "diamond", "netherite")) {
            schema.register(map, String.format("%s:%s_chest", ReinforcedChestsMod.MOD_ID, material), () -> {
                return DSL.optionalFields("Items", DSL.list(TypeReferences.ITEM_STACK.in(schema)));
            });
        }

        cir.setReturnValue(map);
    }
}
