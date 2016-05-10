package io.github.cvronmin.railwayp.client.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.cvronmin.railwayp.client.model.ModelRouteSignage;
import io.github.cvronmin.railwayp.client.renderer.texture.LayeredCustomColorMaskTexture;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityRouteSignageRenderer extends TileEntitySpecialRenderer
{
    /** An array of all the patterns that are being currently rendered. */
    private static final Map DESIGNS = Maps.newHashMap();
    private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("railwayp", "textures/entity/signage_base.png");
    private ModelRouteSignage bannerModel = new ModelRouteSignage();

    public void renderTileEntityBanner(TileEntityRouteSignage entityBanner, double x, double y, double z, float p_180545_8_, int p_180545_9_)
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
//            this.bannerModel.extensionViews(true);
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
        FontRenderer fontrenderer = this.getFontRenderer();
        f3 = 0.015625F * f1;
        //Main
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
        GlStateManager.scale(f3 * 1.5, -f3 * 1.5, f3 * 1.5);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GlStateManager.depthMask(false);
        if (p_180545_9_ < 0)
        {
                if (entityBanner.stationText[0] != null)
                {
                    ITextComponent ichatcomponent = entityBanner.stationText[0];
                    List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((ITextComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, (float)(-fontrenderer.getStringWidth(s) / 2)/* + 33.333333333f*/ + 2, 0 * 10 - entityBanner.stationText.length * 5, 0, false);
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
                if (entityBanner.stationText[1] != null)
                {
                    ITextComponent ichatcomponent = entityBanner.stationText[1];
                    List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((ITextComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, (float)(-fontrenderer.getStringWidth(s) / 2)/* + 50*/ + (2 * 1.5f), (float)(1 * 10 - entityBanner.stationText.length * 5), 0, false);
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        //MainEnd
        if(entityBanner.getDirection() != 1){
        f3 = 0.015625F * f1;
        //Next
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
        GlStateManager.scale(f3, -f3, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GlStateManager.depthMask(false);
        int xx = entityBanner.getDirection() == 0 ? -150 : 280-25-100;
        if (p_180545_9_ < 0)
        {
                if (entityBanner.nextText[0] != null)
                {
                    ITextComponent ichatcomponent = entityBanner.nextText[0];
                    List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((ITextComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2) + xx * 0.6666667f, 0 * 10 - entityBanner.nextText.length * 5 + 20 * 0.6666667f, 0, false);
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
        GlStateManager.scale(f3 * 0.6666667f, -f3 * 0.6666667f, f3 * 0.6666667f);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GlStateManager.depthMask(false);
        if (p_180545_9_ < 0)
        {
                if (entityBanner.nextText[1] != null)
                {
                    ITextComponent ichatcomponent = entityBanner.nextText[1];
                    List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? ((ITextComponent)list.get(0)).getFormattedText() : "";
                        fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2) + xx, 1 * 10 - entityBanner.nextText.length * 5 + 20, 0, false);
                }
        }
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        }
        //NextEnd
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private ResourceLocation func_178463_a(TileEntityRouteSignage bannerObj)
    {
        String s = bannerObj.func_175116_e();

        if (s.isEmpty())
        {
            return null;
        }
        else
        {
            TileEntityRouteSignageRenderer.TimedBannerTexture timedbannertexture = (TileEntityRouteSignageRenderer.TimedBannerTexture)DESIGNS.get(s);

            if (timedbannertexture == null)
            {
                if (DESIGNS.size() >= 256)
                {
                    long i = System.currentTimeMillis();
                    Iterator iterator = DESIGNS.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        String s1 = (String)iterator.next();
                        TileEntityRouteSignageRenderer.TimedBannerTexture timedbannertexture1 = (TileEntityRouteSignageRenderer.TimedBannerTexture)DESIGNS.get(s1);

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
                    TileEntityRouteSignage.EnumBannerPattern enumbannerpattern = (TileEntityRouteSignage.EnumBannerPattern)iterator1.next();
                    arraylist.add("railwayp" + ":" + "textures/entity/banner/" + enumbannerpattern.getPatternName() + ".png");
                }

                timedbannertexture = new TileEntityRouteSignageRenderer.TimedBannerTexture(null);
                timedbannertexture.bannerTexture = new ResourceLocation("railwayp", s);
                Minecraft.getMinecraft().getTextureManager().loadTexture(timedbannertexture.bannerTexture, new LayeredCustomColorMaskTexture(BANNERTEXTURES, arraylist, list));
                DESIGNS.put(s, timedbannertexture);
            }

            timedbannertexture.systemTime = System.currentTimeMillis();
            return timedbannertexture.bannerTexture;
        }
    }

    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.renderTileEntityBanner((TileEntityRouteSignage)te, x, y, z, partialTicks, destroyStage);
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