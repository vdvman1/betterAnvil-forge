package vdvman1.betterAnvil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;


public class Utils {

	public static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);

		// remove final modifier from field
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}

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
					int limit = Config.enchantLimits.get(id);
					if(origVal == value && origVal < limit) {
						compatEnchList.put(id, value + 1);
						repairCost += Config.enchantCombineRepairCost * value;
						repairAmount += Config.enchantCombineRepairBonus * value;
					} else if(origVal < value) {
						compatEnchList.put(id, value);
						repairCost += Config.enchantTransferRepairCost * value;
						repairAmount += Config.enchantTransferRepairBonus * value;
					}
				} else if(item.itemID == Item.enchantedBook.itemID || Enchantment.enchantmentsList[id].canApply(item)) {
					boolean found = false;
					for(Map.Entry<Integer, Integer> entry2: compatEnchList.entrySet()) {
						if(contains(Config.enchantBlackList.get(entry2.getKey()), getEnchName(id))) {
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
		return StatCollector.translateToLocal(ench.getName());
	}

	public static String getEnchName(int id) {
		return getEnchName(Enchantment.enchantmentsList[id]);
	}

	public static boolean contains(String[] array, String target) {
		for (String value : array) {
			if (value.equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean canApplyTogether(Enchantment ench1, Enchantment ench2) {
		return contains(Config.enchantBlackList.get(ench1.effectId), getEnchName(ench2));
	}

}
