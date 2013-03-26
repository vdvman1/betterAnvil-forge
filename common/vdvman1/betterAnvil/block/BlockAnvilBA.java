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
    private static final String[] textureNames = new String[] {"anvil_top", "anvil_top_damaged_1", "anvil_top_damaged_2"};
    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;

    public BlockAnvilBA(int id)
    {
        super(id);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
    public Icon getBlockTextureFromSideAndMetadata(int side, int metadata)
    {
        if (this.field_82521_b == 3 && side == 1) //this.field_82521_b is from normal anvil, is used to represent the section of the anvil being represented, from bottom to top, 0-3
        {
            int k = (metadata >> 2) % this.iconArray.length;
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
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("anvil_base");
        this.iconArray = new Icon[textureNames.length];

        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = iconRegister.registerIcon(textureNames[i]);
        }
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
