package vdvman1.betterAnvil.block;

import vdvman1.betterAnvil.BetterAnvil;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockAnvilBA extends BlockAnvil
{
    public BlockAnvilBA(int id) {
		super(id);
	}

	/**
     * Called upon block activation (right click on the block.)
     */
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        player.openGui(BetterAnvil.instance, 0, world, x, y, z);
        return true;
    }
}
