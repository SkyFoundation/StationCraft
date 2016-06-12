package io.github.cvronmin.railwayp.tileentity;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import io.github.cvronmin.railwayp.init.RPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
//import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
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

    private void func_145863_a(float p_145863_1_, float p_145863_2_)
    {
        if (this.extending)
        {
            p_145863_1_ = 1.0F - p_145863_1_;
        }
        else
        {
            --p_145863_1_;
        }

        AxisAlignedBB axisalignedbb = RPBlocks.platform_door_extension.getBoundingBox(this.worldObj, this.pos, this.pistonState, p_145863_1_, this.pistonFacing);

        if (axisalignedbb != null)
        {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);

            if (!list.isEmpty())
            {
                this.field_174933_k.addAll(list);
                Iterator iterator = this.field_174933_k.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();
                    entity.moveEntity((double)(p_145863_2_ * (float)this.pistonFacing.getFrontOffsetX()), (double)(p_145863_2_ * (float)this.pistonFacing.getFrontOffsetY()), (double)(p_145863_2_ * (float)this.pistonFacing.getFrontOffsetZ()));
                }

                this.field_174933_k.clear();
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
        if (this.lastProgress < 1.0F && this.worldObj != null)
        {
            this.lastProgress = this.progress = 1.0F;
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();

            if (this.worldObj.getBlockState(this.pos).getBlock() == RPBlocks.platform_door_extension)
            {
                this.worldObj.setBlockState(this.pos, this.pistonState, 3);
                if(!net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldObj, pos, worldObj.getBlockState(pos), java.util.EnumSet.noneOf(EnumFacing.class)).isCanceled())
                    this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
            }
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
            this.func_145863_a(1.0F, 0.25F);
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();

            if (this.worldObj.getBlockState(this.pos).getBlock() == RPBlocks.platform_door_extension)
            {
                this.worldObj.setBlockState(this.pos, this.pistonState, 3);
                if(!net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldObj, pos, worldObj.getBlockState(pos), java.util.EnumSet.noneOf(EnumFacing.class)).isCanceled())
                    this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
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
                this.func_145863_a(this.progress, this.progress - this.lastProgress + 0.0625F);
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

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        compound.setInteger("facing", this.pistonFacing.getIndex());
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
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