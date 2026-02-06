package atonkish.reinfchest.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeInvoker {
  @Invoker("create")
  public static <T extends BlockEntity> BlockEntityType<T> create(
      String id,
      BlockEntityType.BlockEntityFactory<? extends T> blockEntityFactory,
      Block... blocks) {
    throw new AssertionError();
  }
}
