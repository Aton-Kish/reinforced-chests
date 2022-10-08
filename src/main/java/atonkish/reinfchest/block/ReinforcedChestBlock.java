package atonkish.reinfchest.block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import atonkish.reinfcore.screen.ReinforcedStorageScreenHandler;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
import atonkish.reinfchest.stat.ModStats;

public class ReinforcedChestBlock extends ChestBlock {
    private static final Map<ReinforcingMaterial, DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<NamedScreenHandlerFactory>>> NAME_RETRIEVER_MAP = new LinkedHashMap<>();
    private final ReinforcingMaterial material;

    protected ReinforcedChestBlock(ReinforcingMaterial material, AbstractBlock.Settings settings,
            Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(settings, supplier);
        this.material = material;

        registerMaterialNameRetriever(material);
    }

    protected static DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<NamedScreenHandlerFactory>> registerMaterialNameRetriever(
            ReinforcingMaterial material) {
        if (!NAME_RETRIEVER_MAP.containsKey(material)) {
            DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<NamedScreenHandlerFactory>> nameRetriever = new DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<NamedScreenHandlerFactory>>() {
                public Optional<NamedScreenHandlerFactory> getFromBoth(ChestBlockEntity chestBlockEntity,
                        ChestBlockEntity chestBlockEntity2) {
                    final Inventory inventory = new DoubleInventory(chestBlockEntity, chestBlockEntity2);
                    return Optional.of(new NamedScreenHandlerFactory() {
                        @Nullable
                        public ScreenHandler createMenu(int i, PlayerInventory playerInventory,
                                PlayerEntity playerEntity) {
                            if (chestBlockEntity.checkUnlocked(playerEntity)
                                    && chestBlockEntity2.checkUnlocked(playerEntity)) {
                                chestBlockEntity.checkLootInteraction(playerInventory.player);
                                chestBlockEntity2.checkLootInteraction(playerInventory.player);
                                return ReinforcedStorageScreenHandler.createDoubleBlockScreen(material, i,
                                        playerInventory, inventory);
                            } else {
                                return null;
                            }
                        }

                        public Text getDisplayName() {
                            if (chestBlockEntity.hasCustomName()) {
                                return chestBlockEntity.getDisplayName();
                            } else {
                                String namespace = BlockEntityType.getId(chestBlockEntity.getType()).getNamespace();
                                return (Text) (chestBlockEntity2.hasCustomName() ? chestBlockEntity2.getDisplayName()
                                        : new TranslatableText(
                                                "container." + namespace + "." + material.getName() + "ChestDouble"));
                            }
                        }
                    });
                }

                public Optional<NamedScreenHandlerFactory> getFrom(ChestBlockEntity chestBlockEntity) {
                    return Optional.of(chestBlockEntity);
                }

                public Optional<NamedScreenHandlerFactory> getFallback() {
                    return Optional.empty();
                }
            };
            NAME_RETRIEVER_MAP.put(material, nameRetriever);
        }

        return NAME_RETRIEVER_MAP.get(material);
    }

    @Override
    protected Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.getOrCreateStat(ModStats.OPEN_REINFORCED_CHEST_MAP.get(this.material));
    }

    @Override
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return ((Optional<NamedScreenHandlerFactory>) this.getBlockEntitySource(state, world, pos, false)
                .apply(NAME_RETRIEVER_MAP.get(this.material))).orElse((NamedScreenHandlerFactory) null);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReinforcedChestBlockEntity(this.material, pos, state);
    }

    public ReinforcingMaterial getMaterial() {
        return this.material;
    }
}
