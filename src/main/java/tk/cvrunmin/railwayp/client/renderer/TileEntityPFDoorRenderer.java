package tk.cvrunmin.railwayp.client.renderer;

import tk.cvrunmin.railwayp.block.BlockPlatformDoor;
import tk.cvrunmin.railwayp.init.RPBlocks;
import tk.cvrunmin.railwayp.tileentity.TileEntityPFDoor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityPFDoorRenderer extends TileEntitySpecialRenderer
{
    private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

    public void func_178461_a(TileEntityPFDoor piston, double p_178461_2_, double p_178461_4_, double p_178461_6_, float p_178461_8_, int p_178461_9_)
    {
        BlockPos blockpos = piston.getPos();
        IBlockState iblockstate = piston.getPistonState();
        Block block = iblockstate.getBlock();

        if (block.getMaterial() != Material.air && piston.func_145860_a(p_178461_8_) < 1.0F)
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            if (Minecraft.isAmbientOcclusionEnabled())
            {
                GlStateManager.shadeModel(7425);
            }
            else
            {
                GlStateManager.shadeModel(7424);
            }

            worldrenderer.startDrawingQuads();
            worldrenderer.setVertexFormat(DefaultVertexFormats.BLOCK);
            worldrenderer.setTranslation((double)((float)p_178461_2_ - (float)blockpos.getX() + (piston.func_174929_b(p_178461_8_) * 0.5f)), (double)((float)p_178461_4_ - (float)blockpos.getY() + (piston.func_174928_c(p_178461_8_) * 0.5f)), (double)((float)p_178461_6_ - (float)blockpos.getZ() + (piston.func_174926_d(p_178461_8_) * 0.5f)));
            worldrenderer.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            World world = this.getWorld();

            if (block == RPBlocks.platform_door_head && piston.func_145860_a(p_178461_8_) < 0.5F)
            {
                iblockstate = iblockstate.withProperty(BlockPlatformDoor.Extension.SHORT, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else if (piston.shouldPistonHeadBeRendered() && !piston.isExtending())
            {
                IBlockState iblockstate1 = RPBlocks.platform_door_head.getDefaultState().withProperty(BlockPlatformDoor.Extension.FACING, iblockstate.getValue(BlockPlatformDoor.Base.FACING));
                iblockstate1 = iblockstate1.withProperty(BlockPlatformDoor.Extension.SHORT, Boolean.valueOf(piston.func_145860_a(p_178461_8_) >= 0.5F));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate1, world, blockpos), iblockstate1, blockpos, worldrenderer, true);
                worldrenderer.setTranslation((double)((float)p_178461_2_ - (float)blockpos.getX()), (double)((float)p_178461_4_ - (float)blockpos.getY()), (double)((float)p_178461_6_ - (float)blockpos.getZ()));
                iblockstate.withProperty(BlockPlatformDoor.Base.EXTENDED, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else
            {
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, false);
            }

            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.func_178461_a((TileEntityPFDoor)te, x, y, z, partialTicks, destroyStage);
    }
}