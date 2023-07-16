package me.gege.trimmersmp.block.entity;

import me.gege.trimmersmp.recipe.CrystalCompressorRecipe;
import me.gege.trimmersmp.screen.CrystalCompressorScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CrystalCompressorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    String FUEL = "turtle_helmet";
    private int progress = 0;
    private int maxProgress = 72;
    public CrystalCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRYSTAL_COMPRESSOR_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0: return CrystalCompressorBlockEntity.this.progress;
                    case 1: return CrystalCompressorBlockEntity.this.maxProgress;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: CrystalCompressorBlockEntity.this.progress = value; break;
                    case 1: CrystalCompressorBlockEntity.this.maxProgress = value; break;
                }
            }

            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Crystal Compressor");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CrystalCompressorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("crystal_compressor.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("crystal_compressor.progress");
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, CrystalCompressorBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        if (hasRecipe(entity)) {
            entity.progress++;
            markDirty(world, blockPos, blockState);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, blockState);
        }
    }

    private static void craftItem(CrystalCompressorBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<CrystalCompressorRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(CrystalCompressorRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if(hasRecipe(entity)) {
            entity.removeStack(0, 1);
            entity.removeStack(1, 1);

            entity.setStack(2, new ItemStack(recipe.get().getOutput(DynamicRegistryManager.EMPTY).getItem(),
                    entity.getStack(2).getCount() + 1));

            entity.resetProgress();
        }
    }

    public static boolean hasRecipe(CrystalCompressorBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }


        Optional<CrystalCompressorRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(CrystalCompressorRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput(DynamicRegistryManager.EMPTY).getItem());

    }
    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }
}
