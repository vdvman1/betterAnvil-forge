package vdvman1.betterAnvil;

import java.util.HashMap;
import java.util.Map;

public class Config {

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
	public static Map<Integer,Integer> enchantLimits = new HashMap<Integer, Integer>();
	public static Map<Integer,String[]> enchantBlackList = new HashMap<Integer, String[]>();

}
