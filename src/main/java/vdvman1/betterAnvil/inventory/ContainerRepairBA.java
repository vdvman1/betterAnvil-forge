package vdvman1.betterAnvil.inventory;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import vdvman1.betterAnvil.BetterAnvil;
import vdvman1.betterAnvil.block.BlockAnvilBA;
import vdvman1.betterAnvil.common.*;

import java.util.*;
import java.util.Map.Entry;

public final class ContainerRepairBA extends ContainerRepair {

    /** Here comes out item you merged and/or renamed. */
    private IInventory outputSlot = new InventoryCraftResult();

    /**
     * The 2 slots where you put your items in that you want to merge and/or rename.
     */
    private IInventory inputSlots = new InventoryRepairBA(this, "Repair", true, 2);
    private World theWorld;
    private int x;
    private int y;
    private int z;

    /** determined by damage of input item and stackSize of repair materials */
    private int stackSizeToBeUsedInRepair = 0;
    public ItemStack resultInputStack = null;
    public ItemStack resultInputStack1 = null;
    private String repairedItemName;

    /** The player that has this container open. */
    private final EntityPlayer thePlayer;
    
    //Currently renaming only
    public boolean isRenamingOnly = false;
    public boolean hadOutput = false;

    public ContainerRepairBA(InventoryPlayer inventoryPlayer, World world, int x, int y, int z, EntityPlayer entityPlayer) {
        super(inventoryPlayer, world, x, y, z, entityPlayer);
    	this.theWorld = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.thePlayer = entityPlayer;
        
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new SlotRepairBA(this, this.outputSlot, 2, 134, 47, world, x, y, z));
        for (int l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(inventoryPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }
        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(inventoryPlayer, l, 8 + l * 18, 142));
        }
    }
    
    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory iInventory) {
        super.onCraftMatrixChanged(iInventory);
        if (iInventory == inputSlots) {
            if(Config.isLegacyMode) {
            	updateRepairOutputOld();
            } else {
            	updateRepairOutput();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateRepairOutput() {
        isRenamingOnly = false;
        hadOutput = false;
        resultInputStack = null;
        resultInputStack1 = null;
        ItemStack stack1 = inputSlots.getStackInSlot(0);
        ItemStack stack2 = inputSlots.getStackInSlot(1);
        double repairCost = 0;
        double repairAmount = 0;
        if(stack1 == null) {
            outputSlot.setInventorySlotContents(0, null);
            maximumCost = 0;
            return;
        }
        ItemStack workStack = stack1.copy();
        //Combine enchantments
        if(stack2 != null) {
            Map<Integer, Integer> enchantments1 = (Map<Integer, Integer>)EnchantmentHelper.getEnchantments(stack1);
            Map<Integer, Integer> enchantments2 = (Map<Integer, Integer>)EnchantmentHelper.getEnchantments(stack2);
            //Used for checking if the stack can be enchanted
            ItemStack notEnchanted = stack1.copy();
            EnchantmentHelper.setEnchantments(new HashMap<Integer, Integer>(), notEnchanted);
            if(stack1.getItem() == stack2.getItem()) {
                //Enchanted item + same enchanted item = item with incompatible enchantments and item with compatible enchantments
                CombinedEnchantments combined = Utils.combine(enchantments1, enchantments2, stack1);
                if (combined == null) {
                    BetterAnvil.BETTER_ANVIL_LOGGER.warn(String.format("Failed to combine enchants from item stack %s and item stack %s!", stack1.toString(), stack2.toString()));
                    return;
                }
                repairCost = combined.repairCost;
                repairAmount = combined.repairAmount;
                EnchantmentHelper.setEnchantments(combined.compatEnchList, workStack);
                if(combined.incompatEnchList.size() != 0) {
                    this.resultInputStack = stack2.copy();
                    EnchantmentHelper.setEnchantments(combined.incompatEnchList, this.resultInputStack);
                    this.resultInputStack.setItemDamage(this.resultInputStack.getMaxDamage() - combined.incompatEnchList.size());
                    if(this.resultInputStack.getItem() == Items.enchanted_book && combined.incompatEnchList.isEmpty()) {
                        this.resultInputStack = new ItemStack(Items.book);
                    }
                }
            } else if(stack1.getItem() == Items.book && stack2.getItem() == Items.enchanted_book) {
                //Copy an enchanted book
                if(!enchantments2.isEmpty()) {
                    this.resultInputStack = stack2.copy();
                    for(Map.Entry<Integer, Integer> entry: enchantments2.entrySet()) {
                        repairCost += entry.getValue() * Config.copyEnchantToBookCostMultiplier;
                        repairAmount += Config.copyEnchantToBookRepairBonus;
                    }
                    workStack = stack2.copy();
                    if(stack1.stackSize <= 1) {
                        this.resultInputStack1 = null;
                    } else {
                        ItemStack resultInput = stack1.copy();
                        resultInput.stackSize -= 1;
                        this.resultInputStack1 = resultInput;
                    }

                }
            } else if((stack1.getItem()== Items.book | stack1.getItem() == Items.enchanted_book) && stack2.isItemEnchanted()) {
                //add first enchantment from item to book
                Entry<Integer, Integer>[] enchantmentEntrySet = enchantments2.entrySet().toArray(new Entry[enchantments2.entrySet().size()]);
                for(Entry<Integer, Integer> ench : enchantmentEntrySet) {
                    enchantments1.put(ench.getKey(), ench.getValue());
                    repairCost += ench.getValue() * Config.copyEnchantToBookCostMultiplier;
                    repairAmount += Config.copyEnchantToBookRepairBonus;
                    enchantments2.remove(ench.getKey());
                }
                workStack = new ItemStack(Items.enchanted_book);
                EnchantmentHelper.setEnchantments(enchantments1, workStack);
                ItemStack resultInput = stack2.copy();
                EnchantmentHelper.setEnchantments(enchantments2, resultInput);
                this.resultInputStack = resultInput;

                if(stack1.getItem() == Items.book) {
                    resultInput = stack1.copy();
                    resultInput.stackSize -= 1;
                    if(resultInput.stackSize <= 0) {
                        resultInput = null;
                    }
                } else {
                    resultInput = null;
                }

                this.resultInputStack1 = resultInput;
            } else if(notEnchanted.isItemEnchantable() && stack2.getItem() == Items.enchanted_book) {
                CombinedEnchantments combined = Utils.combine(enchantments1, enchantments2, stack1);
                if (combined == null) {
                    BetterAnvil.BETTER_ANVIL_LOGGER.warn("Failed to combine enchants from item stack {} and item stack {}!", notEnchanted.toString(), stack2.toString());
                    return;
                }
                repairCost = combined.repairCost;
                repairAmount = combined.repairAmount;
                EnchantmentHelper.setEnchantments(combined.compatEnchList, workStack);
                if(combined.incompatEnchList.size() != 0) {
                    this.resultInputStack = new ItemStack(Items.enchanted_book);
                    EnchantmentHelper.setEnchantments(combined.incompatEnchList, this.resultInputStack);
                } else {
                    this.resultInputStack = new ItemStack(Items.book);
                }
            }
        } else {
            this.outputSlot.setInventorySlotContents(0, null);
        }
        //Rename
        if (this.repairedItemName != null && this.repairedItemName.length() > 0 && !this.repairedItemName.equals(stack1.getDisplayName())) {
            workStack.setStackDisplayName(this.repairedItemName);
            repairCost += Config.renamingCost;
            this.isRenamingOnly = repairCost == Config.renamingCost;
            if (stack1.getItem().isRepairable()) repairAmount += Config.renamingRepairBonus;
        }
        //Repair
        if(stack2 != null && stack1.getItem() == stack2.getItem() && stack1.getItem().isRepairable()) {
            double amount = stack2.getMaxDamage() - stack2.getItemDamage() + ((double)stack1.getMaxDamage() * Config.mainRepairBonusPercent);
            repairAmount += amount;
            repairCost += amount / 100;
        } else if(stack2 != null && stack1.getItem().getIsRepairable(stack1, stack2)) {
            double orig = stack1.getMaxDamage() - stack1.getItemDamage() - repairAmount;
            double damage = orig;
            int max = workStack.getMaxDamage();
            int amount = 0;
            for(int i = 0; i < stack2.stackSize && damage < max; i++) {
                damage = Math.min(damage + (max * Config.itemRepairAmount), max);
                amount++;
            }
            this.resultInputStack = stack2.copy();
            this.resultInputStack.stackSize = stack2.stackSize - amount;
            if(this.resultInputStack.stackSize == 0) {
                this.resultInputStack = null;
            }
            repairAmount += Math.round(damage) - orig;
            repairCost += amount * Config.repairCostPerItem;
        }
        //Set outputs
        workStack.setItemDamage((int)Math.round(workStack.getItemDamage() - repairAmount));
        this.maximumCost = (int) Math.round(repairCost * Config.costMultiplier);
        if(this.maximumCost > 0 || this.isRenamingOnly) {
            this.outputSlot.setInventorySlotContents(0, workStack);
            this.hadOutput = true;
        }
        if(!this.getSlot(2).canTakeStack(thePlayer)) {
            this.outputSlot.setInventorySlotContents(0, null);
        }
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
	public void updateRepairOutputOld() {
        this.isRenamingOnly = false;
        ItemStack itemstack = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 0;
        int itemDamage = 0;
        byte b0 = 0;
        int repairAmount = 0;

        if (itemstack == null) {
            this.outputSlot.setInventorySlotContents(0, null);
            this.maximumCost = 0;
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
            Map<Integer, Integer> enchantmentMap = (Map<Integer, Integer>)EnchantmentHelper.getEnchantments(itemstack1);
            boolean flag = false;
            int repairCost = b0 + itemstack1.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
            this.stackSizeToBeUsedInRepair = 0;
            int itemDamage1;
            int tempInt;
            int tempInt1;
            int tempInt2;
            int tempInt3;
            Iterator<Integer> iterator;
            Enchantment enchantment;

            if (itemstack2 != null) {
                flag = itemstack2.getItem() == Items.enchanted_book && Items.enchanted_book.func_92110_g(itemstack2).tagCount() > 0;//has enchantments on book

                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack1, itemstack2)) {
                    itemDamage1 = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);

                    if (itemDamage1 <= 0) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }

                    for (tempInt = 0; itemDamage1 > 0 && tempInt < itemstack2.stackSize; ++tempInt) {
                        tempInt1 = itemstack1.getItemDamageForDisplay() - itemDamage1;
                        itemstack1.setItemDamage(tempInt1);
                        itemDamage += Math.max(1, itemDamage1 / 100) + enchantmentMap.size();
                        itemDamage1 = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);
                    }

                    this.stackSizeToBeUsedInRepair = tempInt;
                } else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag) {
                        itemDamage1 = itemstack1.getMaxDamage() - itemstack1.getItemDamageForDisplay();
                        tempInt = itemstack2.getMaxDamage() - itemstack2.getItemDamageForDisplay();
                        tempInt1 = tempInt + itemstack1.getMaxDamage() * 12 / 100;
                        int itemDamage2 = itemDamage1 + tempInt1;
                        tempInt2 = itemstack1.getMaxDamage() - itemDamage2;

                        if (tempInt2 < 0) {
                            tempInt2 = 0;
                        }

                        if (tempInt2 < itemstack1.getItemDamage())
                        {
                            itemstack1.setItemDamage(tempInt2);
                            itemDamage += Math.max(1, tempInt1 / 100);
                        }
                    }

                    Map<Integer, Integer> enchantmentMap1 = (Map<Integer, Integer>)EnchantmentHelper.getEnchantments(itemstack2);
                    iterator = enchantmentMap1.keySet().iterator();

                    while (iterator.hasNext()) {
                        tempInt1 = iterator.next();
                        enchantment = Enchantment.enchantmentsList[tempInt1];
                        tempInt2 = enchantmentMap.containsKey(tempInt1) ? enchantmentMap.get(tempInt1) : 0;
                        tempInt3 = enchantmentMap1.get(tempInt1);
                        int enchantmentValue;

                        if (tempInt2 == tempInt3) {
                            ++tempInt3;
                            enchantmentValue = tempInt3;
                        } else {
                            enchantmentValue = Math.max(tempInt3, tempInt2);
                        }

                        tempInt3 = enchantmentValue;
                        int finalEnchantmentValue = tempInt3 - tempInt2;
                        boolean flag1 = enchantment.canApply(itemstack1);

                        if (this.thePlayer.capabilities.isCreativeMode || itemstack1.getItem() == Items.enchanted_book) {
                            flag1 = true;
                        }

                        for(Integer enchantmentValue1 : enchantmentMap.keySet()) {
                            if (enchantmentValue1 != tempInt1 && !Utils.canApplyTogether(enchantment, Enchantment.enchantmentsList[enchantmentValue1])) {
                                flag1 = false;
                                itemDamage += finalEnchantmentValue;
                            }
                        }

                        if (flag1) {
                            if (tempInt3 > enchantment.getMaxLevel()) {
                                tempInt3 = enchantment.getMaxLevel();
                            }

                            enchantmentMap.put(tempInt1, tempInt3);
                            int enchantmentWeight = 0;

                            switch (enchantment.getWeight()) {
                                case 1:
                                    enchantmentWeight = 8;
                                    break;
                                case 2:
                                    enchantmentWeight = 4;
                                    break;
                                case 5:
                                    enchantmentWeight = 2;
                                    break;
                                case 10:
                                    enchantmentWeight = 1;
                                    break;
                                case 3:
                                case 4:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                default:
                                    break;
                            }
                            if (flag) {
                                enchantmentWeight = Math.max(1, enchantmentWeight / 2);
                            }
                            itemDamage += enchantmentWeight * finalEnchantmentValue;
                        }
                    }
                }
            }
            if (StringUtils.isBlank(this.repairedItemName)) {
                if (itemstack.hasDisplayName()) {
                    repairAmount = itemstack.isItemStackDamageable() ? 7 : itemstack.stackSize * 5;
                    itemDamage += repairAmount;
                    itemstack1.func_135074_t();
                }
            } else if (!repairedItemName.equals(itemstack.getDisplayName())) {
                repairAmount = itemstack.isItemStackDamageable() ? 7 : itemstack.stackSize * 5;
                itemDamage += repairAmount;
                if (itemstack.hasDisplayName()) {
                    repairCost += repairAmount / 2;
                }
                itemstack1.setStackDisplayName(this.repairedItemName);
            }
            itemDamage1 = 0;
            for (iterator = enchantmentMap.keySet().iterator(); iterator.hasNext(); repairCost += itemDamage1 + tempInt2 * tempInt3) {
                tempInt1 = iterator.next();
                enchantment = Enchantment.enchantmentsList[tempInt1];
                tempInt2 = enchantmentMap.get(tempInt1);
                tempInt3 = 0;
                ++itemDamage1;
                switch (enchantment.getWeight()) {
                    case 1:
                        tempInt3 = 8;
                        break;
                    case 2:
                        tempInt3 = 4;
                        break;
                    case 5:
                        tempInt3 = 2;
                        break;
                    case 10:
                        tempInt3 = 1;
                        break;
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;
                }
                if (flag) {
                    tempInt3 = Math.max(1, tempInt3 / 2);
                }
            }
            if (flag) {
                repairCost = Math.max(1, repairCost / 2);
            }
            maximumCost = (int)Math.round((repairCost + itemDamage) * Config.costMultiplier);
            if (itemDamage <= 0) {
                itemstack1 = null;
            }
            if (repairAmount == itemDamage && repairAmount > 0) {
                if(Config.renamingCost == 0) {
                    BetterAnvil.BETTER_ANVIL_LOGGER.info("Naming an item only, free renaming enabled, removing cost");
                    maximumCost = 0;
                    isRenamingOnly = true;
                } else if(maximumCost >= 40) {
                    BetterAnvil.BETTER_ANVIL_LOGGER.info("Naming an item only, cost too high; giving discount to cap cost to 39 levels");
                    maximumCost = 39;
                }
            }
            if (itemstack1 != null) {
                tempInt = itemstack1.getRepairCost();
                if (itemstack2 != null && tempInt < itemstack2.getRepairCost()) {
                    tempInt = itemstack2.getRepairCost();
                }
                if (itemstack1.hasDisplayName()) {
                    tempInt -= 9;
                }
                if (tempInt < 0) {
                    tempInt = 0;
                }
                tempInt += 2;
                itemstack1.setRepairCost(tempInt);
                EnchantmentHelper.setEnchantments(enchantmentMap, itemstack1);
            }
            outputSlot.setInventorySlotContents(0, itemstack1);
            detectAndSendChanges();
        }
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        if (!theWorld.isRemote) {
            for (int i = 0; i < inputSlots.getSizeInventory(); ++i) {
                ItemStack itemstack = inputSlots.getStackInSlotOnClosing(i);
                if (itemstack != null) {
                    entityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return theWorld.getBlock(x, y, z) instanceof BlockAnvilBA && entityPlayer.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D;
    }

    /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    @Override
    public void updateItemName(String name) {
        this.repairedItemName = name;

        if (this.getSlot(2).getHasStack()) {
            this.getSlot(2).getStack().setStackDisplayName(this.repairedItemName);
        }

        this.updateRepairOutput();
    }

    public static IInventory getRepairInputInventory(ContainerRepairBA containerRepairBA) {
        return containerRepairBA.inputSlots;
    }

    public static int getStackSizeUsedInRepair(ContainerRepairBA containerRepairBA) {
        return containerRepairBA.stackSizeToBeUsedInRepair;
    }

}
