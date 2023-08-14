
package me.gege.trimmersmp.block.entity;

import me.gege.trimmersmp.recipe.CompressorRecipe;
import me.gege.trimmersmp.screen.CompressorScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static me.gege.trimmersmp.block.custom.CompressorBlock.*;

public class CompressorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress;
    public CompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPRESSOR_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0: return CompressorBlockEntity.this.progress;
                    case 1: return CompressorBlockEntity.this.getMaxTime();
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: CompressorBlockEntity.this.progress = value; break;
                    case 1: CompressorBlockEntity.this.maxProgress = value; break;
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
        return Text.literal("Compressor");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        this.propertyDelegate.set(1, getMaxTime());
        return new CompressorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("compressor.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("compressor.progress");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.inventory, true);
        return nbtCompound;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, CompressorBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        if (entity.getMaxTime() > 0) {
            int progress = (int) (Math.floor((double) entity.progress / entity.getMaxTime() * 100 / PERCENTAGE + 1));
            if (world.getBlockState(blockPos).get(COMPRESSION) != progress) {
                entity.getWorld().playSound(null, blockPos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);
            }

            world.setBlockState(blockPos, blockState.with(COMPRESSION, progress));
        }

        if (entity.getMaxTime() == 0 && world.getBlockState(blockPos).get(COMPRESSION) != 0 && entity.getStack(2).isEmpty()) {
            entity.getWorld().playSound(null, blockPos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);
            world.setBlockState(blockPos, blockState.with(COMPRESSION, 0));
            entity.resetProgress();

        }

        if (hasRecipe(entity)) {
            entity.progress++;
            markDirty(world, blockPos, blockState);

            if (entity.progress >= entity.getMaxTime()) {
                craftItem(entity);
            }

        } else  {
            markDirty(world, blockPos, blockState);
        }
    }

    public Integer getMaxTime() {
        SimpleInventory inventory = new SimpleInventory(this.size());
        for (int i = 0; i < this.size(); i++) {
            inventory.setStack(i, this.getStack(i));
        }

        Optional<CompressorRecipe> match = this.getWorld().getRecipeManager()
                .getFirstMatch(CompressorRecipe.Type.INSTANCE, inventory, this.getWorld());

        if (match.isPresent()) {
            return match.get().getTime();
        }

        return 0;
    }

    private static void craftItem(CompressorBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<CompressorRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(CompressorRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if(hasRecipe(entity)) {
            entity.removeStack(0, 1);
            entity.removeStack(1, 1);

            entity.setStack(2, new ItemStack(recipe.get().getOutput(DynamicRegistryManager.EMPTY).getItem(),
                    entity.getStack(2).getCount() + 1));
            }
        }


    public static boolean hasRecipe(CompressorBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }


        Optional<CompressorRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(CompressorRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory);
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }
}