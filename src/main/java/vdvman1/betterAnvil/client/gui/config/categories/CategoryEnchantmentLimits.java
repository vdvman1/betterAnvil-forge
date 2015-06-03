package vdvman1.betterAnvil.client.gui.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import vdvman1.betterAnvil.client.gui.config.elements.CategoryBA;
import vdvman1.betterAnvil.client.gui.config.elements.ConfigElementBA;
import vdvman1.betterAnvil.common.Config;

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
        for(String propertyName : enchantmentLimitsCategory.keySet()) {
            final Property property = enchantmentLimitsCategory.get(propertyName);
            list.add(new ConfigElementBA<Integer>(property.setRequiresWorldRestart(true)));
        }
        return list;
    }

}
