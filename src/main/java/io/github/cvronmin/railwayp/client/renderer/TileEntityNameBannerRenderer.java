package io.github.cvronmin.railwayp.client.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;

import io.github.cvronmin.railwayp.block.BlockNameBanner;
import io.github.cvronmin.railwayp.client.model.ModelNameBanner;
import io.github.cvronmin.railwayp.client.renderer.texture.UnifedBannerTextures;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityNameBannerRenderer extends TileEntitySpecialRenderer<TileEntityNameBanner> {
	private ModelNameBanner model = new ModelNameBanner();
	private static final int TEXT2_MAX_ALLOW_LENGTH = 64;
	private static final int TEXT1_VERICAL_MAX_ALLOW_LENGTH = 72;
	private static final int TEXT1_HORIZONTAL_MAX_ALLOW_LENGTH = 48;
	@Override
	public void renderTileEntityAt(TileEntityNameBanner te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		boolean flag = te.getWorld() != null;
		int j = flag ? te.getBlockMetadata() : 0;
		long k = flag ? te.getWorld().getTotalWorldTime() : 0L;
		//Block block = te.getBlockType();
		GlStateManager.pushMatrix();
		float f1 = 0.6666667F;
		float f3;

		f3 = 0.0F;

		if (j == 2) {
			f3 = 180.0F;
		}

		if (j == 4) {
			f3 = 90.0F;
		}

		if (j == 5) {
			f3 = -90.0F;
		}
		GlStateManager.translate((float) x + 0.5F, (float) y - 0.25F * f1, (float) z + 0.5F);
		GlStateManager.rotate(-f3, 0.0F, 1.0F, 0.0F);
		if (te.getWorld() != null & te.getPos() != null) {
	        EnumFacing enumfacing = (EnumFacing)te.getWorld().getBlockState(te.getPos()).getValue(BlockNameBanner.FACING);
	        TileEntity te1 = te.getWorld().getTileEntity(te.getPos().offset(enumfacing.getOpposite()));
	        if (te1 instanceof TileEntityColorful)
	        {
        		//GlStateManager.translate(-Math.cos(Math.toRadians(((TileEntityColorful)te1).getRotation())), 0, -Math.sin(Math.toRadians(((TileEntityColorful)te1).getRotation())));
	        	//GlStateManager.rotate(-((TileEntityColorful)te1).getRotation(), 0, 1, 0);
	        }
		}
		GlStateManager.translate(-0.0175F, -0.3125F - 0.05, -0.5375F + 0.055);
		GlStateManager.enableRescaleNormal();
		float a = 0, b = 0, corr = 0.055f;
		//System.out.println("SignType:" + te.getSignType());
		if (te.getSignType() != 0) {
			a= 1/16f;
			b = 0.4f;
			corr = 0.025f;
			ResourceLocation resourcelocation = this.getBannerResourceLocation(te);
			//System.out.println(resourcelocation);
			if (resourcelocation != null) {
				this.bindTexture(resourcelocation);
				GlStateManager.pushMatrix();
				GlStateManager.scale(f1, -f1, -f1);
				this.model.renderBanner();
				GlStateManager.popMatrix();
			}
		}
		//System.out.println("a:" + a);
		FontRenderer fontrenderer = this.getFontRenderer();
		f3 = 0.015625F * f1;
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0.5F * f1 + 0.6 - b * 2.15, 0.07F * f1 - corr);
		GlStateManager.scale(f3 * 2, -f3 * 2, f3 * 2);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
		GlStateManager.depthMask(false);
		int xx;
		boolean requireHdraw = te.getSignType() == 1 ? true : (te.getSignType() == 2 ? true : false);
		if (destroyStage < 0) {
			if (te.signText[0] != null) {
				IChatComponent ichatcomponent = te.signText[0];
				List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false,
						true);
				String s = list != null && list.size() > 0 ? ((IChatComponent) list.get(0)).getFormattedText() : "";

				if (requireHdraw){
					String s1 = list != null && list.size() > 0 ? ((IChatComponent) list.get(0)).getUnformattedText() : "";
					//System.out.println(fontrenderer.FONT_HEIGHT * s.length());
					drawVString(fontrenderer,s1, -fontrenderer.getCharWidth(s.charAt(0)),
							-fontrenderer.FONT_HEIGHT * s.length(), te.getColor());
				}else{
					//System.out.println(fontrenderer.getStringWidth(s));
					fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0 * 10 - te.signText.length * 5,
							te.getColor());
				}
			}
		}
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0.5F * f1 + 0.6 - b * 0.35, 0.07F * f1 - corr);
		GlStateManager.scale(f3, -f3, f3);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
		GlStateManager.depthMask(false);
		if (destroyStage < 0) {
			if (te.signText[1] != null) {
				IChatComponent ichatcomponent = te.signText[1];
				List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false,
						true);
				String s = list != null && list.size() > 0 ? ((IChatComponent) list.get(0)).getFormattedText() : "";
				int xxx = fontrenderer.getStringWidth(s);
				if (xxx > TEXT2_MAX_ALLOW_LENGTH) {
					GlStateManager.scale(TEXT2_MAX_ALLOW_LENGTH / (float)xxx, 1, 1);
				}
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 1 * 10 - te.signText.length * 5,
						te.getColor());
			}
		}
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private ResourceLocation getBannerResourceLocation(TileEntityNameBanner te) {
		return UnifedBannerTextures.NAMESIGN_DESIGNS.getResourceLocation(te.getPatternResourceLocation(),
				te.getPatternList(), te.getColorList());
	}
	private void drawVString(FontRenderer fr, String s, int x, int y, int color){
		char[] sa = s.toCharArray();
		for (int i = 0; i < sa.length; i++) {
			char c = sa[i];
            /*if (c == 167 && i + 1 < s.length())
            {
                int i1 = "0123456789abcdefklmnor".indexOf(s.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if(i1 != -1){
                	i++;
                	continue;
                }
            }*/
			fr.drawString(Character.toString(c), x - fr.getCharWidth(c) / 2, y + fr.FONT_HEIGHT * i, color);
		}
	}
}