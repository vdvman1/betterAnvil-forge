package vdvman1.betterAnvil.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vdvman1.betterAnvil.common.Config;
import vdvman1.betterAnvil.block.BlockAnvilBA;

public final class SlotRepairBA extends Slot {

    private final World theWorld;

    private final int blockPosX, blockPosY, blockPosZ;

    /** The anvil this slot belongs to. */
    private final ContainerRepairBA anvil;

    public SlotRepairBA(ContainerRepairBA containerRepairBA, IInventory iInventory, int slotIndex, int slotX, int slotY, World world, int blockX, int blockY, int blockZ) {
        super(iInventory, slotIndex, slotX, slotY);
        this.anvil = containerRepairBA;
        this.theWorld = world;
        this.blockPosX = blockX;
        this.blockPosY = blockY;
        this.blockPosZ = blockZ;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    @Override
    public boolean canTakeStack(EntityPlayer entityPlayer) {
        return (entityPlayer.capabilities.isCreativeMode || entityPlayer.experienceLevel >= this.anvil.maximumCost) && (this.anvil.maximumCost > 0 || this.anvil.isRenamingOnly) && this.getHasStack();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        if (!entityPlayer.capabilities.isCreativeMode) {
            entityPlayer.addExperienceLevel(-anvil.maximumCost);//Removes experience levels from the player using the maximum cost from the anvil then making it a negative.
        }
        
        ItemStack slot0Stack = this.anvil.resultInputStack1 == null ? null : this.anvil.resultInputStack1.copy();
        ItemStack slot1Stack = this.anvil.resultInputStack == null ? null : this.anvil.resultInputStack.copy();
        ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(0, slot0Stack);//BUG-FIX: This should always go first.
        ContainerRepairBA.getRepairInputInventory(this.anvil).setInventorySlotContents(1, slot1Stack);
        this.anvil.maximumCost = 0;

        if (!entityPlayer.capabilities.isCreativeMode && !this.theWorld.isRemote && this.theWorld.getBlock(this.blockPosX, this.blockPosY, this.blockPosZ) instanceof BlockAnvilBA && entityPlayer.getRNG().nextFloat() < Config.breakChance) {
            int blockMetadata = this.theWorld.getBlockMetadata(this.blockPosX, this.blockPosY, this.blockPosZ);
            int blockOrientation = blockMetadata & 3;
            int blockDamage = blockMetadata >> 2;
            ++blockDamage;

            if (blockDamage > 2) {
                this.theWorld.setBlockToAir(this.blockPosX, this.blockPosY, this.blockPosZ);
                this.theWorld.playAuxSFX(1020, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
            } else {
                this.theWorld.setBlockMetadataWithNotify(this.blockPosX, this.blockPosY, this.blockPosZ, blockOrientation | blockDamage << 2, 2);
                this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
            }
        } else if (!this.theWorld.isRemote) {
            this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
        }
        this.anvil.detectAndSendChanges();
    }

}
