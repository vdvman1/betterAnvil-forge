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
	private static final String[] ANVIL_ICON_NAMES = new String[] {
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_base",
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_top_damaged_0",
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_top_damaged_1",
            BetterAnvil.MOD_ID.toLowerCase() + ":" + "better_anvil_top_damaged_2"
    };

    @SideOnly(Side.CLIENT)
    private final IIcon[] anvilIcons = new IIcon[BlockAnvilBA.ANVIL_ICON_NAMES.length];

    public BlockAnvilBA() {
        setHardness(5.0F);
        setStepSound(Block.soundTypeAnvil);
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
        if (anvilRenderSide == 3 && side == 1) return anvilIcons[(meta >> 2) % anvilIcons.length];
        return anvilIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        for(int i = 0; i < anvilIcons.length; ++i) anvilIcons[i] = iconRegister.registerIcon(BlockAnvilBA.ANVIL_ICON_NAMES[i]);
    }

}
