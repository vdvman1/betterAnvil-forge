package vdvman1.betterAnvil;

import java.util.HashMap;
import java.util.Map;

public final class Config {

	public static double breakChance;
	public static double costMultiplier;
	public static int renamingCost;
	public static double itemRepairAmount;
	public static int enchantCombineRepairCost;
	public static int enchantTransferRepairCost;
	public static int enchantCombineRepairBonus;
	public static int enchantTransferRepairBonus;
	public static int copyEnchantToBookCostMultiplier;
	public static int copyEnchantToBookRepairBonus;
	public static int renamingRepairBonus;
	public static int mainRepairBonusPercent;
	public static int repairCostPerItem;
	public static boolean isLegacyMode;
	public static final Map<Integer,Integer> ENCHANT_LIMITS = new HashMap<Integer, Integer>();
	public static final Map<Integer,String[]> ENCHANT_BLACK_LIST = new HashMap<Integer, String[]>();

}
