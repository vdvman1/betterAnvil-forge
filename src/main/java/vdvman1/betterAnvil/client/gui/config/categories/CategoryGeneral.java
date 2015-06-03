package vdvman1.betterAnvil.client.gui.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.Configuration;
import vdvman1.betterAnvil.client.gui.config.elements.CategoryBA;
import vdvman1.betterAnvil.client.gui.config.elements.ConfigElementBA;
import vdvman1.betterAnvil.common.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 6/2/2015 at 11:55 AM.
 * @author Master801
 */
public final class CategoryGeneral extends CategoryBA {

    public CategoryGeneral() {
        super("General", false, false);
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new ConfigElementBA<Boolean>(Config.getConfiguration().get(Configuration.CATEGORY_GENERAL, "legacyMode", false)).setName("Legacy mode"));
        return list;
    }

}
