package vdvman1.betterAnvil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.Tuple4;

import com.google.common.primitives.Ints;


public class Utils {

	public static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);

		// remove final modifier from field
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}

	public static Tuple4<Integer, Double, Map<Integer, Integer>, Map<Integer, Integer>> combine(Map<Integer, Integer> enchList1, Map<Integer, Integer> enchList2, ItemStack item) {
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
					if(origVal == value && origVal < BetterAnvil.enchantLimits.get(id)) {
						compatEnchList.put(id, value + 1);
						repairCost += BetterAnvil.enchantCombineRepairCost;
						repairAmount += BetterAnvil.enchantCombineRepairBonus;
					} else if(origVal < value) {
						compatEnchList.put(id, value);
						repairCost += BetterAnvil.enchantTransferRepairCost;
						repairAmount += BetterAnvil.enchantTransferRepairBonus;
					}
				} else if(item.itemID == Item.enchantedBook.itemID || Enchantment.enchantmentsList[id].canApply(item)) {
					boolean found = false;
					for(Map.Entry<Integer, Integer> entry2: compatEnchList.entrySet()) {
						if(contains(BetterAnvil.enchantBlackList.get(entry2.getKey()), getEnchName(id))) {
							inCompatEnchList.put(id, entry.getValue());
							found = true;
						}
					}
					if(!found) {
						compatEnchList.put(id, entry.getValue());
						repairCost += BetterAnvil.enchantTransferRepairCost;
						repairAmount += BetterAnvil.enchantTransferRepairBonus;
					}
				} else {
					inCompatEnchList.put(id, entry.getValue());
				}
			}
		}
		return new Tuple4<Integer, Double, Map<Integer, Integer>, Map<Integer, Integer>>(repairCost, repairAmount, compatEnchList, inCompatEnchList);
	}

	public static String getEnchName(Enchantment ench) {
		String enchName = ench.getTranslatedName(1);
		return enchName.substring(0, enchName.lastIndexOf(' '));
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

}
