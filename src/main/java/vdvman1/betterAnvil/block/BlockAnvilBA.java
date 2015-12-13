package vdvman1.betterAnvil.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vdvman1.betterAnvil.BetterAnvil;
import vdvman1.betterAnvil.inventory.TileEntityBA;

public final class BlockAnvilBA extends BlockAnvil implements ITileEntityProvider {

    //Icon names
    private static final String[] BETTER_ANVIL_ICON_NAMES = new String[] {
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
        if (anvilRenderSide == 3 && side == 1) return anvilIcons[((meta >> 2) + 1) % anvilIcons.length];
        return anvilIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        anvilIcons = new IIcon[BlockAnvilBA.BETTER_ANVIL_ICON_NAMES.length];
        for(int i = 0; i < anvilIcons.length; ++i)
            anvilIcons[i] = iconRegister.registerIcon(BlockAnvilBA.BETTER_ANVIL_ICON_NAMES[i]);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBA();
    }

    @Override
    public void breakBlock(World world, int xCoord, int yCoord, int zCoord, Block block, int metadata) {
        TileEntity tileentity = world.getTileEntity(xCoord, yCoord, zCoord);
        if (tileentity instanceof TileEntityBA) {
            TileEntityBA tile = (TileEntityBA) tileentity;
            for(int i = 0; i < tile.getSizeInventory(); ++i) {
                ItemStack itemstack = tile.getStackInSlot(i);
                if (itemstack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                    final float f3 = 0.05F;

                    EntityItem entityitem = new EntityItem(world, (double) xCoord + f, (double) yCoord + f1, (double) zCoord + f2, itemstack.copy());
                    entityitem.motionX = world.rand.nextGaussian() * f3;
                    entityitem.motionY = world.rand.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = world.rand.nextGaussian() * f3;
                    world.spawnEntityInWorld(entityitem);
                }
            }
        }
        super.breakBlock(world, xCoord, yCoord, zCoord, block, metadata);
    }

}
