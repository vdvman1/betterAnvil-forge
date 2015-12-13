package vdvman1.betterAnvil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vdvman1.betterAnvil.block.BlockAnvilBA;
import vdvman1.betterAnvil.common.Config;
import vdvman1.betterAnvil.common.EventHandlerBA;
import vdvman1.betterAnvil.common.GuiHandler;
import vdvman1.betterAnvil.common.Utils;
import vdvman1.betterAnvil.inventory.TileEntityBA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = BetterAnvil.MOD_ID, name = BetterAnvil.MOD_NAME, version = BetterAnvil.VERSION, guiFactory = "vdvman1.betterAnvil.client.gui.config.ConfigFactoryBA")
public final class BetterAnvil {

    //Global variables
    public static final String MOD_ID = "BetterAnvil", MOD_NAME = "Better Anvils", VERSION = "@VERSION@";

    //Logger
    public static final Logger BETTER_ANVIL_LOGGER = LogManager.getLogger(BetterAnvil.MOD_NAME);

    //Sound type
    public static final Block.SoundType SOUND_TYPE_BETTER_ANVIL = new Block.SoundType("anvil", 0.3F, 1.0F) {
        
        @Override
        public String getBreakSound() {
            return "dig.stone";
        }
        
        @Override
        public String func_150496_b() {
            return "random.anvil_land";
        }
        
        @Override
        public String getStepResourcePath() {
            return MOD_ID+":step.anvil";
        }
    };

    //Blocks
    public static final BlockAnvilBA BLOCK_BETTER_ANVIL = new BlockAnvilBA();
    
    @Instance(BetterAnvil.MOD_ID)
    public static BetterAnvil instance;

    //Called before initialization, usually used for configuration
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.setConfiguration(new Configuration(event.getSuggestedConfigurationFile()));
        Config.syncConfiguration(true);

        GameRegistry.registerBlock(BetterAnvil.BLOCK_BETTER_ANVIL, ItemAnvilBlock.class, "better_anvil");
        GameRegistry.registerTileEntity(TileEntityBA.class, "tile_entity_better_anvil");
    }

    //Called during initialization, used for registering everything etc.
    @EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.addShapedRecipe(new ItemStack(BetterAnvil.BLOCK_BETTER_ANVIL, 1, 0), "DAD", "XDX", 'X', Items.iron_ingot, 'A', new ItemStack(Blocks.anvil, 1, 0), 'D', Items.diamond);//Only use a new anvil (that is not damaged) for the crafting recipe.

        MinecraftForge.EVENT_BUS.register(EventHandlerBA.INSTANCE);
        FMLCommonHandler.instance().bus().register(EventHandlerBA.INSTANCE);

        //register gui
        NetworkRegistry.INSTANCE.registerGuiHandler(BetterAnvil.instance, new GuiHandler());
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent event) {
        Config.loadLists(true);
    }

    @EventHandler
    public void missingMappings(FMLMissingMappingsEvent event) {
        for(MissingMapping missingMapping : event.get()) {
            if (missingMapping.name.equals("BetterAnvil:anvilba")) {
                switch(missingMapping.type) {
                    case BLOCK:
                        missingMapping.remap(BetterAnvil.BLOCK_BETTER_ANVIL);
                        break;
                    case ITEM:
                        missingMapping.remap(Item.getItemFromBlock(BetterAnvil.BLOCK_BETTER_ANVIL));
                        break;
                }
            }
        }
    }

}
