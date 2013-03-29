package vdvman1.betterAnvil;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.Configuration;
import vdvman1.betterAnvil.block.BlockAnvilBA;
import vdvman1.betterAnvil.gui.GuiHandler;
import vdvman1.betterAnvil.packet.PacketHandler;
import vdvman1.betterAnvil.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = BetterAnvil.modid, name = "Better Anvils", version = "4.0b1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {BetterAnvil.channel}, packetHandler = PacketHandler.class)
public class BetterAnvil {
	
	//Global variables
	public static final String modid = "BetterAnvil";
	public static final String channel = "BetterAnvil";
	
	//Blocks
	public Block anvil;
	
	//Configuration
	public static double breakChance;
	public static double costMultiplier;
	
	@Instance(BetterAnvil.modid)
	public static BetterAnvil instance;
	
	@SidedProxy(clientSide="vdvman1.betterAnvil.proxy.ClientProxy", serverSide = "vdvman1.betterAnvil.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	//Called before initialization, usually used for configuration
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		breakChance = config.get(Configuration.CATEGORY_GENERAL, "breakChance", "0.12").getDouble(0.12);
		costMultiplier = config.get(Configuration.CATEGORY_GENERAL, "anvilCostMultiplier", "1").getDouble(1);
		config.save();
	}
	
	//Called during initialization, used for registering everything etc.
	@SuppressWarnings("unchecked")
	@Init
	public void init(FMLInitializationEvent event) {
		//Replace BlockAnvil in Block.class with BlockAnvilBA
		try {
			Class<?> block = Block.class;
			//Field MCAnvil = block.getDeclaredField("anvil");
			Field MCAnvil = block.getDeclaredFields()[166];
			Block.blocksList[145] = null;
			anvil = (new BlockAnvilBA(145)).setHardness(5.0F).setStepSound(Block.soundAnvilFootstep).setResistance(2000.0F).setUnlocalizedName("anvil");
			Utils.setFinalStatic(MCAnvil, anvil);
			Item.itemsList[145] = null;
			Item.itemsList[145] = (new ItemAnvilBlock(anvil)).setUnlocalizedName("anvil");
			Block.blocksList[145] = anvil;
		} catch (NoSuchFieldException e) {
			System.out.println("Could not replace BlockAnvil, NoSuchFieldException.\nThis should never happen!\nPlease let vdvman1 know which mods you are using!");
			e.printStackTrace();
			System.out.println("Disabling Better Anvils");
			return;
		} catch (SecurityException e) {
			System.out.println("Could not replace BlockAnvil, SecurityException.\nYou are using too high security levels, please lower them.\nDisabling Better Anvils");
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Could not replace BlockAnvil, IllegalArgumentException.\nThis should never happen!\nThis means that BlockAnvil could not be set to BlockAnvilBA.\nPlease let vdvman1 know ASAP!");
			e.printStackTrace();
			System.out.println("Disabling Better Anvils");
			return;
		} catch (IllegalAccessException e) {
			System.out.println("Could not replace BlockAnvil, IllegalAccessException.\nThis should never happen!\nThis means that Better Anvils could not access BlockAnvil.\nPlease let vdvman1 know!");
			e.printStackTrace();
			System.out.println("Disabling Better Anvils");
			return;
		}
		//Replace recipe for the anvil to craft the correct anvil
		try {
			Class<CraftingManager> craftingManager = CraftingManager.class;
			//Field cmInstanceField = craftingManager.getDeclaredField("instance");
			Field cmInstanceField = craftingManager.getDeclaredFields()[0];
			cmInstanceField.setAccessible(true);
			CraftingManager cmInstance = (CraftingManager) cmInstanceField.get(craftingManager);
			//Field recipesField = craftingManager.getDeclaredField("recipes");
			Field recipesField = craftingManager.getDeclaredFields()[1];
			recipesField.setAccessible(true);
			List<IRecipe> recipes = (List<IRecipe>) recipesField.get(cmInstance);
			for(IRecipe _recipe: recipes) {
				if(_recipe instanceof ShapedRecipes) {
					ShapedRecipes recipe = (ShapedRecipes)_recipe;
					if (recipe.recipeOutputItemID == 145) {
						recipes.remove(recipe);
						GameRegistry.addShapedRecipe(new ItemStack(Block.anvil, 1), new Object[] {"III", " i ", "iii", 'I', Block.blockSteel, 'i', Item.ingotIron});
						break;
					}
				}
			}
		} catch (SecurityException e) {
			System.out.println("Could not replace anvil recipe, SecurityException.\nYou are using too high security levels, please lower them.\nBetter Anvils may/may not work at this stage");
		} catch (IllegalArgumentException e) {
			System.out.println("Could not replace anvil recipe, IllegalArgumentException.\nThis should never happen!\nThis means that the anvil recipe could not be set/retrieved.\nPlease let vdvman1 know ASAP!");
			e.printStackTrace();
			System.out.println("Better Anvils may/may not work at this stage");
		} catch (IllegalAccessException e) {
			System.out.println("Could not replace anvil recipe, IllegalAccessException.\nThis should never happen!\nThis means that Better Anvils could not access the anvil recipe.\nPlease let vdvman1 know!");
			e.printStackTrace();
			System.out.println("Better Anvils may/may not work at this stage");
		}
		
		//register gui
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
	}

}
