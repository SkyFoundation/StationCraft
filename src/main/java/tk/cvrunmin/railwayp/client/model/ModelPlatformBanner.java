package tk.cvrunmin.railwayp.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPlatformBanner extends ModelBase
{
    public ModelRenderer bannerSlate;
    public ModelRenderer bannerExtension1;
    public ModelRenderer bannerExtension2;

    public ModelPlatformBanner()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.bannerSlate = new ModelRenderer(this, 0, 0);
        this.bannerSlate.addBox(-11.5F, -1.0F, -1.0F, 48, 20, 1, 0.0F);
        this.bannerExtension1 = new ModelRenderer(this, 0, 21);
        this.bannerExtension1.addBox(-35.5f, -1.0f, -1.0f, 24, 20, 1, 0.0f);
        this.bannerExtension2 = new ModelRenderer(this, 0, 21);
        this.bannerExtension2.addBox(36.5f, -1.0f, -1.0f, 24, 20, 1, 0.0f);
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
        this.bannerSlate.rotationPointY = -32.0F;
        this.bannerExtension1.rotationPointY = -32.0f;
        this.bannerExtension2.rotationPointY = -32.0f;
        this.bannerSlate.render(0.0625F);
        this.bannerExtension1.render(0.0625f);
        this.bannerExtension2.render(0.0625f);
//        this.bannerStand.render(0.0625F);
//        this.bannerTop.render(0.0625F);
    }
    
    public void extensionViews(boolean state){
    	this.bannerExtension1.showModel = state;
    	this.bannerExtension2.showModel = state;
    }
}