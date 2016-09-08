package io.github.cvronmin.railwayp.block;

import io.github.cvronmin.railwayp.init.RPItems;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityColorful();
	}
    private ItemStack getTileDataItemStack(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityColorful)
        {
            ItemStack itemstack = new ItemStack(this, 1);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            tileentity.writeToNBT(nbttagcompound);
            nbttagcompound.removeTag("x");
            nbttagcompound.removeTag("y");
            nbttagcompound.removeTag("z");
            nbttagcompound.removeTag("id");
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
            return itemstack;
        }
        else
        {
            return null;
        }
        
    }
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        ItemStack itemstack = this.getTileDataItemStack(worldIn, pos, state);
        return itemstack != null ? itemstack : new ItemStack(this);
    }
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
        if (te instanceof TileEntityColorful)
        {
            ItemStack itemstack = new ItemStack(this, 1);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            te.writeToNBT(nbttagcompound);
            nbttagcompound.removeTag("x");
            nbttagcompound.removeTag("y");
            nbttagcompound.removeTag("z");
            nbttagcompound.removeTag("id");
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
        }
    }
    @Override
    public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileEntity te = world.getTileEntity(pos);

        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        if (te instanceof TileEntityColorful)
        {
        	TileEntityColorful banner = (TileEntityColorful)te;
            ItemStack item = new ItemStack(this, 1);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            te.writeToNBT(nbttagcompound);
            nbttagcompound.removeTag("x");
            nbttagcompound.removeTag("y");
            nbttagcompound.removeTag("z");
            nbttagcompound.removeTag("id");
            item.setTagInfo("BlockEntityTag", nbttagcompound);
            ret.add(item);
        }
        else
        {
            ret.add(new ItemStack(this, 1));
        }
        return ret;
    }
}
