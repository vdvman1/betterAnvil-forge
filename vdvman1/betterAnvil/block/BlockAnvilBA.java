package vdvman1.betterAnvil.block;

import net.minecraft.block.BlockAnvil;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vdvman1.betterAnvil.BetterAnvil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAnvilBA extends BlockAnvil
{

    public BlockAnvilBA(int id)
    {
        super(id);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int sideHit, float sideHitX, float sideHitY, float sideHitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            entityPlayer.openGui(BetterAnvil.instance, 0, world, x, y, z);
            return true;
        }
    }
}
