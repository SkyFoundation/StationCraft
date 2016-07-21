package io.github.cvronmin.railwayp.block;

import java.util.List;
import java.util.Random;

import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.tileentity.TileEntityPFDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlatformDoor {

	public static class Base extends Block {
	    public static final PropertyDirection FACING = BlockHorizontal.FACING;
	    public static final PropertyBool LEFTY = PropertyBool.create("lefty");
	    public static final PropertyBool EXTENDED = PropertyBool.create("extended");
	    public static final PropertyBool POWERED = PropertyBool.create("powered");

	    public Base()
	    {
	        super(Material.GLASS);
	        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEFTY, false).withProperty(EXTENDED, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)));
	        this.setSoundType(SoundType.STONE);
	        this.setHardness(0.5F);
	        this.setCreativeTab(CreativeTabs.REDSTONE);
	    }
	    @SideOnly(Side.CLIENT)
	    public BlockRenderLayer getBlockLayer()
	    {
	        return BlockRenderLayer.CUTOUT_MIPPED;
	    }

	    public boolean canProvidePower()
	    {
	        return true;
	    }
	    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	    {
	        return side != EnumFacing.DOWN ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
//	        return ((Boolean)state.getValue(POWERED)).booleanValue() && state.getValue(FACING) != side ? 15 : 0;
	    }
	    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	    {
//	    	return ((Boolean)state.getValue(POWERED)).booleanValue() && state.getValue(FACING) != side ? 15 : 0;
	        return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
	    }
	    public boolean isOpaqueCube()
	    {
	        return false;
	    }

	    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	    {
	        worldIn.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)).withProperty(LEFTY, shouldBlockPlacedAsLefty(worldIn, pos, placer)), 2);

	        if (!worldIn.isRemote)
	        {
	            this.checkForMove(worldIn, pos, state);
	        }
	    }

	    /**
	     * Called when a neighboring block changes.
	     */
	    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	    {
	        if (!worldIn.isRemote)
	        {
	            this.checkForMove(worldIn, pos, state);
	        }
	    }

	    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	    {
	        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
	        {
	            this.checkForMove(worldIn, pos, state);
	        }
	    }

	    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	    {
	        return this.getDefaultState().withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)).withProperty(LEFTY, shouldBlockPlacedAsLefty(worldIn,pos,placer)).withProperty(EXTENDED, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false));
	    }

	    private boolean shouldBlockPlacedAsLefty(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn) {
	    	EnumFacing sf = getFacingFromEntity(worldIn, clickedBlock, entityIn);
	    	if(sf.getAxisDirection() == AxisDirection.POSITIVE){
	    		if(sf.getAxis() == Axis.Z)
	    			return entityIn.posX > clickedBlock.getX();
		    	if(sf.getAxis() == Axis.X)
	    			return entityIn.posZ < clickedBlock.getZ();
	    	}
	    	else {
	    		if(sf.getAxis() == Axis.Z)
	    			return entityIn.posX < clickedBlock.getX();
		    	if(sf.getAxis() == Axis.X)
	    			return entityIn.posZ > clickedBlock.getZ();
	    	}
			return false;
		}
	    public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
	    {
	        if (MathHelper.abs((float)entityIn.posX - (float)clickedBlock.getX()) < 2.0F && MathHelper.abs((float)entityIn.posZ - (float)clickedBlock.getZ()) < 2.0F)
	        {
	            double d0 = entityIn.posY + (double)entityIn.getEyeHeight();

/*	            if (d0 - (double)clickedBlock.getY() > 2.0D)
	            {
	                return EnumFacing.UP;
	            }

	            if ((double)clickedBlock.getY() - d0 > 0.0D)
	            {
	                return EnumFacing.DOWN;
	            }*/
	        }

	        return entityIn.getHorizontalFacing().getOpposite();
	    }
		private void checkForMove(World worldIn, BlockPos pos, IBlockState state)
	    {
	        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
	        boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);

	        if (flag && !((Boolean)state.getValue(EXTENDED)).booleanValue())
	        {
	            if ((new DoorStructureHelper(worldIn, pos, enumfacing, true)).canMove())
	            {
	                worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
	            }
	        }
	        else if (!flag && ((Boolean)state.getValue(EXTENDED)).booleanValue())
	        {
	            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)), 2);
	            worldIn.addBlockEvent(pos, this, 1, enumfacing.getHorizontalIndex());
	        }
	    }

	    private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing)
	    {
	        EnumFacing[] aenumfacing = EnumFacing.values();
	        int i = aenumfacing.length;
	        int j;

	        for (j = 0; j < i; ++j)
	        {
	            EnumFacing enumfacing1 = aenumfacing[j];

	            if (enumfacing1 != facing && worldIn.isSidePowered(pos.offset(enumfacing1), enumfacing1))
	            {
	                return true;
	            }
	        }

	        if (worldIn.isSidePowered(pos, EnumFacing.NORTH))
	        {
	            return true;
	        }
	        else
	        {
	            BlockPos blockpos1 = pos.up();
	            EnumFacing[] aenumfacing1 = EnumFacing.values();
	            j = aenumfacing1.length;

	            for (int k = 0; k < j; ++k)
	            {
	                EnumFacing enumfacing2 = aenumfacing1[k];

	                if (enumfacing2 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos1.offset(enumfacing2), enumfacing2))
	                {
	                    return true;
	                }
	            }
	            blockpos1 = pos.down();
	            for (int k = 0; k < j; ++k)
	            {
	                EnumFacing enumfacing2 = aenumfacing1[k];

	                if (enumfacing2 != EnumFacing.UP && worldIn.isSidePowered(blockpos1.offset(enumfacing2), enumfacing2))
	                {
	                    return true;
	                }
	            }

	            return false;
	        }
	    }

	    /**
	     * Called on both Client and Server when World#addBlockEvent is called
	     */
	    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
	    {
	        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

	        if (!worldIn.isRemote)
	        {
	            boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);

	            if (flag && eventID == 1)
	            {
	                worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)).withProperty(POWERED, Boolean.valueOf(true)), 2);
	                return false;
	            }

	            if (!flag && eventID == 0)
	            {
	                return false;
	            }
	        }

	        if (eventID == 0)
	        {
	            if (!this.doMove(worldIn, pos, enumfacing, true))
	            {
	                return false;
	            }

	            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)).withProperty(POWERED, Boolean.valueOf(true)), 2);
	            worldIn.playSound(null, pos,SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
	        }
	        else if (eventID == 1)
	        {
	            TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));

	            if (tileentity1 instanceof TileEntityPFDoor)
	            {
	                ((TileEntityPFDoor)tileentity1).clearPistonTileEntity();
	            }

	            worldIn.setBlockState(pos, RPBlocks.platform_door_extension.getDefaultState().withProperty(Moving.FACING, enumfacing).withProperty(LEFTY, state.getValue(LEFTY)), 3);
	            worldIn.setTileEntity(pos, Moving.newTileEntity(this.getStateFromMeta(eventParam | (state.getValue(LEFTY) ? 4 : 0)), enumfacing, false, true));
	            worldIn.setBlockToAir(pos.offset(enumfacing));
	            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
	        }

	        return true;
	    }

	    protected static final AxisAlignedBB PISTON_BASE_EW_LEFT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8D, 1.0D, 1.0D, 1.0D);
	    protected static final AxisAlignedBB PISTON_BASE_EW_RIGHT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.2D);
	    protected static final AxisAlignedBB PISTON_BASE_NS_LEFT_AABB = new AxisAlignedBB(0.8D, 0.0D, 0.0D, 1D, 1.0D, 1.0D);
	    protected static final AxisAlignedBB PISTON_BASE_NS_RIGHT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, .2D, 1.0D, 1.0D);
	    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	    {
	        /*if (((Boolean)state.getValue(EXTENDED)).booleanValue())
	        {*/
	            switch ((EnumFacing)state.getValue(FACING))
	            {
	                default:
	                case NORTH:
	                	if(state.getValue(LEFTY))
	                		return PISTON_BASE_NS_LEFT_AABB;
	                    return PISTON_BASE_NS_RIGHT_AABB;
	                case SOUTH:
	                	if(!state.getValue(LEFTY))
	                		return PISTON_BASE_NS_LEFT_AABB;
	                    return PISTON_BASE_NS_RIGHT_AABB;
	                case WEST:
	                	if(!state.getValue(LEFTY))
	                		return PISTON_BASE_EW_LEFT_AABB;
	                    return PISTON_BASE_EW_RIGHT_AABB;
	                case EAST:
	                	if(state.getValue(LEFTY))
	                		return PISTON_BASE_EW_LEFT_AABB;
	                    return PISTON_BASE_EW_RIGHT_AABB;
	            }
	        /*}
	        else
	        {
	            return FULL_BLOCK_AABB;
	        }*/
	    }

	    /**
	     * Add all collision boxes of this Block to the list that intersect with the given mask.
	     *  
	     * @param collidingEntity the Entity colliding with this Block
	     */
	    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, Entity p_185477_6_)
	    {
	        addCollisionBoxToList(pos, p_185477_4_, p_185477_5_, state.getBoundingBox(worldIn, pos));
	    }

	    public boolean isFullCube()
	    {
	        return false;
	    }

	    public static EnumFacing getFacing(int meta)
	    {
	        int j = meta & 3;
	        return j > 3 ? null : EnumFacing.getHorizontal(j);
	    }

    public static boolean func_185646_a(IBlockState p_185646_0_, World worldIn, BlockPos pos, EnumFacing facing, boolean p_185646_4_)
    {
        Block block = p_185646_0_.getBlock();

        if (block == Blocks.OBSIDIAN)
        {
            return false;
        }
        else if (!worldIn.getWorldBorder().contains(pos))
        {
            return false;
        }
        else if (pos.getY() >= 0 && (facing != EnumFacing.DOWN || pos.getY() != 0))
        {
            if (pos.getY() <= worldIn.getHeight() - 1 && (facing != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1))
            {
                if (block != RPBlocks.platform_door_base)
                {
                    if (p_185646_0_.getBlockHardness(worldIn, pos) == -1.0F)
                    {
                        return false;
                    }

                    if (p_185646_0_.getMobilityFlag() == EnumPushReaction.BLOCK)
                    {
                        return false;
                    }

                    if (p_185646_0_.getMobilityFlag() == EnumPushReaction.DESTROY)
                    {
                        return p_185646_4_;
                    }
                }
                else if (((Boolean)p_185646_0_.getValue(EXTENDED)).booleanValue())
                {
                    return false;
                }

                return !block.hasTileEntity(p_185646_0_);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

	    private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending)
	    {
	        if (!extending)
	        {
	            worldIn.setBlockToAir(pos.offset(direction));
	        }
	        IBlockState thisstate = worldIn.getBlockState(pos);
	        DoorStructureHelper blockpistonstructurehelper = new DoorStructureHelper(worldIn, pos, direction, extending);
	        List list = blockpistonstructurehelper.getBlocksToMove();
	        List list1 = blockpistonstructurehelper.getBlocksToDestroy();

	        if (!blockpistonstructurehelper.canMove())
	        {
	            return false;
	        }
	        else
	        {
	            int i = list.size() + list1.size();
	            Block[] ablock = new Block[i];
	            EnumFacing enumfacing1 = extending ? direction : direction.getOpposite();
	            int j;
	            BlockPos blockpos1;

	            for (j = list1.size() - 1; j >= 0; --j)
	            {
	                blockpos1 = (BlockPos)list1.get(j);
	                Block block = worldIn.getBlockState(blockpos1).getBlock();
	                //With our change to how snowballs are dropped this needs to disallow to mimic vanilla behavior.
	                float chance = block instanceof BlockSnow ? -1.0f : 1.0f;
	                block.dropBlockAsItemWithChance(worldIn, blockpos1, worldIn.getBlockState(blockpos1), chance, 0);
	                worldIn.setBlockToAir(blockpos1);
	                --i;
	                ablock[i] = block;
	            }

	            IBlockState iblockstate;

	            for (j = list.size() - 1; j >= 0; --j)
	            {
	                blockpos1 = (BlockPos)list.get(j);
	                iblockstate = worldIn.getBlockState(blockpos1);
	                Block block1 = iblockstate.getBlock();
	                block1.getMetaFromState(iblockstate);
	                worldIn.setBlockToAir(blockpos1);
	                blockpos1 = blockpos1.offset(enumfacing1);
	                worldIn.setBlockState(blockpos1, RPBlocks.platform_door_extension.getDefaultState().withProperty(FACING, direction).withProperty(LEFTY, thisstate.getValue(LEFTY)), 4);
	                worldIn.setTileEntity(blockpos1, Moving.newTileEntity(iblockstate, direction, extending, false));
	                --i;
	                ablock[i] = block1;
	            }

	            BlockPos blockpos2 = pos.offset(direction);

	            if (extending)
	            {
	                iblockstate = RPBlocks.platform_door_head.getDefaultState().withProperty(Extension.FACING, direction).withProperty(LEFTY, thisstate.getValue(LEFTY));
	                IBlockState iblockstate1 = RPBlocks.platform_door_extension.getDefaultState().withProperty(Moving.FACING, direction).withProperty(LEFTY, thisstate.getValue(LEFTY));
	                worldIn.setBlockState(blockpos2, iblockstate1, 4);
	                worldIn.setTileEntity(blockpos2, Moving.newTileEntity(iblockstate, direction, true, false));
	            }

	            int k;

	            for (k = list1.size() - 1; k >= 0; --k)
	            {
	                worldIn.notifyNeighborsOfStateChange((BlockPos)list1.get(k), ablock[i++]);
	            }

	            for (k = list.size() - 1; k >= 0; --k)
	            {
	                worldIn.notifyNeighborsOfStateChange((BlockPos)list.get(k), ablock[i++]);
	            }

	            if (extending)
	            {
	                worldIn.notifyNeighborsOfStateChange(blockpos2, RPBlocks.platform_door_head);
	                worldIn.notifyNeighborsOfStateChange(pos, this);
	            }

	            return true;
	        }
	    }

	    /**
	     * Convert the given metadata into a BlockState for this Block
	     */
	    public IBlockState getStateFromMeta(int meta)
	    {
	        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(LEFTY, Boolean.valueOf((meta & 4) > 0)).withProperty(EXTENDED, Boolean.valueOf((meta & 8) > 0)).withProperty(POWERED, Boolean.valueOf((meta & 16) > 0));
	    }

	    /**
	     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
	     */
	    @SideOnly(Side.CLIENT)
	    public IBlockState getStateForEntityRender(IBlockState state)
	    {
	        return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEFTY, false);
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        byte b0 = 0;
	        int i = b0 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	        
	        if(((Boolean)state.getValue(LEFTY)).booleanValue()) {
	        	i |= 4;
	        }
	        if (((Boolean)state.getValue(EXTENDED)).booleanValue())
	        {
	            i |= 8;
	        }
	        if(((Boolean)state.getValue(POWERED)).booleanValue()){
	        	i |= 16;
	        }

	        return i;
	    }

	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {FACING, EXTENDED, POWERED, LEFTY});
	    }
	    public boolean isFullCube(IBlockState state)
	    {
	        return false;
	    }

	    /**
	     * Used to determine ambient occlusion and culling when rebuilding chunks for render
	     */
	    public boolean isOpaqueCube(IBlockState state)
	    {
	        return false;
	    }
	}

	public static class Extension extends Block {

	    public static final PropertyDirection FACING = BlockHorizontal.FACING;
	    public static final PropertyBool SHORT = PropertyBool.create("short");

	    public Extension()
	    {
	        super(Material.GLASS);
	        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(Base.LEFTY, false).withProperty(SHORT, Boolean.valueOf(false)));
	        this.setSoundType(SoundType.STONE);
	        this.setHardness(0.5F);
	    }
	    public boolean isFullCube(IBlockState state)
	    {
	        return false;
	    }

	    /**
	     * Used to determine ambient occlusion and culling when rebuilding chunks for render
	     */
	    public boolean isOpaqueCube(IBlockState state)
	    {
	        return false;
	    }
	    @SideOnly(Side.CLIENT)
	    public BlockRenderLayer getBlockLayer()
	    {
	        return BlockRenderLayer.CUTOUT_MIPPED;
	    }
	    @SideOnly(Side.CLIENT)
	    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	    {
	        return blockState.getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	    }
	    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	    {
	        if (player.capabilities.isCreativeMode)
	        {
	            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

	            if (enumfacing != null)
	            {
	                BlockPos blockpos1 = pos.offset(enumfacing.getOpposite());
	                Block block = worldIn.getBlockState(blockpos1).getBlock();

	                if (block == RPBlocks.platform_door_base)
	                {
	                    worldIn.setBlockToAir(blockpos1);
	                }
	            }
	        }

	        super.onBlockHarvested(worldIn, pos, state, player);
	    }

	    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	    {
	        super.breakBlock(worldIn, pos, state);
	        EnumFacing enumfacing = ((EnumFacing)state.getValue(FACING)).getOpposite();
	        pos = pos.offset(enumfacing);
	        IBlockState iblockstate1 = worldIn.getBlockState(pos);

	        if ((iblockstate1.getBlock() == RPBlocks.platform_door_base) && ((Boolean)iblockstate1.getValue(Base.EXTENDED)).booleanValue())
	        {
	            iblockstate1.getBlock().dropBlockAsItem(worldIn, pos, iblockstate1, 0);
	            worldIn.setBlockToAir(pos);
	        }
	    }

	    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	    {
	        return false;
	    }

	    /**
	     * Check whether this Block can be placed on the given side
	     */
	    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	    {
	        return false;
	    }

	    /**
	     * Returns the quantity of items to drop on block destruction.
	     */
	    public int quantityDropped(Random random)
	    {
	        return 0;
	    }

	    protected static final AxisAlignedBB PISTON_EXTENSION_EW_LEFT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9D, 1.0D, 1.0D, 1D);
	    protected static final AxisAlignedBB PISTON_EXTENSION_NS_LEFT_AABB = new AxisAlignedBB(0.9D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	    protected static final AxisAlignedBB PISTON_EXTENSION_EW_RIGHT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1D);
	    protected static final AxisAlignedBB PISTON_EXTENSION_NS_RIGHT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1D, 1.0D, 1.0D);
	    
	    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	    {
	        switch ((EnumFacing)state.getValue(FACING))
	        {
	            default:
	            case NORTH:
                	if(state.getValue(Base.LEFTY))
                		return PISTON_EXTENSION_NS_LEFT_AABB;
	                return PISTON_EXTENSION_NS_RIGHT_AABB;
	            case SOUTH:
                	if(!state.getValue(Base.LEFTY))
                		return PISTON_EXTENSION_NS_LEFT_AABB;
	                return PISTON_EXTENSION_NS_RIGHT_AABB;
	            case WEST:
                	if(!state.getValue(Base.LEFTY))
                		return PISTON_EXTENSION_EW_LEFT_AABB;
	                return PISTON_EXTENSION_EW_RIGHT_AABB;
	            case EAST:
                	if(state.getValue(Base.LEFTY))
                		return PISTON_EXTENSION_EW_LEFT_AABB;
	                return PISTON_EXTENSION_EW_RIGHT_AABB;
	        }
	    }

	    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, Entity p_185477_6_)
	    {
	        addCollisionBoxToList(pos, p_185477_4_, p_185477_5_, state.getBoundingBox(worldIn, pos));
	    }

	    /**
	     * Called when a neighboring block changes.
	     */
	    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	    {
	        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
	        BlockPos blockpos1 = pos.offset(enumfacing.getOpposite());
	        IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

	        if (iblockstate1.getBlock() != RPBlocks.platform_door_base)
	        {
	            worldIn.setBlockToAir(pos);
	        }
	        else
	        {
	            iblockstate1.getBlock().onNeighborBlockChange(worldIn, blockpos1, iblockstate1, neighborBlock);
	        }
	    }

	    public static EnumFacing getFacing(int meta)
	    {
	        int j = meta & 3;
	        return j > 3 ? null : EnumFacing.getHorizontal(j);
	    }

	    @SideOnly(Side.CLIENT)
	    public Item getItem(World worldIn, BlockPos pos)
	    {
	        return Item.getItemFromBlock(RPBlocks.platform_door_base);
	    }

	    /**
	     * Convert the given metadata into a BlockState for this Block
	     */
	    public IBlockState getStateFromMeta(int meta)
	    {
	        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(Base.LEFTY, Boolean.valueOf((meta & 4) > 0));
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        byte b0 = 0;
	        int i = b0 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	        
	        if(state.getValue(Base.LEFTY)){
	        	i |= 4;
	        }
	        return i;
	    }

	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {FACING, SHORT, Base.LEFTY});
	    }
	}

	public static class Moving extends BlockContainer {

	    public static final PropertyDirection FACING = Extension.FACING;

	    public Moving()
	    {
	        super(Material.GLASS);
	        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(Base.LEFTY, false));
	        this.setHardness(-1.0F);
	    }
	    public boolean isFullCube(IBlockState state)
	    {
	        return false;
	    }

	    /**
	     * Used to determine ambient occlusion and culling when rebuilding chunks for render
	     */
	    public boolean isOpaqueCube(IBlockState state)
	    {
	        return false;
	    }
	    @SideOnly(Side.CLIENT)
	    public BlockRenderLayer getBlockLayer()
	    {
	        return BlockRenderLayer.CUTOUT_MIPPED;
	    }
	    @SideOnly(Side.CLIENT)
	    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	    {
	        return blockAccess.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	    }
	    /**
	     * Returns a new instance of a block's tile entity class. Called on placing the block.
	     */
	    public TileEntity createNewTileEntity(World worldIn, int meta)
	    {
	        return null;
	    }

	    public static TileEntity newTileEntity(IBlockState state, EnumFacing facing, boolean extending, boolean renderHead)
	    {
	        return new TileEntityPFDoor(state, facing, extending, renderHead);
	    }

	    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	    {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof TileEntityPFDoor)
	        {
	            ((TileEntityPFDoor)tileentity).clearPistonTileEntity();
	        }
	        else
	        {
	            super.breakBlock(worldIn, pos, state);
	        }
	    }

	    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	    {
	        return false;
	    }

	    /**
	     * Check whether this Block can be placed on the given side
	     */
	    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	    {
	        return false;
	    }

	    /**
	     * Called when a player destroys this Block
	     */
	    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	    {
	        BlockPos blockpos1 = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());
	        IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

	        if (iblockstate1.getBlock() instanceof Base && ((Boolean)iblockstate1.getValue(Base.EXTENDED)).booleanValue())
	        {
	            worldIn.setBlockToAir(blockpos1);
	        }
	    }

	    public boolean isOpaqueCube()
	    {
	        return false;
	    }

	    public boolean isFullCube()
	    {
	        return false;
	    }

	    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	    {
	        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
	        {
	            worldIn.setBlockToAir(pos);
	            return true;
	        }
	        else
	        {
	            return false;
	        }
	    }

	    /**
	     * Get the Item that this Block should drop when harvested.
	     *  
	     * @param fortune the level of the Fortune enchantment on the player's tool
	     */
	    public Item getItemDropped(IBlockState state, Random rand, int fortune)
	    {
	        return null;
	    }

	    /**
	     * Spawns this Block's drops into the World as EntityItems.
	     *  
	     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
	     * @param fortune The player's fortune level
	     */
	    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	    {
	        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
	    }

	    /**
	     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
	     *  
	     * @param start The start vector
	     * @param end The end vector
	     */
	    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	    {
	        return null;
	    }

	    /**
	     * Called when a neighboring block changes.
	     */
	    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	    {
	        if (!worldIn.isRemote)
	        {
	            worldIn.getTileEntity(pos);
	        }
	    }

    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntityPFDoor tileentitypiston = this.getTileEntity(worldIn, pos);
        return tileentitypiston == null ? null : tileentitypiston.func_184321_a(worldIn, pos);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TileEntityPFDoor tileentitypiston = this.getTileEntity(source, pos);
        return tileentitypiston != null ? tileentitypiston.func_184321_a(source, pos) : FULL_BLOCK_AABB;
    }
    
	    private TileEntityPFDoor getTileEntity(IBlockAccess worldIn, BlockPos pos)
	    {
	        TileEntity tileentity = worldIn.getTileEntity(pos);
	        return tileentity instanceof TileEntityPFDoor ? (TileEntityPFDoor)tileentity : null;
	    }

	    @SideOnly(Side.CLIENT)
	    public Item getItem(World worldIn, BlockPos pos)
	    {
	        return null;
	    }

	    /**
	     * Convert the given metadata into a BlockState for this Block
	     */
	    public IBlockState getStateFromMeta(int meta)
	    {
	        return this.getDefaultState().withProperty(FACING, Extension.getFacing(meta)).withProperty(Base.LEFTY, Boolean.valueOf((meta & 4) > 0));
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        byte b0 = 0;
	        int i = b0 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	        
	        if(state.getValue(Base.LEFTY)){
	        	i |= 4;
	        }
	        return i;
	    }

	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {FACING, Base.LEFTY});
	    }

	    @Override
	    public java.util.List<net.minecraft.item.ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	    {
	    	TileEntityPFDoor tileentitypiston = this.getTileEntity(world, pos);
	        if (tileentitypiston != null)
	        {
	            IBlockState pushed = tileentitypiston.getPistonState();
	            return pushed.getBlock().getDrops(world, pos, pushed, fortune);
	        }
	        return new java.util.ArrayList();
	    }
	}
}
