package vdvman1.betterAnvil.inventory;

import vdvman1.betterAnvil.BetterAnvil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SlotRepairBA extends Slot
{
    private final World theWorld;

    private final int blockPosX;

    private final int blockPosY;

    private final int blockPosZ;

    /** The anvil this slot belongs to. */
    private final ContainerRepairBA anvil;

    public SlotRepairBA(ContainerRepairBA par1ContainerRepair, IInventory par2IInventory, int par3, int par4, int par5, World par6World, int par7, int par8, int par9)
    {
        super(par2IInventory, par3, par4, par5);
        this.anvil = par1ContainerRepair;
        this.theWorld = par6World;
        this.blockPosX = par7;
        this.blockPosY = par8;
        this.blockPosZ = par9;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return (par1EntityPlayer.capabilities.isCreativeMode || par1EntityPlayer.experienceLevel >= this.anvil.maximumCost) && this.anvil.maximumCost > 0 && this.getHasStack();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        if (!par1EntityPlayer.capabilities.isCreativeMode)
        {
            par1EntityPlayer.addExperienceLevel(-this.anvil.maximumCost);
        }

        ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(0, (ItemStack)null);

        if (ContainerRepairBA.getStackSizeUsedInRepair(this.anvil) > 0)
        {
            ItemStack itemstack1 = ContainerRepairBA.getRepairInputInventory(this.anvil).getStackInSlot(1);

            if (itemstack1 != null && itemstack1.stackSize > ContainerRepairBA.getStackSizeUsedInRepair(this.anvil))
            {
                itemstack1.stackSize -= ContainerRepairBA.getStackSizeUsedInRepair(this.anvil);
                ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(1, itemstack1);
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
            int i = this.theWorld.getBlockMetadata(this.blockPosX, this.blockPosY, this.blockPosZ);
            int j = i & 3;
            int k = i >> 2;
            ++k;

            if (k > 2)
            {
                this.theWorld.setBlockToAir(this.blockPosX, this.blockPosY, this.blockPosZ);
                this.theWorld.playAuxSFX(1020, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
            }
            else
            {
                this.theWorld.setBlockMetadataWithNotify(this.blockPosX, this.blockPosY, this.blockPosZ, j | k << 2, 2);
                this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
            }
        }
        else if (!this.theWorld.isRemote)
        {
            this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
        }
    }
}
