package tk.cvrunmin.railwayp.block;

import java.util.Random;

import tk.cvrunmin.railwayp.init.RPItems;
import tk.cvrunmin.railwayp.tileentity.TileEntityWHPF;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWHPF extends BlockContainer{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
    public BlockWHPF() {
        super(Material.wood);
        float f = 0.25F;
        float f1 = 1.0F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
	// TODO Auto-generated method stub
	return new TileEntityWHPF();
    }
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return RPItems.whpf;
    }
    
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityWHPF)
        {
            ItemStack itemstack = new ItemStack(RPItems.whpf, 1);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            tileentity.writeToNBT(nbttagcompound);
            nbttagcompound.removeTag("x");
            nbttagcompound.removeTag("y");
            nbttagcompound.removeTag("z");
            nbttagcompound.removeTag("id");
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }
    
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
    {
        if (te instanceof TileEntityWHPF)
        {
            ItemStack itemstack = new ItemStack(RPItems.whpf, 1);
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
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return RPItems.whpf;
    }
    
    public static class BlockBannerHanging extends BlockWHPF
    {

        public BlockBannerHanging()
        {
        	this.disableStats();
            this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        }

        public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
        {
            EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
            float f = 0.0F;
            f=0.109375f;
            float f1 = 0.78125F;
            f1=0.890625f;
            float f2 = 0.0F;
            float f3 = 1.0F;
            float f4 = 0.125F;
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

            switch (BlockWHPF.SwitchEnumFacing.FACING_LOOKUP[enumfacing.ordinal()])
            {
                case 1:
                default:
                    this.setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
                    break;
                case 2:
                    this.setBlockBounds(f2, f, 0.0F, f3, f1, f4);
                    break;
                case 3:
                    this.setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
                    break;
                case 4:
                    this.setBlockBounds(0.0F, f, f2, f4, f1, f3);
            }
        }

        /**
         * Called when a neighboring block changes.
         */
        public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
        {
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

                if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid())
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }

        /**
         * Convert the given metadata into a BlockState for this Block
         */
        public IBlockState getStateFromMeta(int meta)
        {
            EnumFacing enumfacing = EnumFacing.getFront(meta);

            if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            {
                enumfacing = EnumFacing.NORTH;
            }

            return this.getDefaultState().withProperty(FACING, enumfacing);
        }

        /**
         * Convert the BlockState into the correct metadata value
         */
        public int getMetaFromState(IBlockState state)
        {
            return ((EnumFacing)state.getValue(FACING)).getIndex();
        }

        protected BlockState createBlockState()
        {
            return new BlockState(this, new IProperty[] {FACING});
        }
    }
    
    public static class BlockBannerStanding extends BlockWHPF
    {

        public BlockBannerStanding()
        {
            this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, Integer.valueOf(0)));
        }

        /**
         * Called when a neighboring block changes.
         */
        public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
        {
            if (!worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid())
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }

        /**
         * Convert the given metadata into a BlockState for this Block
         */
        public IBlockState getStateFromMeta(int meta)
        {
            return this.getDefaultState().withProperty(ROTATION, Integer.valueOf(meta));
        }

        /**
         * Convert the BlockState into the correct metadata value
         */
        public int getMetaFromState(IBlockState state)
        {
            return ((Integer)state.getValue(ROTATION)).intValue();
        }

        protected BlockState createBlockState()
        {
            return new BlockState(this, new IProperty[] {ROTATION});
        }
    }

static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

        static
        {
            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

}
