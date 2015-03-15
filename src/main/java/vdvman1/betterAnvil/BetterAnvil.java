package vdvman1.betterAnvil;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import vdvman1.betterAnvil.block.BlockAnvilBA;
import vdvman1.betterAnvil.gui.GuiHandler;

import java.util.ArrayList;

@Mod(modid = BetterAnvil.MODID, name = BetterAnvil.MOD_NAME, version = BetterAnvil.VERSION)
public final class BetterAnvil {

    //Global variables
    public static final String MODID = "BetterAnvil";
    public static final String MOD_NAME = "Better Anvils";
    public static final String VERSION = "@VERSION@";

    //Blocks
    public static final BlockAnvilBA ANVIL = new BlockAnvilBA();
    
    static Configuration config;

    //Configuration categories
    public static final String CAT_ADJUSTMENTS = "Adjustments";

    @Instance(BetterAnvil.MODID)
    public static BetterAnvil instance;

    //Called before initialization, usually used for configuration
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        Property prop;
        Config.isLegacyMode = config.get(Configuration.CATEGORY_GENERAL, "legacyMode", false).getBoolean(false);
        Config.breakChance = config.get(BetterAnvil.CAT_ADJUSTMENTS, "breakChance", 12).getDouble(12) / 100;
        Config.costMultiplier = config.get(BetterAnvil.CAT_ADJUSTMENTS, "anvilCostMultiplier", 1).getDouble(1);
        Config.renamingCost = config.get(BetterAnvil.CAT_ADJUSTMENTS, "renamingCost", 5).getInt(5);
        Config.renamingRepairBonus = config.get(BetterAnvil.CAT_ADJUSTMENTS, "renamingRepairBonus", 1).getInt(1);
        Config.mainRepairBonusPercent = config.get(BetterAnvil.CAT_ADJUSTMENTS, "mainRepairBonusPercent", 12).getInt(12) / 100;
        Config.repairCostPerItem = config.get(BetterAnvil.CAT_ADJUSTMENTS, "repairCostPerItem", 3).getInt(3);
        
        prop = config.get(BetterAnvil.CAT_ADJUSTMENTS, "enchantCombineRepairCost", 2);
        prop.comment = "Cost to increase an enchantment by a level";
        Config.enchantCombineRepairCost = prop.getInt(2);
        
        prop = config.get(BetterAnvil.CAT_ADJUSTMENTS, "enchantTransferRepairCost", 1);
        prop.comment = "Cost to transfer an enchantment to a tool";
        Config.enchantTransferRepairCost = prop.getInt(1);
        
        prop = config.get(BetterAnvil.CAT_ADJUSTMENTS, "enchantCombineRepairBonus", 2);
        prop.comment = "Repair bonus added when increasing an enchantment by a level";
        Config.enchantCombineRepairBonus = prop.getInt(2);
        
        prop = config.get(BetterAnvil.CAT_ADJUSTMENTS, "enchantTransferRepairBonus", 1);
        prop.comment = "Repair bonus added when transfering an enchantment to a tool";
        Config.enchantTransferRepairBonus = prop.getInt(1);
        
        prop = config.get(BetterAnvil.CAT_ADJUSTMENTS, "copyEnchantToBookCostMultiplier", 1);
        prop.comment = "Cost muliplier per enchantment copied onto a book\nThis is multiplied by the enchantment level";
        Config.copyEnchantToBookCostMultiplier = prop.getInt(2);
        
        prop = config.get(BetterAnvil.CAT_ADJUSTMENTS, "copyEnchantToBookRepairBonus", 1);
        prop.comment = "Repair bonus added when copying an enchantment to a book";
        Config.copyEnchantToBookRepairBonus = prop.getInt(1);
        
        prop = config.get(BetterAnvil.CAT_ADJUSTMENTS, "itemRepairAmount", 25);
        prop.comment = "Percentage each item will repair the tool by";
        Config.itemRepairAmount = prop.getInt(25) / 100.0D;
        config.save();
    }

    //Called during initialization, used for registering everything etc.
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandlerBA.INSTANCE);
        try {
            GameRegistry.addSubstitutionAlias("minecraft:anvil", Type.BLOCK, BetterAnvil.ANVIL);
            GameRegistry.addSubstitutionAlias("minecraft:anvil", Type.ITEM, new ItemAnvilBlock(BetterAnvil.ANVIL));
        } catch(ExistingSubstitutionException e) {
            e.printStackTrace();
        }
        //register gui
        NetworkRegistry.INSTANCE.registerGuiHandler(BetterAnvil.instance, new GuiHandler());
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent event) {
        for(Enchantment ench: Enchantment.enchantmentsList) {
            if(ench != null) {
                String enchName = Utils.getEnchName(ench);
                int defaulLimit = ench.getMaxLevel();
                int enchLimit = BetterAnvil.config.get("Enchantment Limits", enchName, defaulLimit).getInt(5);
                Config.ENCHANT_LIMITS.put(ench.effectId, enchLimit);
                ArrayList<String> defaultBlackList = new ArrayList<String>();
                for(Enchantment ench1: Enchantment.enchantmentsList) {
                    if(ench1 != null && ench1.effectId != ench.effectId && !ench.canApplyTogether(ench1)) {
                        String ench1Name = Utils.getEnchName(ench1);
                        defaultBlackList.add(ench1Name);
                    }
                }
                String[] enchBlackList = BetterAnvil.config.get("Enchantment Blacklist", enchName, defaultBlackList.toArray(new String[0])).getStringList();
                Config.ENCHANT_BLACK_LIST.put(ench.effectId, enchBlackList);
            }
        }
        config.save();
    }

    public static final class EventHandlerBA {

        public static final Object INSTANCE = new EventHandlerBA();

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void registerTextures(TextureStitchEvent e) {//Fixes FML/Forge bug.
            if (e.map.getTextureType() == 0) {//Block texture type.
                BetterAnvil.ANVIL.registerBlockIcons(e.map);
            }
        }

    }

}
