package vdvman1.betterAnvil.inventory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vdvman1.betterAnvil.BetterAnvil;

public class SlotRepairBA extends Slot
{
    final World theWorld;

    final int blockPosX;

    final int blockPosY;

    final int blockPosZ;

    /** The anvil this slot belongs to. */
    final ContainerRepairBA anvil;

    SlotRepairBA(ContainerRepairBA containerRepairBA, IInventory iInventory, int x, int y, int z, World world, int blockX, int blockY, int blockZ)
    {
        super(iInventory, x, y, z);
        this.anvil = containerRepairBA;
        this.theWorld = world;
        this.blockPosX = blockX;
        this.blockPosY = blockY;
        this.blockPosZ = blockZ;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return (par1EntityPlayer.capabilities.isCreativeMode || par1EntityPlayer.experienceLevel >= this.anvil.maximumCost) && this.anvil.maximumCost > 0 && this.getHasStack();
    }

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        if (!par1EntityPlayer.capabilities.isCreativeMode)
        {
            par1EntityPlayer.addExperienceLevel(-this.anvil.maximumCost);
        }

        ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(0, (ItemStack)null);

        if (ContainerRepairBA.getStackSizeUsedInRepair(this.anvil) > 0)
        {
            ItemStack var3 = ContainerRepairBA.getRepairInputInventory(this.anvil).getStackInSlot(1);

            if (var3 != null && var3.stackSize > ContainerRepairBA.getStackSizeUsedInRepair(this.anvil))
            {
                var3.stackSize -= ContainerRepairBA.getStackSizeUsedInRepair(this.anvil);
                ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(1, var3);
            }
            else
            {
                ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(1, (ItemStack)null);
            }
        }
        else
        {
            ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(1, (ItemStack)null);
        }

        this.anvil.maximumCost = 0;

        if (!par1EntityPlayer.capabilities.isCreativeMode && !this.theWorld.isRemote && this.theWorld.getBlockId(this.blockPosX, this.blockPosY, this.blockPosZ) == Block.anvil.blockID && par1EntityPlayer.getRNG().nextFloat() < BetterAnvil.breakChance)
        {
            int var6 = this.theWorld.getBlockMetadata(this.blockPosX, this.blockPosY, this.blockPosZ);
            int var4 = var6 & 3;
            int var5 = var6 >> 2;
            ++var5;

            if (var5 > 2)
            {
                this.theWorld.setBlockWithNotify(this.blockPosX, this.blockPosY, this.blockPosZ, 0);
                this.theWorld.playAuxSFX(1020, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
            }
            else
            {
                this.theWorld.setBlockMetadataWithNotify(this.blockPosX, this.blockPosY, this.blockPosZ, var4 | var5 << 2);
                this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
            }
        }
        else if (!this.theWorld.isRemote)
        {
            this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
        }
    }
}
