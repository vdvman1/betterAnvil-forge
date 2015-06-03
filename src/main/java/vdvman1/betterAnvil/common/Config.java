package vdvman1.betterAnvil.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.Map;

public final class Config {

    public static double breakChance, costMultiplier;
    public static int renamingCost;
    public static double itemRepairAmount;
    public static int enchantCombineRepairCost, enchantTransferRepairCost, enchantCombineRepairBonus, enchantTransferRepairBonus;
    public static int copyEnchantToBookCostMultiplier, copyEnchantToBookRepairBonus;
    public static int renamingRepairBonus, mainRepairBonusPercent, repairCostPerItem;
    public static boolean isLegacyMode;
    public static final Map<Integer, Integer> ENCHANT_LIMITS = new HashMap<Integer, Integer>();
    public static final Map<Integer, String[]> ENCHANT_BLACK_LIST = new HashMap<Integer, String[]>();

    private static Configuration configuration = null;

    public static final String CATEGORY_ADJUSTMENTS = "adjustments", CATEGORY_ENCHANTMENT_LIMITS = "enchantment limits";

    public static void setConfiguration(Configuration configuration) {
        Config.configuration = configuration;
    }

    public static Configuration getConfiguration() {
        return Config.configuration;
    }

    public static void syncConfiguration(boolean load) {
        if (Config.configuration == null) {
            return;
        }
        if (load) {
            Config.configuration.load();
        }
        Property prop;
        Config.isLegacyMode = Config.configuration.get(Configuration.CATEGORY_GENERAL, "legacyMode", false).getBoolean(false);
        Config.breakChance = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "breakChance", 12.0D).setLanguageKey("gui.config.adjustments.breakChance").setMinValue(1.0D).setMaxValue(100.0D).getDouble(12.0D) / 100.0D;
        Config.costMultiplier = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "anvilCostMultiplier", 1.0D).setLanguageKey("gui.config.adjustments.anvilCostMultiplier").setMinValue(1.0D).setMaxValue(100.0D).getDouble(1.0D);
        Config.renamingCost = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "renamingCost", 5).setLanguageKey("gui.config.adjustments.renamingCost").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(5);
        Config.renamingRepairBonus = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "renamingRepairBonus", 1).setLanguageKey("gui.config.adjustments.renamingRepairBonus").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(1);
        Config.mainRepairBonusPercent = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "mainRepairBonusPercent", 12).setLanguageKey("gui.config.adjustments.mainRepairBonusPercent").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(12) / 100;
        Config.repairCostPerItem = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "repairCostPerItem", 3).setLanguageKey("gui.config.adjustments.repairCostPerItem").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(3);

        prop = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "enchantCombineRepairCost", 2);
        prop.comment = "Cost to increase an enchantment by a level";
        Config.enchantCombineRepairCost = prop.getInt(2);

        prop = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "enchantTransferRepairCost", 1);
        prop.comment = "Cost to transfer an enchantment to a tool";
        Config.enchantTransferRepairCost = prop.getInt(1);

        prop = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "enchantCombineRepairBonus", 2);
        prop.comment = "Repair bonus added when increasing an enchantment by a level";
        Config.enchantCombineRepairBonus = prop.getInt(2);

        prop = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "enchantTransferRepairBonus", 1);
        prop.comment = "Repair bonus added when transfering an enchantment to a tool";
        Config.enchantTransferRepairBonus = prop.getInt(1);

        prop = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "copyEnchantToBookCostMultiplier", 1);
        prop.comment = "Cost multiplier per enchantment copied onto a book\nThis is multiplied by the enchantment level";
        Config.copyEnchantToBookCostMultiplier = prop.getInt(2);

        prop = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "copyEnchantToBookRepairBonus", 1);
        prop.comment = "Repair bonus added when copying an enchantment to a book";
        Config.copyEnchantToBookRepairBonus = prop.getInt(1);

        prop = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "itemRepairAmount", 25.0D);
        prop.comment = "Percentage each item will repair the tool by";
        Config.itemRepairAmount = prop.getDouble(25.0D) / 100.0D;

        if (Config.configuration.hasChanged()) {
            Config.configuration.save();
        }
    }

}
