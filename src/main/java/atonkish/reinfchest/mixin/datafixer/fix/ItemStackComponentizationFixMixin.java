package atonkish.reinfchest.mixin.datafixer.fix;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mojang.serialization.Dynamic;

import net.minecraft.datafixer.fix.ItemStackComponentizationFix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;

@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
  @Inject(at = @At("RETURN"), method = "fixBlockEntityData", cancellable = true)
  private static <T> void fixBlockEntityData(
      ItemStackComponentizationFix.StackData data,
      Dynamic<T> dynamic,
      String blockEntityId,
      CallbackInfoReturnable<Dynamic<T>> cir) {
    Set<String> itemIds = new HashSet<>();
    for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
      itemIds.add(String.format("%s:%s_chest", ReinforcedChestsMod.MOD_ID, material.getName()));
    }

    if (data.itemMatches(itemIds)) {
      List<Dynamic<T>> list =
          dynamic
              .get("Items")
              .asList(
                  itemsDynamic ->
                      itemsDynamic
                          .emptyMap()
                          .set(
                              "slot",
                              itemsDynamic.createInt(
                                  itemsDynamic.get("Slot").asByte((byte) 0) & 255))
                          .set("item", itemsDynamic.remove("Slot")));
      if (!list.isEmpty()) {
        data.setComponent("minecraft:container", dynamic.createList(list.stream()));
      }
      cir.setReturnValue(dynamic.remove("Items"));
    }
  }
}
