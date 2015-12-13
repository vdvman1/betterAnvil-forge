package vdvman1.betterAnvil.client.gui.config;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import vdvman1.betterAnvil.client.gui.config.screen.GuiScreenConfigBA;

import java.util.Set;

/**
 * Used as a config factory in {@link cpw.mods.fml.common.Mod#guiFactory()} for {@link vdvman1.betterAnvil.BetterAnvil}.
 *
 *
 * Created by Master801 on 5/9/2015 at 12:40 PM.
 *
 * @author Master801
 */
public final class ConfigFactoryBA implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {//Not needed
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiScreenConfigBA.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {//Not needed
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {//Not needed
        return null;
    }

}
