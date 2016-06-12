package io.github.cvronmin.railwayp.item;

import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNameBanner extends Item
{

    public ItemNameBanner()
    {
        super();
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    public ItemNameBanner(Block block){
    	this();
    }
    
    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.DOWN || facing == EnumFacing.UP)
        {
            return false;
        }
        else if (!state.getBlock().getMaterial().isSolid())
        {
            return false;
        }
        else
        {
            pos = pos.offset(facing);

            if (!playerIn.canPlayerEdit(pos, facing, stack))
            {
                return false;
            }
            else if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                worldIn.setBlockState(pos, RPBlocks.wall_name_banner.getDefaultState().withProperty(BlockWallSign.FACING, facing), 3);
                --stack.stackSize;
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntityNameBanner)
                {
                    ((TileEntityNameBanner)tileentity).setItemValues(stack);
                }

                return true;
            }
        }
    }

}