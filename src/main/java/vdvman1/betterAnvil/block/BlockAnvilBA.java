package vdvman1.betterAnvil.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vdvman1.betterAnvil.BetterAnvil;

public final class BlockAnvilBA extends BlockAnvil {
	
	//Icon names
	private static final String[] anvilIconNames = new String[] {BetterAnvil.MOD_ID.toLowerCase()+":better_anvil_top_damaged_0", BetterAnvil.MOD_ID.toLowerCase()+":better_anvil_top_damaged_1", BetterAnvil.MOD_ID.toLowerCase()+":better_anvil_top_damaged_2"};

    @SideOnly(Side.CLIENT)
    private IIcon[] anvilIcons;  

    public BlockAnvilBA() {
        setHardness(5.0F);
        setStepSound(Block.soundTypeAnvil);
        setResistance(2000.0F);
        setBlockName("betterAnvil");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int sideHit, float sideHitX, float sideHitY, float sideHitZ) {
        entityPlayer.openGui(BetterAnvil.instance, 0, world, x, y, z);
        return true;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (this.anvilRenderSide == 3 && side == 1)
        {
            int k = (meta >> 2) % this.anvilIcons.length;
            return this.anvilIcons[k];
        }
        else
        {
            return this.blockIcon;
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(BetterAnvil.MOD_ID.toLowerCase()+":better_anvil_base");
        this.anvilIcons = new IIcon[anvilIconNames.length];

        for (int i = 0; i < this.anvilIcons.length; ++i)
        {
            this.anvilIcons[i] = iconRegister.registerIcon(anvilIconNames[i]);
        }
    }

}
