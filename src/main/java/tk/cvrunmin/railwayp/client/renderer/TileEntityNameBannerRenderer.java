package tk.cvrunmin.railwayp.client.renderer;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import tk.cvrunmin.railwayp.tileentity.TileEntityNameBanner;
import tk.cvrunmin.railwayp.tileentity.TileEntityPlatformBanner;

@SideOnly(Side.CLIENT)
public class TileEntityNameBannerRenderer extends TileEntitySpecialRenderer
{

    public void renderTileEntityBanner(TileEntityNameBanner entityBanner, double x, double y, double z, float p_180545_8_, int p_180545_9_)
    {
        boolean flag = entityBanner.getWorld() != null;
        int j = flag ? entityBanner.getBlockMetadata() : 0;
        long k = flag ? entityBanner.getWorld().getTotalWorldTime() : 0L;
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
        if (p_180545_9_ < 0)
        {
                if (entityBanner.signText[0] != null)
                {
                    IChatComponent ichatcomponent = entityBanner.signText[0];
                    List list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((IChatComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0 * 10 - entityBanner.signText.length * 5, entityBanner.getColor());
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.5F * f1 + 0.6, 0.07F * f1);
        GlStateManager.scale(f3, -f3, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GlStateManager.depthMask(false);
        if (p_180545_9_ < 0)
        {
                if (entityBanner.signText[1] != null)
                {
                    IChatComponent ichatcomponent = entityBanner.signText[1];
                    List list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((IChatComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 1 * 10 - entityBanner.signText.length * 5, entityBanner.getColor());
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.renderTileEntityBanner((TileEntityNameBanner)te, x, y, z, partialTicks, destroyStage);
    }
}