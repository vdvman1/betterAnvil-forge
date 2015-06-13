package vdvman1.betterAnvil.client.gui.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import vdvman1.betterAnvil.client.gui.config.elements.CategoryBA;
import vdvman1.betterAnvil.client.gui.config.elements.ConfigElementBA;
import vdvman1.betterAnvil.common.Config;
import vdvman1.betterAnvil.common.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 6/2/2015 at 11:56 AM.
 * @author Master801
 */
public final class CategoryEnchantmentLimits extends CategoryBA {

    public CategoryEnchantmentLimits() {
        super("Enchantment Limits", true, false);
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        ConfigCategory enchantmentLimitsCategory = Config.getConfiguration().getCategory(Config.CATEGORY_ENCHANTMENT_LIMITS);
        for(final String propertyName : enchantmentLimitsCategory.keySet()) {
            final Property property = enchantmentLimitsCategory.get(propertyName);
            if (property == null) {
                continue;
            }
            list.add(new ConfigElementBA<Integer>(property) {

                @Override
                public void set(Integer object) {
                    for(Enchantment enchantment_0 : Enchantment.enchantmentsList) {
                        if (enchantment_0 != null && Utils.getEnchName(enchantment_0).equals(propertyName)) {
                            List<String> defaultBlackList = new ArrayList<String>();
                            for(Enchantment enchantment_1 : Enchantment.enchantmentsList) {
                                if(enchantment_1 != null && enchantment_1.effectId != enchantment_0.effectId && !enchantment_0.canApplyTogether(enchantment_1)) {
                                    String ench1Name = Utils.getEnchName(enchantment_1);
                                    defaultBlackList.add(ench1Name);
                                }
                            }
                            String[] enchBlackList = Config.getConfiguration().get(Config.CATEGORY_ENCHANTMENT_LIMITS, propertyName, defaultBlackList.toArray(new String[defaultBlackList.size()])).getStringList();
                            Config.ENCHANT_BLACK_LIST.put(enchantment_0.effectId, enchBlackList);
                            break;
                        }
                    }
                    super.set(object);
                }

                @Override
                public Object getDefault() {
                    for(Enchantment enchantment_0 : Enchantment.enchantmentsList) {
                        if (enchantment_0 != null && Utils.getEnchName(enchantment_0).equals(propertyName)) {
                            return enchantment_0.getMaxLevel();
                        }
                    }
                    return property.getDefault();
                }

                @Override
                public boolean isDefault() {
                    for(Enchantment enchantment_0 : Enchantment.enchantmentsList) {
                        if (enchantment_0 != null && Utils.getEnchName(enchantment_0).equals(propertyName)) {
                            return property.getInt() == enchantment_0.getMaxLevel();
                        }
                    }
                    return super.isDefault();
                }

                @Override
                public void setToDefault() {
                    for(Enchantment enchantment_0 : Enchantment.enchantmentsList) {
                        if (enchantment_0 != null && Utils.getEnchName(enchantment_0).equals(propertyName)) {
                            set(enchantment_0.getMaxLevel());
                            break;
                        }
                    }
                }

            }.setRequireGameRestart(true));
        }
        return list;
    }

}
