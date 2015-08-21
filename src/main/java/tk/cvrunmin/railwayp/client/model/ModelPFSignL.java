package tk.cvrunmin.railwayp.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPFSignL extends ModelBase{
    ModelRenderer base;
    ModelRenderer box;
    ModelRenderer rod1;
    ModelRenderer rod2;
    public ModelPFSignL()
    {
        textureWidth = 128;
        textureHeight = 64;
        
          base = new ModelRenderer(this, 0, 0);
          base.addBox(-7.5F, -1F, -1F, 40, 16, 1);
          base.setRotationPoint(0F, 0F, -4F);
          box = new ModelRenderer(this, 0, 17);
          box.addBox(-7.5F, -1F, -9F, 40, 16, 9);
          box.setRotationPoint(0F, 0F, 5F);
          rod1 = new ModelRenderer(this, 82, 0);
          rod1.addBox(-1F, 0F, -1F, 2, 4, 2);
          rod1.setRotationPoint(0F, -5F, 0F);
          rod2 = new ModelRenderer(this, 82, 0);
          rod2.addBox(-1F, 0F, -1F, 2, 4, 2);
          rod2.setRotationPoint(24.5F, -5F, 0F);
    }
    public void render()
    {
        base.rotationPointY = -30.5F;
        box.rotationPointY = -30.5f;
        rod1.rotationPointY = -30.5f - 5f;
        rod2.rotationPointY = -30.5f - 5f;
    	float f5 = 0.0625f;
      base.render(f5);
      box.render(f5);
      rod1.render(f5);
      rod2.render(f5);
    }
    public void rodShow(boolean flag){
	box.showModel = flag;
	rod1.showModel = rod2.showModel = flag;
    }
}
