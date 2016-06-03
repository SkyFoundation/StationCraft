package io.github.cvronmin.railwayp.client.renderer.texture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.tileentity.EnumUnifiedBannerPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UnifedBannerTextures {
    /** An array of all the platform banner patterns that are being currently rendered */
    public static final UnifedBannerTextures.Cache BANNER_DESIGNS = new UnifedBannerTextures.Cache("PB", new ResourceLocation("railwayp", "textures/entity/banner_base.png"), "railwayp:textures/entity/banner/");
    /** An array of all the route signage patterns that are being currently rendered */
    public static final UnifedBannerTextures.Cache ROUTESIGN_DESIGNS = new UnifedBannerTextures.Cache("RS", new ResourceLocation("railwayp", "textures/entity/signage_base.png"), "railwayp:textures/entity/banner/");
    /** An array of all the platform sign patterns that are being currently rendered */
    public static final UnifedBannerTextures.Cache PFSIGN_DESIGNS = new UnifedBannerTextures.Cache("PS", new ResourceLocation("railwayp", "textures/entity/pfsign_l.png"), "railwayp:textures/entity/pfsign/");
    public static final ResourceLocation PFSIGN_BASE_TEXTURE = new ResourceLocation("railwayp", "textures/entity/pfsign/base.png");
    public static final ResourceLocation BANNER_BASE_TEXTURE = new ResourceLocation("railwayp", "textures/entity/banner/base.png");
    @SideOnly(Side.CLIENT)
    public static class Cache
        {
            private final Map<String, UnifedBannerTextures.CacheEntry> cacheMap = Maps.<String, UnifedBannerTextures.CacheEntry>newLinkedHashMap();
            private final ResourceLocation cacheResourceLocation;
            private final String cacheResourceBase;
            private String cacheId;

            public Cache(String p_i46998_1_, ResourceLocation p_i46998_2_, String p_i46998_3_)
            {
                this.cacheId = p_i46998_1_;
                this.cacheResourceLocation = p_i46998_2_;
                this.cacheResourceBase = p_i46998_3_;
            }

            public ResourceLocation getResourceLocation(String p_187478_1_, List<EnumUnifiedBannerPattern> p_187478_2_, List<Integer> p_187478_3_)
            {
                if (p_187478_1_.isEmpty())
                {
                    return null;
                }
                else
                {
                    p_187478_1_ = this.cacheId + p_187478_1_;
                    UnifedBannerTextures.CacheEntry bannertextures$cacheentry = (UnifedBannerTextures.CacheEntry)this.cacheMap.get(p_187478_1_);

                    if (bannertextures$cacheentry == null)
                    {
                        if (this.cacheMap.size() >= 256 && !this.func_187477_a())
                        {
                            return UnifedBannerTextures.BANNER_BASE_TEXTURE;
                        }

                        List<String> list = Lists.<String>newArrayList();

                        for (EnumUnifiedBannerPattern tileentitybanner$enumbannerpattern : p_187478_2_)
                        {
                            list.add(this.cacheResourceBase + tileentitybanner$enumbannerpattern.getPatternName() + ".png");
                        }

                        bannertextures$cacheentry = new UnifedBannerTextures.CacheEntry();
                        bannertextures$cacheentry.texture = new ResourceLocation("railwayp", p_187478_1_);
                        Minecraft.getMinecraft().getTextureManager().loadTexture(bannertextures$cacheentry.texture, new LayeredCustomColorMaskTexture(this.cacheResourceLocation, list, p_187478_3_));
                        this.cacheMap.put(p_187478_1_, bannertextures$cacheentry);
                    }

                    bannertextures$cacheentry.systemTime = System.currentTimeMillis();
                    return bannertextures$cacheentry.texture;
                }
            }

            private boolean func_187477_a()
            {
                long i = System.currentTimeMillis();
                Iterator<String> iterator = this.cacheMap.keySet().iterator();

                while (iterator.hasNext())
                {
                    String s = (String)iterator.next();
                    UnifedBannerTextures.CacheEntry bannertextures$cacheentry = (UnifedBannerTextures.CacheEntry)this.cacheMap.get(s);

                    if (i - bannertextures$cacheentry.systemTime > 5000L)
                    {
                        Minecraft.getMinecraft().getTextureManager().deleteTexture(bannertextures$cacheentry.texture);
                        iterator.remove();
                        return true;
                    }
                }

                return this.cacheMap.size() < 256;
            }
        }

    @SideOnly(Side.CLIENT)
    static class CacheEntry
        {
            public long systemTime;
            public ResourceLocation texture;

            private CacheEntry()
            {
            }
        }
}
