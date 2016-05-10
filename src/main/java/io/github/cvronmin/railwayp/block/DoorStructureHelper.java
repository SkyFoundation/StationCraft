package io.github.cvronmin.railwayp.block;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoorStructureHelper {

    private final World world;
    private final BlockPos pistonPos;
    private final BlockPos blockToMove;
    private final EnumFacing moveDirection;
    /** This is a List<BlockPos> of all blocks that will be moved by the piston. */
    private final List toMove = Lists.newArrayList();
    /** This is a List<BlockPos> of blocks that will be destroyed when a piston attempts to move them. */
    private final List toDestroy = Lists.newArrayList();

    public DoorStructureHelper(World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending)
    {
        this.world = worldIn;
        this.pistonPos = posIn;

        if (extending)
        {
            this.moveDirection = pistonFacing;
            this.blockToMove = posIn.offset(pistonFacing);
        }
        else
        {
            this.moveDirection = pistonFacing.getOpposite();
            this.blockToMove = posIn.offset(pistonFacing, 2);
        }
    }

    public boolean canMove()
    {
        this.toMove.clear();
        this.toDestroy.clear();
        IBlockState state = this.world.getBlockState(this.blockToMove);

        if (!BlockPlatformDoor.Base.func_185646_a(state, this.world, this.blockToMove, this.moveDirection, false))
        {
            if (state.getMobilityFlag() != EnumPushReaction.DESTROY)
            {
                return false;
            }
            else
            {
                this.toDestroy.add(this.blockToMove);
                return true;
            }
        }
        else if (!this.func_177251_a(this.blockToMove))
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.toMove.size(); ++i)
            {
                BlockPos blockpos = (BlockPos)this.toMove.get(i);

                if (!this.func_177250_b(blockpos))
                {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean func_177251_a(BlockPos origin)
    {
    	IBlockState state = this.world.getBlockState(origin);
        Block block = state.getBlock();

        if (block.isAir(state, world, origin))
        {
            return true;
        }
        else if (!BlockPlatformDoor.Base.func_185646_a(state, this.world, origin, this.moveDirection, false))
        {
            return true;
        }
        else if (origin.equals(this.pistonPos))
        {
            return true;
        }
        else if (this.toMove.contains(origin))
        {
            return true;
        }
        else
        {
            int i = 1;

            if (i + this.toMove.size() > 12)
            {
                return false;
            }
            else
            {
                int i1 = 0;
                int j;

                for (j = i - 1; j >= 0; --j)
                {
                    this.toMove.add(origin.offset(this.moveDirection.getOpposite(), j));
                    ++i1;
                }

                j = 1;

                while (true)
                {
                    BlockPos blockpos2 = origin.offset(this.moveDirection, j);
                    int k = this.toMove.indexOf(blockpos2);

                    if (k > -1)
                    {
                        this.func_177255_a(i1, k);

                        for (int l = 0; l <= k + i1; ++l)
                        {
                            BlockPos blockpos3 = (BlockPos)this.toMove.get(l);

                            if (!this.func_177250_b(blockpos3))
                            {
                                return false;
                            }
                        }

                        return true;
                    }
                    state = this.world.getBlockState(blockpos2);
                    block = state.getBlock();

                    if (block.isAir(state, world, blockpos2))
                    {
                        return true;
                    }

                    if (!BlockPlatformDoor.Base.func_185646_a(state, this.world, blockpos2, this.moveDirection, true) || blockpos2.equals(this.pistonPos))
                    {
                        return false;
                    }

                    if (block.getMobilityFlag(state) == EnumPushReaction.DESTROY)
                    {
                        this.toDestroy.add(blockpos2);
                        return true;
                    }

                    if (this.toMove.size() >= 12)
                    {
                        return false;
                    }

                    this.toMove.add(blockpos2);
                    ++i1;
                    ++j;
                }
            }
        }
    }

    private void func_177255_a(int p_177255_1_, int p_177255_2_)
    {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        ArrayList arraylist2 = Lists.newArrayList();
        arraylist.addAll(this.toMove.subList(0, p_177255_2_));
        arraylist1.addAll(this.toMove.subList(this.toMove.size() - p_177255_1_, this.toMove.size()));
        arraylist2.addAll(this.toMove.subList(p_177255_2_, this.toMove.size() - p_177255_1_));
        this.toMove.clear();
        this.toMove.addAll(arraylist);
        this.toMove.addAll(arraylist1);
        this.toMove.addAll(arraylist2);
    }

    private boolean func_177250_b(BlockPos p_177250_1_)
    {
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j)
        {
            EnumFacing enumfacing = aenumfacing[j];

            if (enumfacing.getAxis() != this.moveDirection.getAxis() && !this.func_177251_a(p_177250_1_.offset(enumfacing)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a List<BlockPos> of all the blocks that are being moved by the piston.
     */
    public List getBlocksToMove()
    {
        return this.toMove;
    }

    /**
     * Returns an List<BlockPos> of all the blocks that are being destroyed by the piston.
     */
    public List getBlocksToDestroy()
    {
        return this.toDestroy;
    }
}
