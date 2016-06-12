package io.github.cvronmin.railwayp.client.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import io.github.cvronmin.railwayp.client.model.ModelPFSignL;
import io.github.cvronmin.railwayp.client.renderer.texture.UnifedBannerTextures;
import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWHPFRenderer extends TileEntitySpecialRenderer<TileEntityWHPF>{
    /** An array of all the patterns that are being currently rendered. */
    private static final Map DESIGNS = Maps.newHashMap();
    private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("railwayp", "textures/entity/pfsign_l.png");
	private static final int TEXT1_MAX_ALLOW_LENGTH = 47;
	private static final int TEXT2_MAX_ALLOW_LENGTH = 92;
    private ModelPFSignL bannerModel = new ModelPFSignL();

    public void renderTileEntityAt(TileEntityWHPF entityBanner, double x, double y, double z, float p_180545_8_, int p_180545_9_)
    {
        boolean flag = entityBanner.getWorld() != null;
        boolean flag1 = !flag || entityBanner.getBlockType() == RPBlocks.roof_where_pf;
        int j = flag ? entityBanner.getBlockMetadata() : 0;
        long k = flag ? entityBanner.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        	float f1 = 0.6666667F;
        	float f3;

        	if (flag1)
        	{
        		GlStateManager.translate((float)x + 0.5F, (float)y - 0.75F * f1, (float)z + 0.5F);
        		GlStateManager.translate(-Math.cos(Math.toRadians(entityBanner.getRotation())) /2, 0, -Math.sin(Math.toRadians(entityBanner.getRotation())) /2);
        		//float f2 = (float)(j * 360) / 16.0F;
        		GlStateManager.rotate(-entityBanner.getRotation(), 0.0F, 1.0F, 0.0F);
        		this.bannerModel.rodShow(true);
        	}
        	else
        	{
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
        		GlStateManager.translate(-0.0175F, -0.3125F, -0.6875F);
        		this.bannerModel.rodShow(false);
        	}
        	GlStateManager.enableRescaleNormal();
        	ResourceLocation resourcelocation = this.func_178463_a(entityBanner);
        
        	if (resourcelocation != null)
        	{
        		this.bindTexture(resourcelocation);
        		GlStateManager.pushMatrix();
        			GlStateManager.scale(f1, -f1, -f1);
        			this.bannerModel.render();
        		GlStateManager.popMatrix();
        	}
        	FontRenderer fontrenderer = this.getFontRenderer();
        	f3 = 0.015625F * f1;
        	GlStateManager.pushMatrix();
        		GlStateManager.translate(-0.4F, 0.5F * f1 + 0.6, 0.07F * f1 + 0.17);
        		GlStateManager.scale(f3 * 2, -f3 * 2, f3 * 2);
        		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        		GlStateManager.depthMask(false);
        		int xx;
        		if (p_180545_9_ < 0)
        		{
        			if (entityBanner.signText[0] != null)
        			{
        				IChatComponent ichatcomponent = entityBanner.signText[0];
        				List list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 180, fontrenderer, false, true);
        				String s = list != null && list.size() > 0 ? ((IChatComponent)list.get(0)).getFormattedText() : "";
        				xx = entityBanner.getDirection() <= 1 ? 12 + 25 : (entityBanner.getDirection() >= 2 ? 36 + 16 - fontrenderer.getStringWidth(s) : 0);
        				int xxx = fontrenderer.getStringWidth(s);
        				//System.out.println("Text1:" + xxx);
        				if (xxx > TEXT1_MAX_ALLOW_LENGTH) {
        					GlStateManager.scale(TEXT1_MAX_ALLOW_LENGTH / (float)xxx, 1, 1);
        					if(entityBanner.getDirection() <= 1)
        						xx *= (float)xxx / TEXT1_MAX_ALLOW_LENGTH;
        					else{
        						xx /= (float)xxx / TEXT1_MAX_ALLOW_LENGTH * 16;
        						xx += 5 * ((float)xxx / TEXT1_MAX_ALLOW_LENGTH);
        					}
        				}
                        fontrenderer.drawString(s, xx, 0 * 10 - entityBanner.signText.length * 5, 16777215);
        			}
        		}
        		GlStateManager.depthMask(true);
        	GlStateManager.popMatrix();
        	GlStateManager.pushMatrix();
        		GlStateManager.translate(-0.4F, 0.5F * f1 + 0.6, 0.07F * f1 + 0.17);
        		GlStateManager.scale(f3, -f3, f3);
        		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        		GlStateManager.depthMask(false);
        		if (p_180545_9_ < 0)
        		{
        			if (entityBanner.signText[1] != null)
        			{
        				IChatComponent ichatcomponent = entityBanner.signText[1];
        				List list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 180, fontrenderer, false, true);
        				String s = list != null && list.size() > 0 ? ((IChatComponent)list.get(0)).getFormattedText() : "";
        				xx = entityBanner.getDirection() <= 1 ? 24 + 50 : (entityBanner.getDirection() >= 2 ? 72 + 30 - fontrenderer.getStringWidth(s) : 0);
        				int xxx = fontrenderer.getStringWidth(s);
        				//System.out.println("Text2:"+xxx);
        				if (xxx > TEXT2_MAX_ALLOW_LENGTH) {
        					GlStateManager.scale(TEXT2_MAX_ALLOW_LENGTH / (float)xxx, 1, 1);
        					if(entityBanner.getDirection() <= 1)
        						xx *= (float)xxx / TEXT2_MAX_ALLOW_LENGTH;
        					else{
        						xx /= (float)xxx / TEXT2_MAX_ALLOW_LENGTH * 16;
        						xx += 10 * ((float)xxx / TEXT2_MAX_ALLOW_LENGTH);
        					}
        				}
        				fontrenderer.drawString(s, xx, 1 * 5 - entityBanner.signText.length * 5, 16777215);
        			}
        		}
        		GlStateManager.depthMask(true);
        	GlStateManager.popMatrix();
        	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private ResourceLocation func_178463_a(TileEntityWHPF bannerObj)
    {
        /*String s = bannerObj.func_175116_e();

        if (s.isEmpty())
        {
            return null;
        }
        else
        {
            TileEntityWHPFRenderer.TimedBannerTexture timedbannertexture = (TileEntityWHPFRenderer.TimedBannerTexture)DESIGNS.get(s);

            if (timedbannertexture == null)
            {
                if (DESIGNS.size() >= 256)
                {
                    long i = System.currentTimeMillis();
                    Iterator iterator = DESIGNS.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        String s1 = (String)iterator.next();
                        TileEntityWHPFRenderer.TimedBannerTexture timedbannertexture1 = (TileEntityWHPFRenderer.TimedBannerTexture)DESIGNS.get(s1);

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
                    TileEntityWHPF.EnumBannerPattern enumbannerpattern = (TileEntityWHPF.EnumBannerPattern)iterator1.next();
                    arraylist.add("railwayp" + ":" + "textures/entity/pfsign/" + enumbannerpattern.getPatternName() + ".png");
                }

                timedbannertexture = new TileEntityWHPFRenderer.TimedBannerTexture(null);
                timedbannertexture.bannerTexture = new ResourceLocation("railwayp", s);
                Minecraft.getMinecraft().getTextureManager().loadTexture(timedbannertexture.bannerTexture, new LayeredCustomColorMaskTexture(BANNERTEXTURES, arraylist, list));
                DESIGNS.put(s, timedbannertexture);
            }

            timedbannertexture.systemTime = System.currentTimeMillis();
            return timedbannertexture.bannerTexture;
        }*/
        return UnifedBannerTextures.PFSIGN_DESIGNS.getResourceLocation(bannerObj.func_175116_e(), bannerObj.getPatternList(), bannerObj.getColorList());

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
