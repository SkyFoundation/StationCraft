package io.github.cvronmin.railwayp.block;

import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockColorful extends BlockContainer{

	public BlockColorful(){
		this(Material.CLAY);
	}
	public BlockColorful(Material material) {
		super(material);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
    public int getRenderType()
    {
        return 3;
    }
    public int tickRate(World worldIn)
    {
        return 5;
    }
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityColorful();
	}
}
