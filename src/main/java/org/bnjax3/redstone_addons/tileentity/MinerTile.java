package org.bnjax3.redstone_addons.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class MinerTile extends TileEntity {


    private final ItemStackHandler itemStackHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemStackHandler);

    public MinerTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public MinerTile() {
        this(ModTileEntities.MINER_TILE_ENTITY.get());
    }

    private ItemStackHandler createHandler(){
        return new ItemStackHandler(1)
        {
            @Override
            public int getSlotLimit(int slot)
            {
                return 1;
            }

            @Override
            public int getSlots() {
                return 1;
            }
        };
    }

    @ParametersAreNonnullByDefault
    @Override
    // READ
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        itemStackHandler.deserializeNBT(compoundNBT.getCompound("miner_inv"));
        super.load(blockState, compoundNBT);
    }
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    // WRITE
    public CompoundNBT save(CompoundNBT compoundNBT) {
        super.save(compoundNBT);
        ItemStackHelper.saveAllItems(compoundNBT, NonNullList.withSize(1, itemStackHandler.getStackInSlot(0)));
        return compoundNBT;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public void damageTool() {
        // only use toolInstance for read
        ItemStack toolInstance = itemStackHandler.getStackInSlot(0);
        int damage = toolInstance.getDamageValue();
        if (toolInstance.isDamageableItem() && damage > 0) {
            itemStackHandler.getStackInSlot(0).setDamageValue(damage + 1);
        } else if (damage == 0) {
            itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    public ItemStack getTool(){
        return itemStackHandler.getStackInSlot(0);
    }
    public void setTool(ItemStack itemStack){
        itemStackHandler.setStackInSlot(0, itemStack);
    }
}
