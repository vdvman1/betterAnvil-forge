package vdvman1.betterAnvil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        Map<Integer, Integer> workingEnchList = new HashMap<Integer, Integer>();
        Map<Integer, Integer> inCompatEnchList = new HashMap<Integer, Integer>();
        if(enchList1 != null && enchList2 != null) {
            workingEnchList.putAll(enchList1);
            Iterator<Integer> it1 = enchList2.keySet().iterator();
            while(it1.hasNext()) {
                int id = it1.next();
                putLargestOrIncMax(workingEnchList, id, enchList2.get(id), BetterAnvil.enchantLimits.get(id));
            }
            it1 = workingEnchList.keySet().iterator();
            Map<Integer, Integer> compatEnchList = new HashMap<Integer, Integer>();
            compatEnchList.putAll(workingEnchList);
            while(it1.hasNext()) {
                int id1 = it1.next();
                Iterator<Integer> it2 = compatEnchList.keySet().iterator();
                while(it2.hasNext()) {
                    int id2 = it2.next();
                    if(contains(BetterAnvil.enchantBlackList.get(id1), getEnchName(id2))) {
                        inCompatEnchList.put(id2, workingEnchList.get(id2));
                        compatEnchList.remove(id2);
                    }
                }
            }
            System.out.println("Compatible");
            it1 = compatEnchList.keySet().iterator();
            while(it1.hasNext()) {
                int id = it1.next();
                System.out.println(Enchantment.enchantmentsList[id].getTranslatedName(workingEnchList.get(id)));
            }
            System.out.println("Incompatible");
            it1 = inCompatEnchList.keySet().iterator();
            while(it1.hasNext()) {
                int id = it1.next();
                System.out.println(Enchantment.enchantmentsList[id].getTranslatedName(workingEnchList.get(id)));
            }
        }
        /*
            while(it1.hasNext()) {
                int id1 = it1.next().intValue();
                Enchantment ench1 = Enchantment.enchantmentsList[id1];
                if(ench1.canApply(item) || item.getItem().itemID == Item.enchantedBook.itemID) {
                    Iterator<Integer> it2 = enchList1.keySet().iterator();
                    while(it2.hasNext()) {
                        int id2 = it1.next().intValue();
                        if(id1 == id2) {
                            continue;
                        } else if(contains(BetterAnvil.enchantBlackList.get(id1), getEnchName(id2))) {
                            putLargest(inCompatEnchList, id2, enchList2.get(id2));
                        }
                    }
                    it2 = enchList1.keySet().iterator();
                    while(it2.hasNext()) {
                        int id2 = it1.next().intValue();
                        if(id1 == id2) {
                            int level1 = enchList1.get(id1);
                            int level2 = enchList2.get(id2);
                            if(level1 == level2 && level1 < BetterAnvil.enchantLimits.get(id1)) {
                                putLargest(compatEnchList, id1, level1 + 1);
                                repairCost += 2;
                            } else if(level1 < level2) {
                                putLargest(compatEnchList, id1, level2);
                                repairCost++;
                            } else {
                                putLargest(compatEnchList, id1, level1);
                                repairCost++;
                            }
                        } else if(!inCompatEnchList.containsKey(id2)) {

                        }
                    }
                }
                if(enchList1.containsKey(id)) {
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
                }
            }*/
        return new Tuple3<Integer, Map<Integer, Integer>, Map<Integer, Integer>>(repairCost, workingEnchList, inCompatEnchList);
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

    public static void putLargestOrIncMax(Map<Integer, Integer> map, int key, int value, int max) {
        if(map.containsKey(key)) {
            int origVal = map.get(key);
            if(origVal == value && origVal < max) {
                map.put(key, value + 1);
            } else if(origVal < value) {
                map.put(key, value);
            }
        } else {
            map.put(key, value);
        }
    }

}
