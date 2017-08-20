package io.github.cvronmin.railwayp.tileentity;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import io.github.cvronmin.railwayp.init.RPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
//import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPFDoor extends TileEntity implements ITickable
{
    private IBlockState pistonState;
    private EnumFacing pistonFacing;
    /** if this piston is extending or not */
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;
    /** the progress in (de)extending */
    private float lastProgress;
    private List field_174933_k = Lists.newArrayList();

    public TileEntityPFDoor() {}

    public TileEntityPFDoor(IBlockState pistonStateIn, EnumFacing pistonFacingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn)
    {
        this.pistonState = pistonStateIn;
        this.pistonFacing = pistonFacingIn;
        this.extending = extendingIn;
        this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
    }

    public IBlockState getPistonState()
    {
        return this.pistonState;
    }

    public int getBlockMetadata()
    {
        return 0;
    }

    /**
     * Returns true if a piston is extending
     */
    public boolean isExtending()
    {
        return this.extending;
    }

    public EnumFacing getFacing()
    {
        return this.pistonFacing;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldPistonHeadBeRendered()
    {
        return this.shouldHeadBeRendered;
    }

    public float getProgress(float p_145860_1_)
    {
        if (p_145860_1_ > 1.0F)
        {
            p_145860_1_ = 1.0F;
        }

        return this.lastProgress + (this.progress - this.lastProgress) * p_145860_1_;
    }
    private float getExtendingProgress(float p_184320_1_)
    {
        return this.extending ? p_184320_1_ - 1.0F : 1.0F - p_184320_1_;
    }
    private void moveCollidedEntities()
    {
        AxisAlignedBB axisalignedbb = this.getAABB(this.world, this.pos).offset(this.pos);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);

        if (!list.isEmpty())
        {
            EnumFacing enumfacing = this.extending ? this.pistonFacing : this.pistonFacing.getOpposite();

            for (int i = 0; i < list.size(); ++i)
            {
                Entity entity = list.get(i);

                if (entity.getPushReaction() != EnumPushReaction.IGNORE)
                {
                    if (this.pistonState.getBlock() == Blocks.SLIME_BLOCK)
                    {
                        switch (enumfacing.getAxis())
                        {
                            case X:
                                entity.motionX = (double)enumfacing.getFrontOffsetX();
                                break;
                            case Y:
                                entity.motionY = (double)enumfacing.getFrontOffsetY();
                                break;
                            case Z:
                                entity.motionZ = (double)enumfacing.getFrontOffsetZ();
                        }
                    }

                    double d0 = 0.0D;
                    double d1 = 0.0D;
                    double d2 = 0.0D;
                    AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();

                    switch (enumfacing.getAxis())
                    {
                        case X:

                            if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                            {
                                d0 = axisalignedbb.maxX - axisalignedbb1.minX;
                            }
                            else
                            {
                                d0 = axisalignedbb1.maxX - axisalignedbb.minX;
                            }

                            d0 = d0 + 0.01D;
                            break;
                        case Y:

                            if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                            {
                                d1 = axisalignedbb.maxY - axisalignedbb1.minY;
                            }
                            else
                            {
                                d1 = axisalignedbb1.maxY - axisalignedbb.minY;
                            }

                            d1 = d1 + 0.01D;
                            break;
                        case Z:

                            if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                            {
                                d2 = axisalignedbb.maxZ - axisalignedbb1.minZ;
                            }
                            else
                            {
                                d2 = axisalignedbb1.maxZ - axisalignedbb.minZ;
                            }

                            d2 = d2 + 0.01D;
                    }

                    entity.move(MoverType.PISTON, d0 * (double)enumfacing.getFrontOffsetX(), d1 * (double)enumfacing.getFrontOffsetY(), d2 * (double)enumfacing.getFrontOffsetZ());
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public float getOffsetX(float p_174929_1_)
    {
        return this.extending ? (this.getProgress(p_174929_1_) - 1.0F) * (float)this.pistonFacing.getFrontOffsetX() : (1.0F - this.getProgress(p_174929_1_)) * (float)this.pistonFacing.getFrontOffsetX();
    }

    @SideOnly(Side.CLIENT)
    public float getOffsetY(float p_174928_1_)
    {
        return this.extending ? (this.getProgress(p_174928_1_) - 1.0F) * (float)this.pistonFacing.getFrontOffsetY() : (1.0F - this.getProgress(p_174928_1_)) * (float)this.pistonFacing.getFrontOffsetY();
    }

    @SideOnly(Side.CLIENT)
    public float getOffsetZ(float p_174926_1_)
    {
        return this.extending ? (this.getProgress(p_174926_1_) - 1.0F) * (float)this.pistonFacing.getFrontOffsetZ() : (1.0F - this.getProgress(p_174926_1_)) * (float)this.pistonFacing.getFrontOffsetZ();
    }

    /**
     * removes a piston's tile entity (and if the piston is moving, stops it)
     */
    public void clearPistonTileEntity()
    {
        if (this.lastProgress < 1.0F && this.world != null)
        {
            this.lastProgress = this.progress = 1.0F;
            this.world.removeTileEntity(this.pos);
            this.invalidate();

            if (this.world.getBlockState(this.pos).getBlock() == RPBlocks.platform_door_extension)
            {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                if(!net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(world, pos, world.getBlockState(pos), java.util.EnumSet.noneOf(EnumFacing.class), false).isCanceled())
                    this.world.notifyNeighborsOfStateChange(this.pos, this.pistonState.getBlock(), true);
            }
        }
    }
    public void setProgress(float progress, boolean extending){
    	if(this.extending ^ extending){
    		this.progress = 1 - progress;
    	}else {
			this.progress = progress;
		}
    }
    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        this.lastProgress = this.progress;

        if (this.lastProgress >= 1.0F)
        {
            this.moveCollidedEntities();
            this.world.removeTileEntity(this.pos);
            this.invalidate();

            if (this.world.getBlockState(this.pos).getBlock() == RPBlocks.platform_door_extension)
            {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                if(!net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(world, pos, world.getBlockState(pos), java.util.EnumSet.noneOf(EnumFacing.class), false).isCanceled())
                    this.world.notifyNeighborsOfStateChange(this.pos, this.pistonState.getBlock(),true);
            }
        }
        else
        {
            this.progress += 0.5F * 0.1f;

            if (this.progress >= 1.0F)
            {
                this.progress = 1.0F;
            }

            if (this.extending)
            {
                this.moveCollidedEntities();
            }
        }
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.pistonState = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
        this.pistonFacing = EnumFacing.getFront(compound.getInteger("facing"));
        this.lastProgress = this.progress = compound.getFloat("progress");
        this.extending = compound.getBoolean("extending");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        compound.setInteger("facing", this.pistonFacing.getIndex());
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
		return compound;
    }
    public AxisAlignedBB getAABB(IBlockAccess p_184321_1_, BlockPos p_184321_2_)
    {
        return this.getAABB(p_184321_1_, p_184321_2_, this.progress).union(this.getAABB(p_184321_1_, p_184321_2_, this.lastProgress));
    }

    public AxisAlignedBB getAABB(IBlockAccess p_184319_1_, BlockPos p_184319_2_, float p_184319_3_)
    {
        p_184319_3_ = this.getExtendingProgress(p_184319_3_);
        return this.pistonState.getBoundingBox(p_184319_1_, p_184319_2_).offset((double)(p_184319_3_ * (float)this.pistonFacing.getFrontOffsetX()), (double)(p_184319_3_ * (float)this.pistonFacing.getFrontOffsetY()), (double)(p_184319_3_ * (float)this.pistonFacing.getFrontOffsetZ()));
    }
    static final class SwitchAxis
        {
            static final int[] field_177248_a = new int[EnumFacing.Axis.values().length];

            static
            {
                try
                {
                    field_177248_a[EnumFacing.Axis.X.ordinal()] = 1;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_177248_a[EnumFacing.Axis.Y.ordinal()] = 2;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_177248_a[EnumFacing.Axis.Z.ordinal()] = 3;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}