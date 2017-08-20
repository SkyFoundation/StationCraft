package io.github.cvronmin.railwayp.block;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import io.github.cvronmin.railwayp.tileentity.TileEntityRailNoticer;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailNoticer extends BlockRailBase implements ITileEntityProvider{

    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.<BlockRailBase.EnumRailDirection>create("shape", BlockRailBase.EnumRailDirection.class, p_apply_1_ -> p_apply_1_ != EnumRailDirection.NORTH_EAST && p_apply_1_ != EnumRailDirection.NORTH_WEST && p_apply_1_ != EnumRailDirection.SOUTH_EAST && p_apply_1_ != EnumRailDirection.SOUTH_WEST);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private boolean readyupdate = true;
    public BlockRailNoticer()
    {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)).withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
        this.setTickRandomly(true);
        this.isBlockContainer = true;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 20;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (!state.getValue(POWERED).booleanValue())
            {
                this.updatePoweredState(worldIn, pos, state);
            }
        }
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote && state.getValue(POWERED).booleanValue())
        {
            this.updatePoweredState(worldIn, pos, state);
        }
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return state.getValue(POWERED).booleanValue() ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !state.getValue(POWERED).booleanValue() ? 0 : (side == EnumFacing.UP ? 15 : 0);
    }

    private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean flag = state.getValue(POWERED).booleanValue();
        boolean flag1 = false;
        List<EntityMinecart> list = this.<EntityMinecart>findMinecarts(worldIn, pos, EntityMinecart.class, new Predicate[0]);

        if (!list.isEmpty())
        {
            flag1 = true;
        }

        if (flag1 && !flag)
        {
/*            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);*/
        }

        if (!flag1 && flag)
        {
/*            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 3);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);*/
            this.readyupdate = true;
        }

        if (flag1)
        {
            worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
        }

//        worldIn.updateComparatorOutputLevel(pos, this);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.updatePoweredState(worldIn, pos, state);
    }

    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty()
    {
        return SHAPE;
    }

/*    public boolean hasComparatorInputOverride()
    {
        return true;
    }*/

/*    public int getComparatorInputOverride(World worldIn, BlockPos pos)
    {
        if (((Boolean)worldIn.getBlockState(pos).getValue(POWERED)).booleanValue())
        {
            List list = this.findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, new Predicate[0]);

            if (!list.isEmpty())
            {
                return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlockLogic().getSuccessCount();
            }

            List list1 = this.findMinecarts(worldIn, pos, EntityMinecart.class, new Predicate[] {IEntitySelector.selectInventories});

            if (!list1.isEmpty())
            {
                return Container.calcRedstoneFromInventory((IInventory)list1.get(0));
            }
        }

        return 0;
    }*/

    protected <T extends EntityMinecart> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> clazz, Predicate<Entity>... filter)
    {
        AxisAlignedBB axisalignedbb = this.getDectectionBox(pos);
        return filter.length != 1 ? worldIn.getEntitiesWithinAABB(clazz, axisalignedbb) : worldIn.getEntitiesWithinAABB(clazz, axisalignedbb, filter[0]);
    }

    private AxisAlignedBB getDectectionBox(BlockPos pos)
    {
        float f = 0.2F;
        return new AxisAlignedBB((double)((float)pos.getX() + 0.2F), (double)pos.getY(), (double)((float)pos.getZ() + 0.2F), (double)((float)(pos.getX() + 1) - 0.2F), (double)((float)(pos.getY() + 1) - 0.2F), (double)((float)(pos.getZ() + 1) - 0.2F));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | state.getValue(SHAPE).getMetadata();

        if (state.getValue(POWERED).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    public void onMinecartPass(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos)
    {
	TileEntity te = world.getTileEntity(pos);
	Entity eti = cart.getRidingEntity();
	if(readyupdate && te instanceof TileEntityRailNoticer && eti != null && eti instanceof EntityPlayerMP){
		((TileEntityRailNoticer)te).sendNotice((EntityPlayerMP) eti);
//		readyupdate = false;
	}
    }
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {SHAPE, POWERED});
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
	return new TileEntityRailNoticer();
    }
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @SuppressWarnings("incomplete-switch")
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:

                switch (state.getValue(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                }

            case COUNTERCLOCKWISE_90:

                switch (state.getValue(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                }

            case CLOCKWISE_90:

                switch (state.getValue(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                }

            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @SuppressWarnings("incomplete-switch")
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(SHAPE);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:

                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    default:
                        return super.withMirror(state, mirrorIn);
                }

            case FRONT_BACK:

                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                }
        }

        return super.withMirror(state, mirrorIn);
    }
    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
    	super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
}
