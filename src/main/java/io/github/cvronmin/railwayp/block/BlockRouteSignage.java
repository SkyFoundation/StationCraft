package io.github.cvronmin.railwayp.block;

import java.util.Random;

import io.github.cvronmin.railwayp.init.RPItems;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRouteSignage extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

    protected BlockRouteSignage()
    {
        super(Material.wood);
    }

    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }
    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    /**
     * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
     */
    public boolean canSpawnInBlock()
    {
        return true;
    }
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityRouteSignage();
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return RPItems.route_sign;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityRouteSignage)
        {
            ItemStack itemstack = new ItemStack(RPItems.route_sign, 1);
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

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)    {
        if (te instanceof TileEntityRouteSignage)
        {
            ItemStack itemstack = new ItemStack(RPItems.route_sign, 1);
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

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return RPItems.route_sign;
    }

    public static class BlockBannerHanging extends BlockRouteSignage
        {

            public BlockBannerHanging()
            {
            	this.disableStats();
                this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
            }

            protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.109375, 0.875D, 1.0D, 0.890625, 1.0D);
            protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.109375f, 0.0D, 1.0D, 0.890625f, 0.125D);
            protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875, 0.109375f, 0.0D, 1.0D, 0.890625f, 1.0D);
            protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.109375f, 0.0D, 0.125D, 0.890625f, 1.0D);
            public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
            {
                switch ((EnumFacing)state.getValue(FACING))
                {
                    case NORTH:
                    default:
                        return NORTH_AABB;
                    case SOUTH:
                        return SOUTH_AABB;
                    case WEST:
                        return WEST_AABB;
                    case EAST:
                        return EAST_AABB;
                }
            }
            /**
             * Called when a neighboring block changes.
             */
            public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
            {
                EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

/*                if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid())
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    worldIn.setBlockToAir(pos);
                }*/

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

            protected BlockStateContainer createBlockState()
            {
                return new BlockStateContainer(this, new IProperty[] {FACING});
            }
        }
}