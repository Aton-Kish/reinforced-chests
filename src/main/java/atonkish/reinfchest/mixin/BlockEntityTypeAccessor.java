package atonkish.reinfchest.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeAccessor {
    @Accessor
    Set<Block> getBlocks();
}
