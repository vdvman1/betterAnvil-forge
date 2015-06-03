package vdvman1.betterAnvil.client.gui.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import vdvman1.betterAnvil.client.gui.config.elements.CategoryBA;
import vdvman1.betterAnvil.client.gui.config.elements.ConfigElementBA;
import vdvman1.betterAnvil.common.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 6/2/2015 at 11:56 AM.
 * @author Master801
 */
public final class CategoryAdjustments extends CategoryBA {

    public CategoryAdjustments() {
        super("Adjustments", false, false);
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new ConfigElementBA<Double>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "breakChance", 12.0D).setLanguageKey("gui.config.adjustments.breakChance").setMinValue(1.0D).setMaxValue(100.0D)).setName("Break chance"));
        list.add(new ConfigElementBA<Double>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "anvilCostMultiplier", 1.0D).setLanguageKey("gui.config.adjustments.anvilCostMultiplier").setMinValue(1.0D).setMaxValue(100.0D)).setName("Anvil cost multiplier"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "renamingCost", 5).setLanguageKey("gui.config.adjustments.renamingCost").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Renaming cost"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "renamingRepairBonus", 1).setLanguageKey("gui.config.adjustments.renamingRepairBonus").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Renaming repair bonus"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "mainRepairBonusPercent", 12).setLanguageKey("gui.config.adjustments.mainRepairBonusPercent").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Main repair bonus percent"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "repairCostPerItem", 3).setLanguageKey("gui.config.adjustments.repairCostPerItem").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Repair cost per item"));

        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "enchantCombineRepairCost", 2).setLanguageKey("gui.config.adjustments.enchantCombineRepairCost").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Enchant - Combine repair cost"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "enchantTransferRepairCost", 1).setLanguageKey("gui.config.adjustments.enchantTransferRepairCost").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Enchant - Transfer repair cost"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "enchantCombineRepairBonus", 2).setLanguageKey("gui.config.adjustments.enchantCombineRepairBonus").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Enchant - Combine repair bonus"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "enchantTransferRepairBonus", 1).setLanguageKey("gui.config.adjustments.enchantTransferRepairBonus").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Enchant - Transfer repair bonus"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "copyEnchantToBookCostMultiplier", 1).setLanguageKey("gui.config.adjustments.copyEnchantToBookCostMultiplier").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Copy enchant to book cost multiplier"));
        list.add(new ConfigElementBA<Integer>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "copyEnchantToBookRepairBonus", 1).setLanguageKey("gui.config.adjustments.copyEnchantToBookRepairBonus").setMinValue(0).setMaxValue(Short.MAX_VALUE)).setName("Copy enchant to book repair multiplier"));
        list.add(new ConfigElementBA<Double>(Config.getConfiguration().get(Config.CATEGORY_ADJUSTMENTS, "itemRepairAmount", 25.0D).setLanguageKey("gui.config.adjustments.itemRepairAmount").setMinValue(1.0D).setMaxValue(100.0D)).setName("Item repair amount"));
        return list;
    }

}
