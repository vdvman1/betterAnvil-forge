package vdvman1.betterAnvil.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBA extends TileEntity implements IInventory {

    private final ItemStack[] inventory = new ItemStack[2];

    /**
     * Container of this anvil's block.
     */
    private ContainerRepairBA theContainer;

    public void setContainer(ContainerRepairBA container) {
        this.theContainer = container;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (theContainer != null) theContainer.onCraftMatrixChanged(this);
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int stackSize) {
        if (inventory[slotIndex] != null) {
            ItemStack itemstack;
            if (inventory[slotIndex].stackSize <= stackSize) {
                itemstack = inventory[slotIndex];
                inventory[slotIndex] = null;
                markDirty();
                return itemstack;
            } else {
                itemstack = inventory[slotIndex].splitStack(stackSize);
                if (inventory[slotIndex].stackSize == 0) inventory[slotIndex] = null;
                markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex) {
        if (inventory[slotIndex] != null) {
            ItemStack itemstack = inventory[slotIndex];
            inventory[slotIndex] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack stack) {
        this.inventory[slotIndex] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "container.betteranvil";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;//Makes the slot a whole byte. Slot = 1000; Slot & 255 = { Slot = 255 }
            if (j >= 0 && j < inventory.length) inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                NBTTagCompound slotNBTTag = new NBTTagCompound();
                slotNBTTag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(slotNBTTag);
                nbttaglist.appendTag(slotNBTTag);
            }
        }
        tag.setTag("Items", nbttaglist);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
        //NOOP
    }

    @Override
    public void closeInventory() {
        //NOOP
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return false;
    }
}
