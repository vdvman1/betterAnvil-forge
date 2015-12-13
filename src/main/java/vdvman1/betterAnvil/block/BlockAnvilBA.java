package vdvman1.betterAnvil.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vdvman1.betterAnvil.BetterAnvil;
import vdvman1.betterAnvil.inventory.TileEntityBA;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class BlockAnvilBA extends BlockAnvil implements ITileEntityProvider {
    
    //Icon names
    private static final String[] ANVIL_ICON_NAMES = new String[] {
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_base",
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_top_damaged_0",
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_top_damaged_1",
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_top_damaged_2"
    };

    @SideOnly(Side.CLIENT)
    private IIcon[] anvilIcons;

    public BlockAnvilBA() {
        setHardness(5.0F);
        setStepSound(BetterAnvil.SOUND_TYPE_BETTER_ANVIL);
        setResistance(2000.0F);
        setBlockName("betterAnvil");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int sideHit, float sideHitX, float sideHitY, float sideHitZ) {
        entityPlayer.openGui(BetterAnvil.instance, 0, world, x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (anvilRenderSide == 3 && side == 1) return anvilIcons[((meta >> 2)+1) % anvilIcons.length];
        return anvilIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        anvilIcons = new IIcon[BlockAnvilBA.ANVIL_ICON_NAMES.length];
        for(int i = 0; i < anvilIcons.length; ++i) anvilIcons[i] = iconRegister.registerIcon(BlockAnvilBA.ANVIL_ICON_NAMES[i]);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBA();
    }
    
    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        TileEntity tileentity = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

        if (tileentity instanceof TileEntityBA)
        {
            TileEntityBA tile = (TileEntityBA)tileentity;

            for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tile.getStackInSlot(i1);

                if (itemstack != null)
                {
                    Random rand = new Random();
                    float f = rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityitem = new EntityItem(p_149749_1_, (double)((float)p_149749_2_ + f), (double)((float)p_149749_3_ + f1), (double)((float)p_149749_4_ + f2), itemstack.copy());
                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)rand.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)rand.nextGaussian() * f3);
                    p_149749_1_.spawnEntityInWorld(entityitem);
                }
            }
        }

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

}
