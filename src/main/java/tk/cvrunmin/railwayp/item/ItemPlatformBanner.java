package tk.cvrunmin.railwayp.item;

import java.util.List;

import tk.cvrunmin.railwayp.init.RPBlocks;
import tk.cvrunmin.railwayp.init.RPItems;
import tk.cvrunmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPlatformBanner extends Item
{

    public ItemPlatformBanner()
    {
        super();
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    public ItemPlatformBanner(Block block){
    	this();
    }
    
    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side == EnumFacing.DOWN || side == EnumFacing.UP)
        {
            return false;
        }
        else if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
        {
            return false;
        }
        else
        {
            pos = pos.offset(side);

            if (!playerIn.canPlayerEdit(pos, side, stack))
            {
                return false;
            }
            else if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                worldIn.setBlockState(pos, RPBlocks.wall_platform_banner.getDefaultState().withProperty(BlockWallSign.FACING, side), 3);
                --stack.stackSize;
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntityPlatformBanner)
                {
                    ((TileEntityPlatformBanner)tileentity).setItemValues(stack);
                }

                return true;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
            return 16777215;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *  
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
/*    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        int i = aenumdyecolor.length;

        for (int j = 0; j < i; ++j)
        {
            EnumDyeColor enumdyecolor = aenumdyecolor[j];
            subItems.add(new ItemStack(itemIn, 1, enumdyecolor.getDyeDamage()));
        }
    }*/

    /**
     * gets the CreativeTab this item is displayed on
     */
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab()
    {
        return CreativeTabs.tabDecorations;
    }

}