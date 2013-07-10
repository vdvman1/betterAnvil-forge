package vdvman1.betterAnvil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.primitives.Ints;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.Tuple3;


public class Utils {

    public static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);

        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    public static Tuple3<Integer, Map<Integer, Integer>, Map<Integer, Integer> > combine(Map<Integer, Integer> enchList1, Map<Integer, Integer> enchList2, ItemStack item) {
        int repairCost = 0;
        Map<Integer, Integer> compatEnchList = new HashMap<Integer, Integer>();
        Map<Integer, Integer> inCompatEnchList = new HashMap<Integer, Integer>();
        if(enchList1 != null && enchList2 != null) {
            compatEnchList.putAll(enchList1);
            for(Map.Entry<Integer, Integer> entry: compatEnchList.entrySet()) {
                int id = entry.getKey();
                int value = enchList2.get(id);
                if(compatEnchList.containsKey(id)) {
                    int origVal = compatEnchList.get(id);
                    if(origVal == value && origVal < BetterAnvil.enchantLimits.get(id)) {
                        compatEnchList.put(id, value + 1);
                        repairCost += 2;
                    } else if(origVal < value) {
                        compatEnchList.put(id, value);
                        repairCost++;
                    }
                } else if(Enchantment.enchantmentsList[id].canApply(item)) {
                    compatEnchList.put(id, value);
                    repairCost++;
                } else {
                    inCompatEnchList.put(id, value);
                }
            }
            int[] ids = Ints.toArray(compatEnchList.keySet());
            for (int i = 0; i < ids.length; i++) {
                int id1 = ids[i];
                for (int j = i+1; j<ids.length; j++) {
                    int id2 = ids[j];
                    if(contains(BetterAnvil.enchantBlackList.get(id1), getEnchName(id2))) {
                        inCompatEnchList.put(id2, compatEnchList.get(id2));
                        compatEnchList.remove(id2);
                    }
                }
            }
            /*System.out.println("Compatible");
            for(Map.Entry<Integer, Integer> entry: compatEnchList.entrySet()) {
                System.out.println(Enchantment.enchantmentsList[entry.getKey()].getTranslatedName(entry.getValue()));
            }
            System.out.println();
            System.out.println("Incompatible");
            for(Map.Entry<Integer, Integer> entry: inCompatEnchList.entrySet()) {
                if(entry.getKey() != null && entry.getValue() != null)
                    System.out.println(Enchantment.enchantmentsList[entry.getKey()].getTranslatedName(entry.getValue()));
            }
            System.out.println("Repair Cost: " + repairCost);*/
        }
        return new Tuple3<Integer, Map<Integer, Integer>, Map<Integer, Integer>>(repairCost, compatEnchList, inCompatEnchList);
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
