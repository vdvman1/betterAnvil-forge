package vdvman1.betterAnvil.client.gui.config.screen;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import vdvman1.betterAnvil.BetterAnvil;
import vdvman1.betterAnvil.client.gui.config.categories.*;
import vdvman1.betterAnvil.common.Config;

import java.util.*;

/**
 * Created by Master801 on 5/9/2015 at 12:44 PM.
 * @author Master801
 */
public final class GuiScreenConfigBA extends GuiConfig {

    /**
     * Do not touch this constructor, it is reflectively gotten and invoked by the config factory.
     */
    public GuiScreenConfigBA(GuiScreen parentScreen) {
        super(parentScreen, Arrays.asList((IConfigElement)new CategoryGeneral(), new CategoryAdjustments(), new CategoryEnchantmentLimits()), BetterAnvil.MOD_ID, "BA-CFG", false, false, GuiConfig.getAbridgedConfigPath(Config.getConfiguration().toString()));
    }

}
