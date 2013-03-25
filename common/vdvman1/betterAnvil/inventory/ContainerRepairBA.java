package vdvman1.betterAnvil.inventory;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vdvman1.betterAnvil.BetterAnvil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerRepairBA extends ContainerRepair
{
    /** Here comes out item you merged and/or renamed. */
    private IInventory outputSlot = new InventoryCraftResult();

    /**
     * The 2slots where you put your items in that you want to merge and/or rename.
     */
    private IInventory inputSlots = new InventoryRepairBA(this, "Repair", true, 2);
    private World theWorld;
    private int field_82861_i;
    private int field_82858_j;
    private int field_82859_k;

    /** determined by damage of input item and stackSize of repair materials */
    private int stackSizeToBeUsedInRepair = 0;
    private String repairedItemName;

    /** The player that has this container open. */
    private final EntityPlayer thePlayer;

    public ContainerRepairBA(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
    {
        super(par1InventoryPlayer, par2World, par3, par4, par5, par6EntityPlayer);
    	this.theWorld = par2World;
        this.field_82861_i = par3;
        this.field_82858_j = par4;
        this.field_82859_k = par5;
        this.thePlayer = par6EntityPlayer;
        
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new SlotRepairBA(this, this.outputSlot, 2, 134, 47, par2World, par3, par4, par5));
        int l;

        for (l = 0; l < 3; ++l)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, l, 8 + l * 18, 142));
        }
    }
    
    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        super.onCraftMatrixChanged(par1IInventory);

        if (par1IInventory == this.inputSlots)
        {
            this.updateRepairOutput();
        }
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public void updateRepairOutput()
    {
        ItemStack itemstack = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 0;
        int i = 0;
        byte b0 = 0;
        int j = 0;

        if (itemstack == null)
        {
            this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
            this.maximumCost = 0;
        }
        else
        {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
            Map map = EnchantmentHelper.getEnchantments(itemstack1);
            boolean flag = false;
            int k = b0 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
            this.stackSizeToBeUsedInRepair = 0;
            int l;
            int i1;
            int j1;
            int k1;
            int l1;
            Iterator iterator;
            Enchantment enchantment;

            if (itemstack2 != null)
            {
                flag = itemstack2.itemID == Item.enchantedBook.itemID && Item.enchantedBook.func_92110_g(itemstack2).tagCount() > 0;

                if (itemstack1.isItemStackDamageable() && Item.itemsList[itemstack1.itemID].getIsRepairable(itemstack, itemstack2))
                {
                    l = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);

                    if (l <= 0)
                    {
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
                        return;
                    }

                    for (i1 = 0; l > 0 && i1 < itemstack2.stackSize; ++i1)
                    {
                        j1 = itemstack1.getItemDamageForDisplay() - l;
                        itemstack1.setItemDamage(j1);
                        i += Math.max(1, l / 100) + map.size();
                        l = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);
                    }

                    this.stackSizeToBeUsedInRepair = i1;
                }
                else
                {
                    if (!flag && (itemstack1.itemID != itemstack2.itemID || !itemstack1.isItemStackDamageable()))
                    {
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag)
                    {
                        l = itemstack.getMaxDamage() - itemstack.getItemDamageForDisplay();
                        i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamageForDisplay();
                        j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int i2 = l + j1;
                        k1 = itemstack1.getMaxDamage() - i2;

                        if (k1 < 0)
                        {
                            k1 = 0;
                        }

                        if (k1 < itemstack1.getItemDamage())
                        {
                            itemstack1.setItemDamage(k1);
                            i += Math.max(1, j1 / 100);
                        }
                    }

                    Map map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    iterator = map1.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        j1 = ((Integer)iterator.next()).intValue();
                        enchantment = Enchantment.enchantmentsList[j1];
                        k1 = map.containsKey(Integer.valueOf(j1)) ? ((Integer)map.get(Integer.valueOf(j1))).intValue() : 0;
                        l1 = ((Integer)map1.get(Integer.valueOf(j1))).intValue();
                        int j2;

                        if (k1 == l1)
                        {
                            ++l1;
                            j2 = l1;
                        }
                        else
                        {
                            j2 = Math.max(l1, k1);
                        }

                        l1 = j2;
                        int k2 = l1 - k1;
                        boolean flag1 = enchantment.func_92089_a(itemstack);

                        if (this.thePlayer.capabilities.isCreativeMode || itemstack.itemID == ItemEnchantedBook.enchantedBook.itemID)
                        {
                            flag1 = true;
                        }

                        Iterator iterator1 = map.keySet().iterator();

                        while (iterator1.hasNext())
                        {
                            int l2 = ((Integer)iterator1.next()).intValue();

                            if (l2 != j1 && !enchantment.canApplyTogether(Enchantment.enchantmentsList[l2]))
                            {
                                flag1 = false;
                                i += k2;
                            }
                        }

                        if (flag1)
                        {
                            if (l1 > enchantment.getMaxLevel())
                            {
                                l1 = enchantment.getMaxLevel();
                            }

                            map.put(Integer.valueOf(j1), Integer.valueOf(l1));
                            int i3 = 0;

                            switch (enchantment.getWeight())
                            {
                                case 1:
                                    i3 = 8;
                                    break;
                                case 2:
                                    i3 = 4;
                                case 3:
                                case 4:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                default:
                                    break;
                                case 5:
                                    i3 = 2;
                                    break;
                                case 10:
                                    i3 = 1;
                            }

                            if (flag)
                            {
                                i3 = Math.max(1, i3 / 2);
                            }

                            i += i3 * k2;
                        }
                    }
                }
            }

            if (this.repairedItemName != null && !this.repairedItemName.equalsIgnoreCase(itemstack.getDisplayName()) && this.repairedItemName.length() > 0)
            {
                j = itemstack.isItemStackDamageable() ? 7 : itemstack.stackSize * 5;
                i += j;

                if (itemstack.hasDisplayName())
                {
                    k += j / 2;
                }

                itemstack1.setItemName(this.repairedItemName);
            }

            l = 0;

            for (iterator = map.keySet().iterator(); iterator.hasNext(); k += l + k1 * l1)
            {
                j1 = ((Integer)iterator.next()).intValue();
                enchantment = Enchantment.enchantmentsList[j1];
                k1 = ((Integer)map.get(Integer.valueOf(j1))).intValue();
                l1 = 0;
                ++l;

                switch (enchantment.getWeight())
                {
                    case 1:
                        l1 = 8;
                        break;
                    case 2:
                        l1 = 4;
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;
                    case 5:
                        l1 = 2;
                        break;
                    case 10:
                        l1 = 1;
                }

                if (flag)
                {
                    l1 = Math.max(1, l1 / 2);
                }
            }

            if (flag)
            {
                k = Math.max(1, k / 2);
            }

            this.maximumCost = (int)Math.round((k + i) * BetterAnvil.costMultiplier);

            if (i <= 0)
            {
                itemstack1 = null;
            }

            if (j == i && j > 0 && this.maximumCost >= 40)
            {
                this.theWorld.getWorldLogAgent().logInfo("Naming an item only, cost too high; giving discount to cap cost to 39 levels");
                this.maximumCost = 39;
            }

            if (itemstack1 != null)
            {
                i1 = itemstack1.getRepairCost();

                if (itemstack2 != null && i1 < itemstack2.getRepairCost())
                {
                    i1 = itemstack2.getRepairCost();
                }

                if (itemstack1.hasDisplayName())
                {
                    i1 -= 9;
                }

                if (i1 < 0)
                {
                    i1 = 0;
                }

                i1 += 2;
                itemstack1.setRepairCost(i1);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }

            this.outputSlot.setInventorySlotContents(0, itemstack1);
            this.detectAndSendChanges();
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.maximumCost = par2;
        }
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);

        if (!this.theWorld.isRemote)
        {
            for (int i = 0; i < this.inputSlots.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.inputSlots.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    par1EntityPlayer.dropPlayerItem(itemstack);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.theWorld.getBlockId(this.field_82861_i, this.field_82858_j, this.field_82859_k) != Block.anvil.blockID ? false : par1EntityPlayer.getDistanceSq((double)this.field_82861_i + 0.5D, (double)this.field_82858_j + 0.5D, (double)this.field_82859_k + 0.5D) <= 64.0D;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 0 && par2 != 1)
            {
                if (par2 >= 3 && par2 < 39 && !this.mergeItemStack(itemstack1, 0, 2, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

    /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    @Override
    public void updateItemName(String par1Str)
    {
        this.repairedItemName = par1Str;

        if (this.getSlot(2).getHasStack())
        {
            this.getSlot(2).getStack().setItemName(this.repairedItemName);
        }

        this.updateRepairOutput();
    }

    public static IInventory getRepairInputInventory(ContainerRepairBA par0ContainerRepair)
    {
        return par0ContainerRepair.inputSlots;
    }

    public static int getStackSizeUsedInRepair(ContainerRepairBA par0ContainerRepair)
    {
        return par0ContainerRepair.stackSizeToBeUsedInRepair;
    }
}
