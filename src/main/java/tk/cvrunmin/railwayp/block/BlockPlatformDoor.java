package tk.cvrunmin.railwayp.block;

import java.util.List;
import java.util.Random;

import tk.cvrunmin.railwayp.init.RPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlatformDoor {

	public static class Base extends Block {
	    public static final PropertyDirection FACING = PropertyDirection.create("facing");
	    public static final PropertyBool EXTENDED = PropertyBool.create("extended");
	    /** This piston is the sticky one? */
	    private final boolean isSticky;

	    public Base(boolean isSticky)
	    {
	        super(Material.piston);
	        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, Boolean.valueOf(false)));
	        this.isSticky = isSticky;
	        this.setStepSound(soundTypePiston);
	        this.setHardness(0.5F);
	        this.setCreativeTab(CreativeTabs.tabRedstone);
	    }

	    public boolean isOpaqueCube()
	    {
	        return false;
	    }

	    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	    {
	        worldIn.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)), 2);

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
	        return this.getDefaultState().withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)).withProperty(EXTENDED, Boolean.valueOf(false));
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
	            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(false)), 2);
	            worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
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
	                worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
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

	            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
	            worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "tile.piston.out", 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
	        }
	        else if (eventID == 1)
	        {
	            TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));

	            if (tileentity1 instanceof TileEntityPiston)
	            {
	                ((TileEntityPiston)tileentity1).clearPistonTileEntity();
	            }

	            worldIn.setBlockState(pos, RPBlocks.platform_door_extension.getDefaultState().withProperty(Moving.FACING, enumfacing).withProperty(Moving.TYPE, this.isSticky ? Extension.EnumPistonType.STICKY : Extension.EnumPistonType.DEFAULT), 3);
	            worldIn.setTileEntity(pos, Moving.newTileEntity(this.getStateFromMeta(eventParam), enumfacing, false, true));

	            if (this.isSticky)
	            {
	                BlockPos blockpos1 = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
	                Block block = worldIn.getBlockState(blockpos1).getBlock();
	                boolean flag1 = false;

	                if (block == RPBlocks.platform_door_extension)
	                {
	                    TileEntity tileentity = worldIn.getTileEntity(blockpos1);

	                    if (tileentity instanceof TileEntityPiston)
	                    {
	                        TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity;

	                        if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending())
	                        {
	                            tileentitypiston.clearPistonTileEntity();
	                            flag1 = true;
	                        }
	                    }
	                }

	                if (!flag1 && !block.isAir(worldIn, blockpos1) && canPush(block, worldIn, blockpos1, enumfacing.getOpposite(), false) && (block.getMobilityFlag() == 0 || block == RPBlocks.platform_door_base))
	                {
	                    this.doMove(worldIn, pos, enumfacing, false);
	                }
	            }
	            else
	            {
	                worldIn.setBlockToAir(pos.offset(enumfacing));
	            }

	            worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "tile.piston.in", 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
	        }

	        return true;
	    }

	    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	    {
	        IBlockState iblockstate = worldIn.getBlockState(pos);

	        if (iblockstate.getBlock() == this && ((Boolean)iblockstate.getValue(EXTENDED)).booleanValue())
	        {
	            float f = 0.25F;
	            EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);

	            if (enumfacing != null)
	            {
	                switch (Base.SwitchEnumFacing.FACING_LOOKUP[enumfacing.ordinal()])
	                {
	                    case 1:
	                        this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
	                        break;
	                    case 2:
	                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
	                        break;
	                    case 3:
	                        this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	                        break;
	                    case 4:
	                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
	                }
	            }
	        }
	        else
	        {
	            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	        }
	    }

	    /**
	     * Sets the block's bounds for rendering it as an item
	     */
	    public void setBlockBoundsForItemRender()
	    {
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	    }

	    /**
	     * Add all collision boxes of this Block to the list that intersect with the given mask.
	     *  
	     * @param collidingEntity the Entity colliding with this Block
	     */
	    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	    {
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	    }

	    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	    {
	        this.setBlockBoundsBasedOnState(worldIn, pos);
	        return super.getCollisionBoundingBox(worldIn, pos, state);
	    }

	    public boolean isFullCube()
	    {
	        return false;
	    }

	    public static EnumFacing getFacing(int meta)
	    {
	        int j = meta & 7;
	        return j > 5 ? null : EnumFacing.getFront(j);
	    }

	    public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
	    {
	        if (MathHelper.abs((float)entityIn.posX - (float)clickedBlock.getX()) < 2.0F && MathHelper.abs((float)entityIn.posZ - (float)clickedBlock.getZ()) < 2.0F)
	        {
	            double d0 = entityIn.posY + (double)entityIn.getEyeHeight();

	            if (d0 - (double)clickedBlock.getY() > 2.0D)
	            {
	                return EnumFacing.UP;
	            }

	            if ((double)clickedBlock.getY() - d0 > 0.0D)
	            {
	                return EnumFacing.DOWN;
	            }
	        }

	        return entityIn.getHorizontalFacing().getOpposite();
	    }

	    public static boolean canPush(Block blockIn, World worldIn, BlockPos pos, EnumFacing direction, boolean allowDestroy)
	    {
	        if (blockIn == Blocks.obsidian)
	        {
	            return false;
	        }
	        else if (!worldIn.getWorldBorder().contains(pos))
	        {
	            return false;
	        }
	        else if (pos.getY() >= 0 && (direction != EnumFacing.DOWN || pos.getY() != 0))
	        {
	            if (pos.getY() <= worldIn.getHeight() - 1 && (direction != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1))
	            {
	                if (blockIn != RPBlocks.platform_door_base)
	                {
	                    if (blockIn.getBlockHardness(worldIn, pos) == -1.0F)
	                    {
	                        return false;
	                    }

	                    if (blockIn.getMobilityFlag() == 2)
	                    {
	                        return false;
	                    }

	                    if (blockIn.getMobilityFlag() == 1)
	                    {
	                        if (!allowDestroy)
	                        {
	                            return false;
	                        }

	                        return true;
	                    }
	                }
	                else if (((Boolean)worldIn.getBlockState(pos).getValue(EXTENDED)).booleanValue())
	                {
	                    return false;
	                }

	                return !(blockIn.hasTileEntity(worldIn.getBlockState(pos)));
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
	                worldIn.setBlockState(blockpos1, RPBlocks.platform_door_extension.getDefaultState().withProperty(FACING, direction), 4);
	                worldIn.setTileEntity(blockpos1, Moving.newTileEntity(iblockstate, direction, extending, false));
	                --i;
	                ablock[i] = block1;
	            }

	            BlockPos blockpos2 = pos.offset(direction);

	            if (extending)
	            {
	                Extension.EnumPistonType enumpistontype = this.isSticky ? Extension.EnumPistonType.STICKY : Extension.EnumPistonType.DEFAULT;
	                iblockstate = RPBlocks.platform_door_head.getDefaultState().withProperty(Extension.FACING, direction).withProperty(Extension.TYPE, enumpistontype);
	                IBlockState iblockstate1 = RPBlocks.platform_door_extension.getDefaultState().withProperty(Moving.FACING, direction).withProperty(Moving.TYPE, this.isSticky ? Extension.EnumPistonType.STICKY : Extension.EnumPistonType.DEFAULT);
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
	        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(EXTENDED, Boolean.valueOf((meta & 8) > 0));
	    }

	    /**
	     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
	     */
	    @SideOnly(Side.CLIENT)
	    public IBlockState getStateForEntityRender(IBlockState state)
	    {
	        return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        byte b0 = 0;
	        int i = b0 | ((EnumFacing)state.getValue(FACING)).getIndex();

	        if (((Boolean)state.getValue(EXTENDED)).booleanValue())
	        {
	            i |= 8;
	        }

	        return i;
	    }

	    protected BlockState createBlockState()
	    {
	        return new BlockState(this, new IProperty[] {FACING, EXTENDED});
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

	public static class Extension extends Block {

	    public static final PropertyDirection FACING = PropertyDirection.create("facing");
	    public static final PropertyEnum TYPE = PropertyEnum.create("type", Extension.EnumPistonType.class);
	    public static final PropertyBool SHORT = PropertyBool.create("short");

	    public Extension()
	    {
	        super(Material.piston);
	        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, Extension.EnumPistonType.DEFAULT).withProperty(SHORT, Boolean.valueOf(false)));
	        this.setStepSound(soundTypePiston);
	        this.setHardness(0.5F);
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

	    public boolean isOpaqueCube()
	    {
	        return false;
	    }

	    public boolean isFullCube()
	    {
	        return false;
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

	    /**
	     * Add all collision boxes of this Block to the list that intersect with the given mask.
	     *  
	     * @param collidingEntity the Entity colliding with this Block
	     */
	    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	    {
	        this.applyHeadBounds(state);
	        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	        this.applyCoreBounds(state);
	        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	    }

	    private void applyCoreBounds(IBlockState state)
	    {
	        float f = 0.25F;
	        float f1 = 0.375F;
	        float f2 = 0.625F;
	        float f3 = 0.25F;
	        float f4 = 0.75F;

	        switch (Extension.SwitchEnumFacing.FACING_LOOKUP[((EnumFacing)state.getValue(FACING)).ordinal()])
	        {
	            case 1:
	                this.setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
	                break;
	            case 2:
	                this.setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
	                break;
	            case 3:
	                this.setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
	                break;
	            case 4:
	                this.setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
	        }
	    }

	    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	    {
	        this.applyHeadBounds(worldIn.getBlockState(pos));
	    }

	    public void applyHeadBounds(IBlockState state)
	    {
	        float f = 0.25F;
	        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

	        if (enumfacing != null)
	        {
	            switch (Extension.SwitchEnumFacing.FACING_LOOKUP[enumfacing.ordinal()])
	            {
	                case 1:
	                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
	                    break;
	                case 2:
	                    this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
	                    break;
	                case 3:
	                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
	                    break;
	                case 4:
	                    this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	            }
	        }
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

	    @SideOnly(Side.CLIENT)
	    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	    {
	        return true;
	    }

	    public static EnumFacing getFacing(int meta)
	    {
	        int j = meta & 7;
	        return j > 5 ? null : EnumFacing.getFront(j);
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
	        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(TYPE, (meta & 8) > 0 ? Extension.EnumPistonType.STICKY : Extension.EnumPistonType.DEFAULT);
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        byte b0 = 0;
	        int i = b0 | ((EnumFacing)state.getValue(FACING)).getIndex();

	        if (state.getValue(TYPE) == Extension.EnumPistonType.STICKY)
	        {
	            i |= 8;
	        }

	        return i;
	    }

	    protected BlockState createBlockState()
	    {
	        return new BlockState(this, new IProperty[] {FACING, TYPE, SHORT});
	    }

	    public static enum EnumPistonType implements IStringSerializable
	    {
	        DEFAULT("normal"),
	        STICKY("sticky");
	        private final String VARIANT;


	        private EnumPistonType(String name)
	        {
	            this.VARIANT = name;
	        }

	        public String toString()
	        {
	            return this.VARIANT;
	        }

	        public String getName()
	        {
	            return this.VARIANT;
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

	public static class Moving extends BlockContainer {

	    public static final PropertyDirection FACING = Extension.FACING;
	    public static final PropertyEnum TYPE = Extension.TYPE;

	    public Moving()
	    {
	        super(Material.piston);
	        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, Extension.EnumPistonType.DEFAULT));
	        this.setHardness(-1.0F);
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
	        return new TileEntityPiston(state, facing, extending, renderHead);
	    }

	    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	    {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof TileEntityPiston)
	        {
	            ((TileEntityPiston)tileentity).clearPistonTileEntity();
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
	    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
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

	    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	    {
	        TileEntityPiston tileentitypiston = this.getTileEntity(worldIn, pos);

	        if (tileentitypiston == null)
	        {
	            return null;
	        }
	        else
	        {
	            float f = tileentitypiston.func_145860_a(0.0F);

	            if (tileentitypiston.isExtending())
	            {
	                f = 1.0F - f;
	            }

	            return this.getBoundingBox(worldIn, pos, tileentitypiston.getPistonState(), f, tileentitypiston.getFacing());
	        }
	    }

	    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	    {
	        TileEntityPiston tileentitypiston = this.getTileEntity(worldIn, pos);

	        if (tileentitypiston != null)
	        {
	            IBlockState iblockstate = tileentitypiston.getPistonState();
	            Block block = iblockstate.getBlock();

	            if (block == this || block.getMaterial() == Material.air)
	            {
	                return;
	            }

	            float f = tileentitypiston.func_145860_a(0.0F);

	            if (tileentitypiston.isExtending())
	            {
	                f = 1.0F - f;
	            }

	            block.setBlockBoundsBasedOnState(worldIn, pos);

	            if (block == RPBlocks.platform_door_base)
	            {
	                f = 0.0F;
	            }

	            EnumFacing enumfacing = tileentitypiston.getFacing();
	            this.minX = block.getBlockBoundsMinX() - (double)((float)enumfacing.getFrontOffsetX() * f);
	            this.minY = block.getBlockBoundsMinY() - (double)((float)enumfacing.getFrontOffsetY() * f);
	            this.minZ = block.getBlockBoundsMinZ() - (double)((float)enumfacing.getFrontOffsetZ() * f);
	            this.maxX = block.getBlockBoundsMaxX() - (double)((float)enumfacing.getFrontOffsetX() * f);
	            this.maxY = block.getBlockBoundsMaxY() - (double)((float)enumfacing.getFrontOffsetY() * f);
	            this.maxZ = block.getBlockBoundsMaxZ() - (double)((float)enumfacing.getFrontOffsetZ() * f);
	        }
	    }

	    public AxisAlignedBB getBoundingBox(World worldIn, BlockPos pos, IBlockState extendingBlock, float progress, EnumFacing direction)
	    {
	        if (extendingBlock.getBlock() != this && extendingBlock.getBlock().getMaterial() != Material.air)
	        {
	            AxisAlignedBB axisalignedbb = extendingBlock.getBlock().getCollisionBoundingBox(worldIn, pos, extendingBlock);

	            if (axisalignedbb == null)
	            {
	                return null;
	            }
	            else
	            {
	                double d0 = axisalignedbb.minX;
	                double d1 = axisalignedbb.minY;
	                double d2 = axisalignedbb.minZ;
	                double d3 = axisalignedbb.maxX;
	                double d4 = axisalignedbb.maxY;
	                double d5 = axisalignedbb.maxZ;

	                if (direction.getFrontOffsetX() < 0)
	                {
	                    d0 -= (double)((float)direction.getFrontOffsetX() * progress);
	                }
	                else
	                {
	                    d3 -= (double)((float)direction.getFrontOffsetX() * progress);
	                }

	                if (direction.getFrontOffsetY() < 0)
	                {
	                    d1 -= (double)((float)direction.getFrontOffsetY() * progress);
	                }
	                else
	                {
	                    d4 -= (double)((float)direction.getFrontOffsetY() * progress);
	                }

	                if (direction.getFrontOffsetZ() < 0)
	                {
	                    d2 -= (double)((float)direction.getFrontOffsetZ() * progress);
	                }
	                else
	                {
	                    d5 -= (double)((float)direction.getFrontOffsetZ() * progress);
	                }

	                return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	            }
	        }
	        else
	        {
	            return null;
	        }
	    }

	    private TileEntityPiston getTileEntity(IBlockAccess worldIn, BlockPos pos)
	    {
	        TileEntity tileentity = worldIn.getTileEntity(pos);
	        return tileentity instanceof TileEntityPiston ? (TileEntityPiston)tileentity : null;
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
	        return this.getDefaultState().withProperty(FACING, Extension.getFacing(meta)).withProperty(TYPE, (meta & 8) > 0 ? Extension.EnumPistonType.STICKY : Extension.EnumPistonType.DEFAULT);
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        byte b0 = 0;
	        int i = b0 | ((EnumFacing)state.getValue(FACING)).getIndex();

	        if (state.getValue(TYPE) == Extension.EnumPistonType.STICKY)
	        {
	            i |= 8;
	        }

	        return i;
	    }

	    protected BlockState createBlockState()
	    {
	        return new BlockState(this, new IProperty[] {FACING, TYPE});
	    }

	    @Override
	    public java.util.List<net.minecraft.item.ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	    {
	        TileEntityPiston tileentitypiston = this.getTileEntity(world, pos);
	        if (tileentitypiston != null)
	        {
	            IBlockState pushed = tileentitypiston.getPistonState();
	            return pushed.getBlock().getDrops(world, pos, pushed, fortune);
	        }
	        return new java.util.ArrayList();
	    }
	}
}
