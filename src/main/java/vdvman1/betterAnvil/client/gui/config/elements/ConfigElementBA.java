package vdvman1.betterAnvil.client.gui.config.elements;

import com.google.common.base.Strings;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Master801 on 6/1/2015 at 4:45 PM.
 * @author Master801
 */
public class ConfigElementBA<T> extends ConfigElement<T> {

    private List<IConfigElement> configElements = null;
    private boolean requiresWorldRestart = false, requiresGameRestart = false;
    private String name = null;

    public ConfigElementBA(ConfigCategory configCategory) {
        super(configCategory);
    }

    public ConfigElementBA(Property property) {
        super(property);
    }

    @Override
    public List<IConfigElement> getChildElements() {
        return configElements != null && !configElements.isEmpty() ? configElements : super.getChildElements();
    }

    @Override
    public boolean requiresWorldRestart() {
        return requiresWorldRestart;
    }

    @Override
    public boolean requiresMcRestart() {
        return requiresGameRestart;
    }

    @Override
    public String getName() {
        return !Strings.isNullOrEmpty(name) ? name : super.getName();
    }

    public ConfigElement<T> setConfigElements(IConfigElement... configElements) {
        if (configElements != null && configElements.length > 0) {
            this.configElements = Arrays.asList(configElements);
        }
        return this;
    }

    public ConfigElement<T> setRequireWorldRestart(boolean requiresWorldRestart) {
        this.requiresWorldRestart = requiresWorldRestart;
        return this;
    }

    public ConfigElement<T> setRequireGameRestart(boolean requiresGameRestart) {
        this.requiresGameRestart = requiresGameRestart;
        return this;
    }

    public ConfigElement<T> setName(String name) {
        this.name = name;
        return this;
    }

}
