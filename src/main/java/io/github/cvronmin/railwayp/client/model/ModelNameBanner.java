package io.github.cvronmin.railwayp.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelNameBanner extends ModelBase {
    public ModelRenderer bannerSlate;

    public ModelNameBanner()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.bannerSlate = new ModelRenderer(this, 0, 0);
        this.bannerSlate.addBox(-8.0F, -15.0F, -0.5F, 16, 30, 1, 0.0F);
//        this.bannerStand = new ModelRenderer(this, 44, 0);
//        this.bannerStand.addBox(-1.0F, -30.0F, -1.0F, 2, 42, 2, 0.0F);
//        this.bannerTop = new ModelRenderer(this, 0, 42);
//        this.bannerTop.addBox(-10.0F, -32.0F, -1.0F, 20, 2, 2, 0.0F);
    }

    /**
     * Renders the banner model in.
     */
    public void renderBanner()
    {
        this.bannerSlate.rotationPointY = -32.0F * 0.725f;
        this.bannerSlate.render(0.0625F);
//        this.bannerStand.render(0.0625F);
//        this.bannerTop.render(0.0625F);
    }
}
