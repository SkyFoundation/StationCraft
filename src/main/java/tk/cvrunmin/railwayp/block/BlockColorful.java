package tk.cvrunmin.railwayp.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tk.cvrunmin.railwayp.tileentity.TileEntityColorful;

public class BlockColorful extends BlockContainer{

	public BlockColorful(){
		this(Material.clay);
	}
	public BlockColorful(Material material) {
		super(material);
		setCreativeTab(CreativeTabs.tabBlock);
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
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
    	TileEntity te = worldIn.getTileEntity(pos);
        return ((TileEntityColorful) te).getColor();
    }
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityColorful();
	}
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        	this.colorMultiplier(worldIn, pos);
    }
}
