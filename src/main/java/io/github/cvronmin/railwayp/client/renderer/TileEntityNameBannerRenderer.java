package io.github.cvronmin.railwayp.client.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;

import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityNameBannerRenderer extends TileEntitySpecialRenderer<TileEntityNameBanner>
{
	@Override
	public void renderTileEntityAt(TileEntityNameBanner te, double x, double y, double z, float partialTicks,
			int destroyStage) {
        boolean flag = te.getWorld() != null;
        int j = flag ? te.getBlockMetadata() : 0;
        long k = flag ? te.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float f1 = 0.6666667F;
        float f3;

            f3 = 0.0F;

            if (j == 2)
            {
                f3 = 180.0F;
            }

            if (j == 4)
            {
                f3 = 90.0F;
            }

            if (j == 5)
            {
                f3 = -90.0F;
            }

            GlStateManager.translate((float)x + 0.5F, (float)y - 0.25F * f1, (float)z + 0.5F);
            GlStateManager.rotate(-f3, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.0175F, -0.3125F, -0.5375F);
        GlStateManager.enableRescaleNormal();
        FontRenderer fontrenderer = this.getFontRenderer();
        f3 = 0.015625F * f1;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.5F * f1 + 0.6, 0.07F * f1);
        GlStateManager.scale(f3 * 2, -f3 * 2, f3 * 2);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GlStateManager.depthMask(false);
        int xx;
        if (destroyStage < 0)
        {
                if (te.signText[0] != null)
                {
                    ITextComponent ichatcomponent = te.signText[0];
                    List<ITextComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((ITextComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0 * 10 - te.signText.length * 5, te.getColor());
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.5F * f1 + 0.6, 0.07F * f1);
        GlStateManager.scale(f3, -f3, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GlStateManager.depthMask(false);
        if (destroyStage < 0)
        {
                if (te.signText[1] != null)
                {
                    ITextComponent ichatcomponent = te.signText[1];
                    List<ITextComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((ITextComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 1 * 10 - te.signText.length * 5, te.getColor());
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

}