package vdvman1.betterAnvil.inventory;

import net.minecraft.inventory.InventoryBasic;

public class InventoryRepairBA extends InventoryBasic
{
    /** Container of this anvil's block. */
    final ContainerRepairBA theContainer;

    InventoryRepairBA(ContainerRepairBA containerRepairBA, String inventoryTitle, int slotCount)
    {
        super(inventoryTitle, slotCount);
        this.theContainer = containerRepairBA;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        this.theContainer.onCraftMatrixChanged(this);
    }
}
