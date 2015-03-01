package vdvman1.betterAnvil;

import java.util.Map;

public final class CombinedEnchantments {
	
	public final int repairCost;
	public final double repairAmount;
	public final Map<Integer, Integer> compatEnchList, incompatEnchList;
	
	public CombinedEnchantments(int repairCost, double repairAmount, Map<Integer, Integer> compatEnchList, Map<Integer, Integer> incompatEnchList)
    {
		this.repairCost = repairCost;
		this.repairAmount = repairAmount;
		this.compatEnchList = compatEnchList;
		this.incompatEnchList = incompatEnchList;
	}

}
