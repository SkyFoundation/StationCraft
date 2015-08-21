package tk.cvrunmin.railwayp.client.renderer;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import tk.cvrunmin.railwayp.tileentity.TileEntityColorful;
@Deprecated
public class TileEntityColorfulRenderer extends TileEntitySpecialRenderer{
    private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage) {
		this.renderTileEntityAt((TileEntityColorful)te, x, y, z, partialTicks, destroyStage);
	}
	public void renderTileEntityAt(TileEntityColorful te, double x, double y, double z,
			float partialTicks, int destroyStage) {
        BlockPos blockpos = te.getPos();
        IBlockState state = te.getWorld().getBlockState(blockpos);
        Block block = state.getBlock();
        
        if (block.getMaterial() != Material.air)
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
        }
	}
}
