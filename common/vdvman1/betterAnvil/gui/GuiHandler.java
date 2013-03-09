package vdvman1.betterAnvil.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import vdvman1.betterAnvil.inventory.ContainerRepairBA;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) {
		if(ID == 0) {
			return new ContainerRepairBA(player.inventory, world, x, y, z, player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0) {
			return new GuiRepairBA(player.inventory, world, z, z, z);
		}
		return null;
	}

}
