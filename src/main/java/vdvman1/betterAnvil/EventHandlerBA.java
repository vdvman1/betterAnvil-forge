package vdvman1.betterAnvil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.AnvilUpdateEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 3/15/2015 at 12:07 PM.
 * @author Master801
 */
public final class EventHandlerBA {

    public static final Object INSTANCE = new EventHandlerBA();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerTextures(TextureStitchEvent e) {//Fixes FML/Forge bug.
        if (e.map.getTextureType() == 0) {//Block texture type.
            BetterAnvil.ANVIL.registerBlockIcons(e.map);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void anvilGui(AnvilUpdateEvent e) {
        //TODO
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onButtonPress(ActionPerformedEvent.Pre e) {
        if (e.gui instanceof GuiRepair) {
            final GuiRepair repair = (GuiRepair)e.gui;

            switch(e.button.id) {
                //TODO
                //
                default:
                    break;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void addButtons(InitGuiEvent.Post e) {
        if (e.gui instanceof GuiRepair) {
            final List<GuiButton> list = new ArrayList<GuiButton>();
            //TODO Add buttons.



            e.buttonList.addAll(list);
        }
    }

    @SubscribeEvent
    public void refundPlayer(ItemPickupEvent e) {
        if (BetterAnvil.shouldRefundPlayer && !e.player.worldObj.isRemote && e.pickedUp.getEntityItem().getItem() instanceof ItemAnvilBlock) {
            e.player.inventory.addItemStackToInventory(new ItemStack(Items.iron_ingot, 3, 0));//We only give out three ingots, because we're already replacing the alpha anvil with a workable anvil.
            BetterAnvil.shouldRefundPlayer = false;
        }
    }

}
