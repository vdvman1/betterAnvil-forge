package vdvman1.betterAnvil.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import vdvman1.betterAnvil.BetterAnvil;

import java.util.*;

public final class Config {

    public static final Map<Integer, Integer> ENCHANT_LIMITS = new HashMap<Integer, Integer>();
    public static final Map<Integer, String[]> ENCHANT_BLACK_LIST = new HashMap<Integer, String[]>();
    public static final String CATEGORY_ADJUSTMENTS = "adjustments", CATEGORY_ENCHANTMENT_LIMITS = "enchantment limits", CATEGORY_ENCHANTMENT_INCOMPATIBILITIES = "enchantment_incompatibilities";

    public static double breakChance, costMultiplier;
    public static int renamingCost;
    public static double itemRepairAmount;
    public static int enchantCombineRepairCost, enchantTransferRepairCost, enchantCombineRepairBonus, enchantTransferRepairBonus;
    public static int copyEnchantToBookCostMultiplier, copyEnchantToBookRepairBonus;
    public static int renamingRepairBonus, mainRepairBonusPercent, repairCostPerItem;
    public static boolean isLegacyMode, enableEnchantDuplication, enableItemDestruction;

    private static Configuration configuration = null;

    public static void setConfiguration(Configuration configuration) {
        Config.configuration = configuration;
    }

    public static Configuration getConfiguration() {
        return Config.configuration;
    }

    public static void syncConfiguration(boolean load) {
        if (Config.configuration == null) return;
        if (load && Config.configuration.getConfigFile().exists()) Config.configuration.load();
        Property prop;

        //Require a world restart for these categories.
        Config.configuration.setCategoryRequiresWorldRestart(Configuration.CATEGORY_GENERAL, true);
        Config.configuration.setCategoryRequiresWorldRestart(Config.CATEGORY_ADJUSTMENTS, true);
        Config.configuration.setCategoryRequiresWorldRestart(Config.CATEGORY_ENCHANTMENT_LIMITS, true);
        Config.configuration.setCategoryRequiresWorldRestart(Config.CATEGORY_ENCHANTMENT_INCOMPATIBILITIES, true);

        Config.isLegacyMode = Config.configuration.get(Configuration.CATEGORY_GENERAL, "legacyMode", false).getBoolean(false);
        Config.breakChance = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "breakChance", 12.0D).setLanguageKey("gui.config.adjustments.breakChance").setMinValue(1.0D).setMaxValue(100.0D).getDouble(12.0D) / 100.0D;
        Config.costMultiplier = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "anvilCostMultiplier", 1.0D).setLanguageKey("gui.config.adjustments.anvilCostMultiplier").setMinValue(1.0D).setMaxValue(100.0D).getDouble(1.0D);
        Config.renamingCost = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "renamingCost", 5).setLanguageKey("gui.config.adjustments.renamingCost").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(5);
        Config.renamingRepairBonus = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "renamingRepairBonus", 1).setLanguageKey("gui.config.adjustments.renamingRepairBonus").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(1);
        Config.mainRepairBonusPercent = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "mainRepairBonusPercent", 12).setLanguageKey("gui.config.adjustments.mainRepairBonusPercent").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(12) / 100;
        Config.repairCostPerItem = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "repairCostPerItem", 3).setLanguageKey("gui.config.adjustments.repairCostPerItem").setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(3);
        Config.enableEnchantDuplication = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "enableEnchantDuplication", true).setLanguageKey("gui.config.adjustments.enableEnchantDuplication").getBoolean(true);
        Config.enableItemDestruction = Config.configuration.get(Config.CATEGORY_ADJUSTMENTS, "enableItemDestruction", false).setLanguageKey("gui.config.adjustments.enableItemDestruction").getBoolean(false);

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

        if(!load) {
        	loadLists(false);
        }

        if (!load && Config.configuration.hasChanged() || !Config.configuration.getConfigFile().exists()) Config.configuration.save();
    }

    public static void loadLists(boolean load) {
        if (Config.getConfiguration() == null) {
            BetterAnvil.BETTER_ANVIL_LOGGER.error("The configuration file was not initialised, please report this as a bug to the mod author(s).");
            return;
        }
        for(Enchantment ench : Enchantment.enchantmentsList) {
            if (ench != null) {
                String enchName = Utils.getEnchName(ench);
                int defaultLimit = ench.getMaxLevel();
                int enchLimit = Config.getConfiguration().get(Config.CATEGORY_ENCHANTMENT_LIMITS, enchName, defaultLimit).setRequiresWorldRestart(false).setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(5);
                Config.ENCHANT_LIMITS.put(ench.effectId, enchLimit);
                List<String> defaultBlackList = new ArrayList<String>();
                for(Enchantment ench1 : Enchantment.enchantmentsList) {
                    if (ench1 != null && ench1.effectId != ench.effectId && !ench.canApplyTogether(ench1)) {
                        String ench1Name = Utils.getEnchName(ench1);
                        defaultBlackList.add(ench1Name);
                    }
                }
                String[] enchBlackList = defaultBlackList.toArray(new String[defaultBlackList.size()]);//FIXME
                enchBlackList = Config.getConfiguration().get(Config.CATEGORY_ENCHANTMENT_INCOMPATIBILITIES, enchName, defaultBlackList.toArray(new String[defaultBlackList.size()])).getStringList();
                if (enchBlackList == null || enchBlackList.length <= 0) continue;//Check for invalid enchantment list.
                Config.ENCHANT_BLACK_LIST.put(ench.effectId, enchBlackList);
            }
        }

        if (load) {
            configuration.save();
        }
    }

}
