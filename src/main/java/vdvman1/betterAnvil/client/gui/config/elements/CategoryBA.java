package vdvman1.betterAnvil.client.gui.config.elements;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.ConfigCategory;

import java.util.List;

/**
 * Created by Master801 on 6/2/2015 at 11:51 AM.
 * @author Master801
 */
public abstract class CategoryBA extends ConfigElementBA {

    protected abstract List<IConfigElement> getConfigElements();

    public CategoryBA(String categoryName, boolean requiresMcRestart, boolean requiresWorldRestart) {
        this(categoryName, requiresMcRestart, requiresWorldRestart, null);
    }

    public CategoryBA(String categoryName, boolean requiresMcRestart, boolean requiresWorldRestart, String tooltip) {
        super(new ConfigCategory(categoryName).setRequiresMcRestart(requiresMcRestart).setRequiresWorldRestart(requiresWorldRestart).setLanguageKey(tooltip));
    }

    @Override
    public final List<IConfigElement> getChildElements() {
        return getConfigElements();
    }

}
