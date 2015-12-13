package vdvman1.betterAnvil.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vdvman1.betterAnvil.BetterAnvil;

public final class Utils {

    public static CombinedEnchantments combine(Map<Integer, Integer> enchList1, Map<Integer, Integer> enchList2, ItemStack item) {
        int repairCost = 0;
        double repairAmount = 0;
        Map<Integer, Integer> compatEnchList = new HashMap<Integer, Integer>();
        Map<Integer, Integer> inCompatEnchList = new HashMap<Integer, Integer>();
        if((enchList1 != null && enchList2 != null) && (!enchList1.isEmpty() || !enchList2.isEmpty())) {
            compatEnchList.putAll(enchList1);
            //Combine all enchantments
            for(Map.Entry<Integer, Integer> entry: enchList2.entrySet()) {
                if (entry == null) continue;
                Enchantment enchantment = null;
                final int index = entry.getKey();
                if (index < Enchantment.enchantmentsList.length) {
                    enchantment = Enchantment.enchantmentsList[index];
                } else {
                    BetterAnvil.BETTER_ANVIL_LOGGER.warn("Enchantment id {}, is invalid!", index);
                }
                if (enchantment == null) continue;
                if(compatEnchList.containsKey(enchantment.effectId)) {
                    int value = enchList2.get(enchantment.effectId);
                    int origVal = compatEnchList.get(enchantment.effectId);
                    int limit = Config.ENCHANT_LIMITS.get(enchantment.effectId);
                    if(origVal == value && origVal < limit) {
                        compatEnchList.put(enchantment.effectId, value + 1);
                        repairCost += Config.enchantCombineRepairCost * value;
                        repairAmount += Config.enchantCombineRepairBonus * value;
                    } else if(origVal < value) {
                        compatEnchList.put(enchantment.effectId, value);
                        repairCost += Config.enchantTransferRepairCost * value;
                        repairAmount += Config.enchantTransferRepairBonus * value;
                    }
                } else if(item.getItem() == Items.enchanted_book || enchantment.canApply(item)) {
                    boolean found = false;
                    for(Map.Entry<Integer, Integer> entry2: compatEnchList.entrySet()) {
                        if (entry2 == null) {
                            continue;
                        }
                        if(areIncompatible(Enchantment.enchantmentsList[entry2.getKey()], enchantment)) {
                            inCompatEnchList.put(enchantment.effectId, entry.getValue());
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        compatEnchList.put(enchantment.effectId, entry.getValue());
                        repairCost += Config.enchantTransferRepairCost * entry.getValue();
                        repairAmount += Config.enchantTransferRepairBonus * entry.getValue();
                    }
                } else {
                    inCompatEnchList.put(enchantment.effectId, entry.getValue());
                }
            }
        }
        return new CombinedEnchantments(repairCost, repairAmount, compatEnchList, inCompatEnchList);
    }

    public static String getEnchName(Enchantment ench) {
        return ench.getName().substring(ench.getName().indexOf('.') + 1);
    }

    public static String getEnchName(int id) {
        return Utils.getEnchName(Enchantment.enchantmentsList[id]);
    }

    public static boolean contains(String[] array, String target) {
        if(array != null && array.length > 0) {
            for (String value : array) {
                if (value != null && value.equals(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean areIncompatible(Enchantment ench1, Enchantment ench2) {
        return Utils.contains(Config.ENCHANT_BLACK_LIST.get(ench1.effectId), Utils.getEnchName(ench2)) || Utils.contains(Config.ENCHANT_BLACK_LIST.get(ench2.effectId), Utils.getEnchName(ench1));
    }

}
