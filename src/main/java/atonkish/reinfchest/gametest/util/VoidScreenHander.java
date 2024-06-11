package atonkish.reinfchest.gametest.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class VoidScreenHander extends ScreenHandler {
    public VoidScreenHander() {
        super(null, 0);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        throw new UnsupportedOperationException("not implemented");
    };

    @Override
    public boolean canUse(PlayerEntity player) {
        throw new UnsupportedOperationException("not implemented");
    };
}
