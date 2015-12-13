package vdvman1.betterAnvil.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import vdvman1.betterAnvil.client.gui.GuiRepairBA;
import vdvman1.betterAnvil.inventory.ContainerRepairBA;
import cpw.mods.fml.common.network.IGuiHandler;

public final class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case 0:
                return new ContainerRepairBA(player.inventory, world, x, y, z, player);
            default:
                return null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case 0:
                return new GuiRepairBA(player.inventory, world, x, y, z);
            default:
                return null;
        }
    }

}
