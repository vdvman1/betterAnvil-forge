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
    private static final String[] field_94431_cO = new String[] {"anvil_top", "anvil_top_damaged_1", "anvil_top_damaged_2"};
    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;

    public BlockAnvilBA(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (this.field_82521_b == 3 && par1 == 1)
        {
            int k = (par2 >> 2) % this.iconArray.length;
            return this.iconArray[k];
        }
        else
        {
            return this.blockIcon;
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("anvil_base");
        this.iconArray = new Icon[field_94431_cO.length];

        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = par1IconRegister.registerIcon(field_94431_cO[i]);
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            par5EntityPlayer.openGui(BetterAnvil.instance, 0, par1World, par2, par3, par4);
            return true;
        }
    }
}
