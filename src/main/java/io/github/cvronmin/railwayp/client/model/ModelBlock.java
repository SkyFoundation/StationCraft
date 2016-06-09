package io.github.cvronmin.railwayp.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBlock extends ModelBase {
	public ModelRenderer modelBlock;
	public ModelBlock(){
		this.textureWidth = 64;
		this.textureHeight = 32;
		modelBlock = new ModelRenderer(this, 0, 0);
		modelBlock.addBox(-8, -8, -8, 16, 16, 16);
	}
	public void render(){
		modelBlock.rotationPointY = -0;
		modelBlock.render(0.09375f);
	}
}
