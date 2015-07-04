package tk.cvrunmin.railwayp.client.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import tk.cvrunmin.railwayp.client.model.ModelPlatformBanner;
import tk.cvrunmin.railwayp.init.RPBlocks;
import tk.cvrunmin.railwayp.tileentity.TileEntityPlatformBanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SideOnly(Side.CLIENT)
public class TileEntityPlatformBannerRenderer extends TileEntitySpecialRenderer
{
    /** An array of all the patterns that are being currently rendered. */
    private static final Map DESIGNS = Maps.newHashMap();
    private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("railwayp", "textures/entity/banner_base.png");
    private ModelPlatformBanner bannerModel = new ModelPlatformBanner();

    public void renderTileEntityBanner(TileEntityPlatformBanner entityBanner, double x, double y, double z, float p_180545_8_, int p_180545_9_)
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
        ResourceLocation resourcelocation = this.func_178463_a(entityBanner);

        if (resourcelocation != null)
        {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(f1, -f1, -f1);
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }
        if(entityBanner.getDirection() != 1){
        FontRenderer fontrenderer = this.getFontRenderer();
        f3 = 0.015625F * f1;
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
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
                    xx = entityBanner.getDirection() == 0 ? 14 : (entityBanner.getDirection() == 2 ? 36 - fontrenderer.getStringWidth(s) : 0);
                        fontrenderer.drawString(s, xx, 0 * 10 - entityBanner.signText.length * 5, 0);
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
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
                    xx = entityBanner.getDirection() == 0 ? 28 : (entityBanner.getDirection() == 2 ? 72 - fontrenderer.getStringWidth(s) : 0);
                        fontrenderer.drawString(s, xx, 1 * 10 - entityBanner.signText.length * 5, 0);
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private ResourceLocation func_178463_a(TileEntityPlatformBanner bannerObj)
    {
        String s = bannerObj.func_175116_e();

        if (s.isEmpty())
        {
            return null;
        }
        else
        {
            TileEntityPlatformBannerRenderer.TimedBannerTexture timedbannertexture = (TileEntityPlatformBannerRenderer.TimedBannerTexture)DESIGNS.get(s);

            if (timedbannertexture == null)
            {
                if (DESIGNS.size() >= 256)
                {
                    long i = System.currentTimeMillis();
                    Iterator iterator = DESIGNS.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        String s1 = (String)iterator.next();
                        TileEntityPlatformBannerRenderer.TimedBannerTexture timedbannertexture1 = (TileEntityPlatformBannerRenderer.TimedBannerTexture)DESIGNS.get(s1);

                        if (i - timedbannertexture1.systemTime > 60000L)
                        {
                            Minecraft.getMinecraft().getTextureManager().deleteTexture(timedbannertexture1.bannerTexture);
                            iterator.remove();
                        }
                    }

                    if (DESIGNS.size() >= 256)
                    {
                        return null;
                    }
                }

                List list1 = bannerObj.getPatternList();
                List list = bannerObj.getColorList();
                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator1 = list1.iterator();

                while (iterator1.hasNext())
                {
                    TileEntityPlatformBanner.EnumBannerPattern enumbannerpattern = (TileEntityPlatformBanner.EnumBannerPattern)iterator1.next();
                    arraylist.add("railwayp" + ":" + "textures/entity/banner/" + enumbannerpattern.getPatternName() + ".png");
                }

                timedbannertexture = new TileEntityPlatformBannerRenderer.TimedBannerTexture(null);
                timedbannertexture.bannerTexture = new ResourceLocation("railwayp", s);
                Minecraft.getMinecraft().getTextureManager().loadTexture(timedbannertexture.bannerTexture, new LayeredColorMaskTexture(BANNERTEXTURES, arraylist, list));
                DESIGNS.put(s, timedbannertexture);
            }

            timedbannertexture.systemTime = System.currentTimeMillis();
            return timedbannertexture.bannerTexture;
        }
    }

    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.renderTileEntityBanner((TileEntityPlatformBanner)te, x, y, z, partialTicks, destroyStage);
    }

    @SideOnly(Side.CLIENT)
    static class TimedBannerTexture
        {
            /** The current system time, in milliseconds. */
            public long systemTime;
            /** The layered texture for the banner to render out. */
            public ResourceLocation bannerTexture;

            private TimedBannerTexture() {}

            TimedBannerTexture(Object p_i46209_1_)
            {
                this();
            }
        }
}