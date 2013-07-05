package vdvman1.betterAnvil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;


public class Utils {
	
	public static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
	    field.setAccessible(true);
	    
	    // remove final modifier from field
	    Field modifiersField = Field.class.getDeclaredField("modifiers");
	    modifiersField.setAccessible(true);
	    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

	    field.set(null, newValue);
	}
	
	public static Tuple combine(Map<Integer, Integer> enchList1, Map<Integer, Integer> enchList2, ItemStack item) {
	    int repairCost = 0;
	    Map<Integer, Integer> enchList = new HashMap<Integer, Integer>();
	    if(enchList1 != null && enchList2 != null) {
	        Iterator<Integer> it1 = enchList2.keySet().iterator();
            while(it1.hasNext()) {
                int id1 = it1.next().intValue();
                Enchantment ench1 = Enchantment.enchantmentsList[id1];
                if(ench1.canApply(item) || item.getItem().itemID == Item.enchantedBook.itemID) {
                    Iterator<Integer> it2 = enchList1.keySet().iterator();
                    while(it2.hasNext()) {
                        int id2 = it1.next().intValue();
                        Enchantment ench2 = Enchantment.enchantmentsList[id2];
                        if(!contains(BetterAnvil.enchantBlackList.get(id1), getEnchName(ench2))) {
                            
                        }
                    }
                }
                /*if(enchList1.containsKey(id)) {
                    if(enchList1.get(id).equals(enchList2.get(id))) {
                        int level = enchList2.get(id).intValue();
                        if(level < BetterAnvil.enchantLimits.get(id)) {
                            level++;
                        }
                        enchList1.put(id, level);
                        repairCost += 2;
                    } else if(enchList1.get(id).intValue() < enchList2.get(id).intValue()) {
                        enchList1.put(id, enchList2.get(id));
                        repairCost++;
                    }
                } else {
                    enchList1.put(id, enchList2.get(id));
                    repairCost++;
                }*/
            }
	    }
	    return new Tuple(repairCost, enchList);
	}
	
	public static String getEnchName(Enchantment ench) {
	    String enchName = ench.getTranslatedName(1);
        return enchName.substring(0, enchName.lastIndexOf(' '));
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
