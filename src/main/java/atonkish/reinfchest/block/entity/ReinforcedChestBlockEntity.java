package atonkish.reinfchest.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import atonkish.reinfcore.screen.ReinforcedStorageScreenHandler;
import atonkish.reinfcore.util.ReinforcingMaterial;

public class ReinforcedChestBlockEntity extends ChestBlockEntity {
    private final ViewerCountManager stateManager;
    private final ReinforcingMaterial cachedMaterial;

    public ReinforcedChestBlockEntity(ReinforcingMaterial material, BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityType.REINFORCED_CHEST_MAP.get(material), blockPos, blockState);
        this.setHeldStacks(DefaultedList.ofSize(material.getSize(), ItemStack.EMPTY));
        this.stateManager = new ViewerCountManager() {
            @Override
            protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
                ReinforcedChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN);
            }

            @Override
            protected void onContainerClose(World world, BlockPos pos, BlockState state) {
                ReinforcedChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE);
            }

            @Override
            protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount,
                    int newViewerCount) {
                ReinforcedChestBlockEntity.this.onViewerCountUpdate(world, pos, state, oldViewerCount, newViewerCount);
            }

            @Override
            protected boolean isPlayerViewing(PlayerEntity player) {
                if (player.currentScreenHandler instanceof ReinforcedStorageScreenHandler) {
                    Inventory inventory = ((ReinforcedStorageScreenHandler) player.currentScreenHandler).getInventory();
                    return inventory == ReinforcedChestBlockEntity.this || inventory instanceof DoubleInventory
                            && ((DoubleInventory) inventory).isPart(ReinforcedChestBlockEntity.this);
                }
                return false;
            }
        };
        this.cachedMaterial = material;
    }

    @Override
    public int size() {
        return this.cachedMaterial.getSize();
    }

    @Override
    protected Text getContainerName() {
        String namespace = BlockEntityType.getId(this.getType()).getNamespace();
        return Text.translatable(
                "container." + namespace + "." + this.cachedMaterial.getName() + "Chest");
    }

    static void playSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        ChestType chestType = (ChestType) state.get(ChestBlock.CHEST_TYPE);
        if (chestType != ChestType.LEFT) {
            double d = (double) pos.getX() + 0.5D;
            double e = (double) pos.getY() + 0.5D;
            double f = (double) pos.getZ() + 0.5D;
            if (chestType == ChestType.RIGHT) {
                Direction direction = ChestBlock.getFacing(state);
                d += (double) direction.getOffsetX() * 0.5D;
                f += (double) direction.getOffsetZ() * 0.5D;
            }

            world.playSound((PlayerEntity) null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F,
                    world.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return ReinforcedStorageScreenHandler.createSingleBlockScreen(this.cachedMaterial, syncId, playerInventory,
                this);
    }

    @Override
    public void onScheduledTick() {
        if (!this.removed) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }

    }

    public ReinforcingMaterial getMaterial() {
        return this.cachedMaterial;
    }
}
