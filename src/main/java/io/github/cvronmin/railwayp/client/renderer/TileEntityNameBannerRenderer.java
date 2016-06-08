package io.github.cvronmin.railwayp.client.renderer;

import java.util.List;
import java.util.Locale;

import org.lwjgl.opengl.GL11;

import io.github.cvronmin.railwayp.client.model.ModelNameBanner;
import io.github.cvronmin.railwayp.client.renderer.texture.UnifedBannerTextures;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
//import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityNameBannerRenderer extends TileEntitySpecialRenderer<TileEntityNameBanner> {
	private ModelNameBanner model = new ModelNameBanner();
	private static final int TEXT2_MAX_ALLOW_LENGTH = 64;
	@Override
	public void renderTileEntityAt(TileEntityNameBanner te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		boolean flag = te.getWorld() != null;
		int j = flag ? te.getBlockMetadata() : 0;
		long k = flag ? te.getWorld().getTotalWorldTime() : 0L;
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
				ITextComponent ichatcomponent = te.signText[0];
				List<ITextComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false,
						true);
				String s = list != null && list.size() > 0 ? ((ITextComponent) list.get(0)).getFormattedText() : "";
				if (requireHdraw){
					String s1 = list != null && list.size() > 0 ? ((ITextComponent) list.get(0)).getUnformattedText() : "";
					drawHString(fontrenderer,s1, -fontrenderer.getCharWidth(s.charAt(0)),
							-fontrenderer.FONT_HEIGHT * s.length(), te.getColor());
				}else
					fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0 * 10 - te.signText.length * 5,
							te.getColor());
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
				ITextComponent ichatcomponent = te.signText[1];
				List<ITextComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false,
						true);
				String s = list != null && list.size() > 0 ? ((ITextComponent) list.get(0)).getFormattedText() : "";
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
	private void drawHString(FontRenderer fr, String s, int x, int y, int color){
		char[] sa = s.toCharArray();
		for (int i = 0; i < sa.length; i++) {
			char c = sa[i];
            if (c == 167 && i + 1 < s.length())
            {
                int i1 = "0123456789abcdefklmnor".indexOf(s.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if(i1 != -1){
                	i++;
                	continue;
                }
            }
			fr.drawString(Character.toString(c), x - fr.getCharWidth(c) / 2, y + i*fr.getCharWidth(c), color);
		}
	}
}