package io.github.cvronmin.railwayp.client.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.cvronmin.railwayp.client.model.ModelBlock;
import io.github.cvronmin.railwayp.client.renderer.texture.LayeredCustomColorMaskTexture;
import io.github.cvronmin.railwayp.client.renderer.texture.UnifedBannerTextures;
import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
public class TileEntityColorfulRenderer extends TileEntitySpecialRenderer<TileEntityColorful>{
    /** An array of all the patterns that are being currently rendered. */
    private static final Map DESIGNS = Maps.newHashMap();
    private static final ResourceLocation PLATE_TEXTURES = new ResourceLocation("railwayp", "textures/entity/plate.png");
    private static final ResourceLocation MOSAIC_TEXTURES = new ResourceLocation("railwayp", "textures/entity/mosaic.png");
	private ModelBlock model = new ModelBlock();
	public void renderTileEntityAt(TileEntityColorful te, double x, double y, double z,
			float partialTicks, int destroyStage) {
        boolean flag = te.getWorld() != null;
        int j = flag ? te.getBlockMetadata() : 0;
        long k = flag ? te.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float f1 = 0.6666667F;
        float f3;

            GlStateManager.translate((float)x + 0.5F, (float)y +0.5f, (float)z + 0.5F);
        	//GlStateManager.translate(-Math.cos(Math.toRadians(te.getRotation())) /2, 0, -Math.sin(Math.toRadians(te.getRotation())) /2);
            //float f2 = (float)(j * 360) / 16.0F;
            //GlStateManager.rotate(-te.getRotation(), 0.0F, 1.0F, 0.0F);

        GlStateManager.enableRescaleNormal();
        ResourceLocation resourcelocation = this.func_178463_a(te);
        if (resourcelocation != null)
        {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(f1, -f1, -f1);
            this.model.render();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0F);
        GlStateManager.popMatrix();
	}
    private ResourceLocation func_178463_a(TileEntityColorful bannerObj)
    {
        String s = "b15f" + bannerObj.getColor();
            TileEntityColorfulRenderer.TimedBannerTexture timedbannertexture = (TileEntityColorfulRenderer.TimedBannerTexture)DESIGNS.get(s);
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

                //List list1 = bannerObj.getPatternList();
                List<Integer> list = new ArrayList<>();
                ArrayList arraylist = Lists.newArrayList();
                //Iterator iterator1 = list1.iterator();

                arraylist.add("railwayp" + ":" + "textures/entity/block/base.png");
                arraylist.add("railwayp" + ":" + "textures/entity/block/filter.png");
                list.add(0xFFFFFF);
                list.add(bannerObj.getColor());

                timedbannertexture = new TileEntityColorfulRenderer.TimedBannerTexture(null);
                timedbannertexture.bannerTexture = new ResourceLocation("railwayp", s);
                Minecraft.getMinecraft().getTextureManager().loadTexture(timedbannertexture.bannerTexture, new LayeredCustomColorMaskTexture(bannerObj.getBlockType().getMaterial() == Material.rock ? MOSAIC_TEXTURES : PLATE_TEXTURES, arraylist, list));
                DESIGNS.put(s, timedbannertexture);
            }

            timedbannertexture.systemTime = System.currentTimeMillis();
            return timedbannertexture.bannerTexture;
        
        //return UnifedBannerTextures.PFSIGN_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(), bannerObj.getPatternList(), bannerObj.getColorList());

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
