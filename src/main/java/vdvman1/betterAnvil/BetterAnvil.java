package vdvman1.betterAnvil;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vdvman1.betterAnvil.block.BlockAnvilBA;
import vdvman1.betterAnvil.common.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = BetterAnvil.MOD_ID, name = BetterAnvil.MOD_NAME, version = BetterAnvil.VERSION, dependencies = "required-after:Forge@[10.13.2.1342,11]", guiFactory = "vdvman1.betterAnvil.client.gui.config.ConfigFactoryBA")
public final class BetterAnvil {

    //Global variables
    public static final String MOD_ID = "BetterAnvil", MOD_NAME = "Better Anvils", VERSION = "@VERSION@";

    public static final Logger BETTER_ANVIL_LOGGER = LogManager.getLogger(BetterAnvil.MOD_NAME);

    //Blocks
    public static final BlockAnvilBA BLOCK_ANVIL = new BlockAnvilBA();

    //Items
    public static final ItemAnvilBlock ITEM_BLOCK_ANVIL = new ItemAnvilBlock(BetterAnvil.BLOCK_ANVIL);

    @Instance(BetterAnvil.MOD_ID)
    public static BetterAnvil instance;

    //Called before initialization, usually used for configuration
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.setConfiguration(new Configuration(new File(Loader.instance().getConfigDir(), "BetterAnvil.cfg")));
        Config.syncConfiguration(true);
        try {
            GameRegistry.addSubstitutionAlias("minecraft:anvil", Type.BLOCK, BetterAnvil.BLOCK_ANVIL);
            GameRegistry.addSubstitutionAlias("minecraft:anvil", Type.ITEM, BetterAnvil.ITEM_BLOCK_ANVIL);
        } catch(ExistingSubstitutionException e) {
            e.printStackTrace();
        }
    }

    //Called during initialization, used for registering everything etc.
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandlerBA.INSTANCE);
        FMLCommonHandler.instance().bus().register(EventHandlerBA.INSTANCE);

        //register gui
        NetworkRegistry.INSTANCE.registerGuiHandler(BetterAnvil.instance, new GuiHandler());
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent event) {
        for(Enchantment ench : Enchantment.enchantmentsList) {
            if(ench != null) {
                String enchName = Utils.getEnchName(ench);
                int defaulLimit = ench.getMaxLevel(), enchLimit = Config.getConfiguration().get(Config.CATEGORY_ENCHANTMENT_LIMITS, enchName, defaulLimit).setRequiresWorldRestart(true).setMinValue(0).setMaxValue(Short.MAX_VALUE).getInt(5);
                Config.ENCHANT_LIMITS.put(ench.effectId, enchLimit);
                List<String> defaultBlackList = new ArrayList<String>();
                for(Enchantment ench1: Enchantment.enchantmentsList) {
                    if(ench1 != null && ench1.effectId != ench.effectId && !ench.canApplyTogether(ench1)) {
                        String ench1Name = Utils.getEnchName(ench1);
                        defaultBlackList.add(ench1Name);
                    }
                }
                String[] enchBlackList = Config.getConfiguration().get(Config.CATEGORY_ENCHANTMENT_LIMITS, enchName, defaultBlackList.toArray(new String[defaultBlackList.size()])).getStringList();
                Config.ENCHANT_BLACK_LIST.put(ench.effectId, enchBlackList);
            }
        }
        Config.getConfiguration().save();
    }

    @EventHandler
    public void missingMappings(FMLMissingMappingsEvent event) {
        for(MissingMapping missingMapping : event.get()) {
            if (missingMapping.name.equals("BetterAnvil:anvilba")) {
                switch(missingMapping.type) {
                    case BLOCK:
                        missingMapping.remap(BetterAnvil.BLOCK_ANVIL);
                        break;
                    case ITEM:
                        missingMapping.remap(BetterAnvil.ITEM_BLOCK_ANVIL);
                        break;
                }
            }
        }
    }

}
