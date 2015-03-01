package vdvman1.betterAnvil.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import vdvman1.betterAnvil.BetterAnvil;

public class BlockAnvilBA extends BlockAnvil
{

    public BlockAnvilBA() {
        setHardness(5.0F);
        setStepSound(Block.soundTypeAnvil);
        setResistance(2000.0F);
        setBlockName("anvil");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int sideHit, float sideHitX, float sideHitY, float sideHitZ)
    {
        entityPlayer.openGui(BetterAnvil.instance, 0, world, x, y, z);
        return true;
    }
}
