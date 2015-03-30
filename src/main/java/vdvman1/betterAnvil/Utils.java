package vdvman1.betterAnvil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;


public final class Utils {

	public static CombinedEnchantments combine(Map<Integer, Integer> enchList1, Map<Integer, Integer> enchList2, ItemStack item) {
		int repairCost = 0;
		double repairAmount = 0;
		Map<Integer, Integer> compatEnchList = new HashMap<Integer, Integer>();
		Map<Integer, Integer> inCompatEnchList = new HashMap<Integer, Integer>();
		if(enchList1 != null && enchList2 != null) {
			compatEnchList.putAll(enchList1);
			//Combine all enchantments
			for(Map.Entry<Integer, Integer> entry: enchList2.entrySet()) {
				int id = entry.getKey();
				if(compatEnchList.containsKey(id)) {
					int value = enchList2.get(id);
					int origVal = compatEnchList.get(id);
					int limit = Config.ENCHANT_LIMITS.get(id);
					if(origVal == value && origVal < limit) {
						compatEnchList.put(id, value + 1);
						repairCost += Config.enchantCombineRepairCost * value;
						repairAmount += Config.enchantCombineRepairBonus * value;
					} else if(origVal < value) {
						compatEnchList.put(id, value);
						repairCost += Config.enchantTransferRepairCost * value;
						repairAmount += Config.enchantTransferRepairBonus * value;
					}
				} else if(item.getItem() == Items.enchanted_book || Enchantment.enchantmentsList[id].canApply(item)) {
					boolean found = false;
					for(Map.Entry<Integer, Integer> entry2: compatEnchList.entrySet()) {
						if(contains(Config.ENCHANT_BLACK_LIST.get(entry2.getKey()), getEnchName(id))) {
							inCompatEnchList.put(id, entry.getValue());
							found = true;
							break;
						}
					}
					if(!found) {
						compatEnchList.put(id, entry.getValue());
						repairCost += Config.enchantTransferRepairCost * entry.getValue();
						repairAmount += Config.enchantTransferRepairBonus * entry.getValue();
					}
				} else {
					inCompatEnchList.put(id, entry.getValue());
				}
			}
		}
		return new CombinedEnchantments(repairCost, repairAmount, compatEnchList, inCompatEnchList);
	}
	
	public static String getEnchName(Enchantment ench) {
		return ench.getName().substring(ench.getName().indexOf('.') + 1);
	}

	public static String getEnchName(int id) {
		return getEnchName(Enchantment.enchantmentsList[id]);
	}

	public static boolean contains(String[] array, String target) {
		if(array != null) {
			for (String value : array) {
				if (value != null && value.equals(target)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean canApplyTogether(Enchantment ench1, Enchantment ench2) {
		return contains(Config.ENCHANT_BLACK_LIST.get(ench1.effectId), getEnchName(ench2));
	}

}
