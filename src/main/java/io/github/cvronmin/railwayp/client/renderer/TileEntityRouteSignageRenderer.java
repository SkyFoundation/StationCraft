package io.github.cvronmin.railwayp.client.renderer;

import com.google.common.collect.Maps;
import io.github.cvronmin.railwayp.client.GLRenderUtil;
import io.github.cvronmin.railwayp.client.model.ModelRouteSignage;
import io.github.cvronmin.railwayp.client.renderer.texture.UnifedBannerTextures;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage.Station;
import static io.github.cvronmin.railwayp.client.GLRenderUtil.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TileEntityRouteSignageRenderer extends TileEntitySpecialRenderer<TileEntityRouteSignage> {
	/** An array of all the patterns that are being currently rendered. */
	private static final Map DESIGNS = Maps.newHashMap();
	private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("railwayp",
			"textures/entity/signage_base.png");
	private ModelRouteSignage bannerModel = new ModelRouteSignage();

	public void renderTileEntityAt(TileEntityRouteSignage entityBanner, double x, double y, double z, float partialTicks,
			int destroyStage) {
		boolean flag = entityBanner.getWorld() != null;
		int j = flag ? entityBanner.getBlockMetadata() : 0;
		long k = flag ? entityBanner.getWorld().getTotalWorldTime() : 0L;
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
		GlStateManager.translate(-0.0175F, -0.3125F, -0.5375F);
		// this.bannerModel.extensionViews(true);
		GlStateManager.enableRescaleNormal();
		ResourceLocation resourcelocation = this.func_178463_a(entityBanner);
		float[] stmidpts = null;
		if (resourcelocation != null) {
			this.bindTexture(resourcelocation);
			GlStateManager.pushMatrix();
			GlStateManager.scale(f1, -f1, -f1);
			this.bannerModel.renderBanner();
			if (entityBanner.checkGoodBanner()) {
				/** Route Length = 4(Scaled) **/
				stmidpts = new float[entityBanner.getStationList().size()];
				//stmidpts[0] = 0;
				float now = 0;
				int hereId = -1;
				float start = 0;
				float end = 0;
				for (int i = 0; i < stmidpts.length; i++) {
					stmidpts[i] = (float) i / (entityBanner.getStationList().size() - 1) * 4f;
					Station station = entityBanner.getStationList().get(i);
					if (station.amIHere()) {
						now = stmidpts[i] - 2f;
						if(hereId == -1){
							hereId = i;
							start = entityBanner.getDirection() == 0 ? now : (entityBanner.getDirection() == 2 ? -2f :  0);
							end = entityBanner.getDirection() == 0 ? 2f : (entityBanner.getDirection() == 2 ? now :  0);
						}
					}
				}
				//stmidpts[entityBanner.getStationList().size() - 1] = 4f;
				drawRouteLine(entityBanner, stmidpts, hereId, start, end);
				GlStateManager.depthMask(false);
			}
			GlStateManager.popMatrix();
		}
		if (stmidpts != null) {
			drawRouteLineText(entityBanner, destroyStage, f1, stmidpts);
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private void drawRouteLineText(TileEntityRouteSignage entityBanner, int destroyStage, float f1, float[] stmidpts) {
		float f3;FontRenderer fontrenderer = this.getFontRenderer();
		f3 = 0.015625F * f1;
		boolean overed = entityBanner.getDirection() == 2;
		boolean needpost = false, changed = false;
		int color = overed ? 0x7F7F7F : 0x000000;
		// Main
		for (int i = 0; i < stmidpts.length; i++) {
            float offset = stmidpts[i];
            float offsetY = 0.25f / 1.5f;
            if (i % 2 == 1)
                offsetY = -offsetY + 0.1f;
            Station station = entityBanner.getStationList().get(i);
            if (station.amIHere() & entityBanner.getDirection() != 1 & !changed) {
                if (overed) {
                    overed = false;
                    color = overed ? 0x7F7F7F : 0x000000;
                    changed = true;
                } else
                    needpost = true;
            }
            GlStateManager.pushMatrix();
			GlStateManager.translate((offset - 2f) / 1.5f, offsetY, 0);
            drawPairTexts(getFontRenderer(), 0, 0.5F * f1 + 0.6f, 0.07F * f1, f3 * .65f, f3 * .45f, color, station.stationName, new int[0], EnumAlignment.CENTER);
            GlStateManager.popMatrix();
            // MainEnd

            if (station.isInterchangeStation()) {
                f3 = 0.015625F * f1;
                offsetY = 0.025f;
                if (i % 2 == 1)
                    offsetY = -offsetY - .225f;
                // Next
				if(station.getInterchangeLines().size() == 1) {
					GlStateManager.pushMatrix();
					GlStateManager.translate((offset - 2f) / 1.5f, -offsetY, 0);
					drawPairTexts(fontrenderer, 0, 0.5f * f1 + 0.6f, 0.07f * f1, f3 * .45f, f3 * .25f, 20 * .66666667f, 20, color, station.getInterchangeLines().get(0).getLineName(), new int[0], EnumAlignment.CENTER);
					GlStateManager.popMatrix();
				}
				else{
					int max = station.getInterchangeLines().size() > 12 ? 12 : station.getInterchangeLines().size();
					GlStateManager.pushMatrix();
					GlStateManager.translate((offset - 2f) / 1.5f, -offsetY, 0);
					for (int j = 0; j < max; j++) {
						int m = j % MathHelper.ceil(max / 2f) + 1;
						int m1 = j <= MathHelper.ceil(max / 2f) - 1 ? 1 : -1;
						drawPairTexts(fontrenderer, 0.115f * m1, 0.5f * f1 + 0.6f + 0.05f * m * ((i % 2) == 0 ? -1 : 1) - .12f * ((i % 2) == 0 ? -1 : 1), 0.07f * f1, f3 * .45f, f3 * .25f, 20 * .66666667f, 20, color, station.getInterchangeLines().get(j).getLineName(), new int[0], j < MathHelper.ceil(max / 2f) ? EnumAlignment.LEFT : EnumAlignment.RIGHT);
					}
					GlStateManager.popMatrix();
				}
            }
            if (needpost) {
                overed = true;
                color = overed ? 0x7F7F7F : 0x000000;
                needpost = false;
                changed = true;
            }
        }
	}

	private void drawRouteLine(TileEntityRouteSignage entityBanner, float[] stmidpts, int hereId, float start, float end) {
		GlStateManager.disableTexture2D();
		//GlStateManager.disableLighting();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		int color = entityBanner.getRouteColor();
		drawRect(-2f,-1.55f, -0.07, 2f, -1.45, 0xff000000 | color);
		drawRect(start,-1.55f, -0.0705555, end, -1.45, 0xff7f7f7f);
		for (int i = 0; i < stmidpts.length; i++) {
            Station station = entityBanner.getStationList().get(i);
                drawStation(stmidpts[i], station, i, hereId != i && (entityBanner.getDirection() == 0 ? (i >= hereId) : (entityBanner.getDirection() == 2 && (i <= hereId))));
        }
		GlStateManager.enableTexture2D();
		//GlStateManager.enableLighting();
	}

	private ResourceLocation func_178463_a(TileEntityRouteSignage bannerObj) {
		return UnifedBannerTextures.ROUTESIGN_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(),
				bannerObj.getPatternList(), bannerObj.getColorList());

	}

	private void drawStation(double offsetMiddleTop, Station station, int index, boolean over) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		GlStateManager.depthMask(false);
		if (station.isInterchangeStation()) {
			if(station.getInterchangeLines().size() == 1) {
				int color = station.getInterchangeLines().get(0).getLineColor();
				float r = ((color >> 16) & 255) / 255f, g = ((color >> 8) & 255) / 255f, b = (color & 255) / 255f;
				if (index % 2 == 0) {
					drawRect(-2f + offsetMiddleTop - .025, -1.5f, -0.075, -2f + offsetMiddleTop + .025, -1.35f, over ? 0xff7f7f7f : 0xff000000 | color);
				} else {
					drawRect(-2f + offsetMiddleTop - .025, -1.65f, -0.075, -2f + offsetMiddleTop + .025, -1.5f, over ? 0xff7f7f7f : 0xff000000 | color);
				}
				GLRenderUtil.drawCircle(-2f + offsetMiddleTop, -1.5f, -0.0707776, over ? 0xff7f7f7f : 0xff000000, 0, 360, 0.055f);
				GLRenderUtil.drawCircle(-2f + offsetMiddleTop, -1.5f, -0.071, 0xffffffff, 0, 360, 0.045f);
			}
			else {
				float offlen;
				if (station.getInterchangeLines().size() >= 3 && station.getInterchangeLines().size() <= 12){
					offlen = .05f * (MathHelper.ceil(station.getInterchangeLines().size() / 2f) + 1) + .025f * MathHelper.ceil(station.getInterchangeLines().size() / 2f);
				}
				else if(station.getInterchangeLines().size() > 12){
					offlen = .05f * 7 + .025f * 6;
				}
				else{
					offlen = .05f * (station.getInterchangeLines().size() + 1) + .025f * (station.getInterchangeLines().size());
				}
				if (index % 2 == 0) {
					for (int i = 0; i < station.getInterchangeLines().size() && i < 12;i++){
						int color = station.getInterchangeLines().get(i).getLineColor();
						int m = i + 1;
						float len = .055f + 0.1f;
						int leftRatio = 0,
						rightRatio = 1;
						if(station.getInterchangeLines().size() > 12){
							m = i % 6 + 1;
							leftRatio = i > 5 ? -1 : 0;
							rightRatio = leftRatio + 1;
						}else if(station.getInterchangeLines().size() >= 3){
							m = i % MathHelper.ceil(station.getInterchangeLines().size() / 2f) + 1;
							leftRatio = i > MathHelper.ceil(station.getInterchangeLines().size() / 2f) - 1 ? -1 : 0;
							rightRatio = leftRatio + 1;
						}
						drawRect(-2f + offsetMiddleTop + len * leftRatio, -1.5f + 0.055f + .025f * m + .05f * (m - 1), -.075f, -2f + offsetMiddleTop + len * rightRatio, -1.5f + 0.055f + .025f * m + .05f * m, over ? 0xff7f7f7f : color | 0xff000000);
					}
					drawCircle(-2f + offsetMiddleTop, -1.5f, -0.0707776, over ? 0xff7f7f7f : 0xff000000, 0, 180, 0.055f);
					drawCircle(-2f + offsetMiddleTop, -1.5f, -0.071, 0xffffffff, 0, 180, 0.045f);
					drawRect(-2f + offsetMiddleTop - .055f, -1.5f, -.0707776, -2f + offsetMiddleTop + .055f, -1.5f + offlen,over ? 0xff7f7f7f : 0xff000000);
					drawRect(-2f + offsetMiddleTop - .045f, -1.5f, -.071, -2f + offsetMiddleTop + .045f, -1.5f + offlen,0xffffffff);
					drawCircle(-2f + offsetMiddleTop, -1.5f + offlen, -0.0707776, over ? 0xff7f7f7f : 0xff000000, 180, 360, 0.055f);
					drawCircle(-2f + offsetMiddleTop, -1.5f + offlen, -0.071, 0xffffffff, 180, 360, 0.045f);
				}
				else {
					for (int i = 0; i < station.getInterchangeLines().size() && i < 12;i++){
						int color = station.getInterchangeLines().get(i).getLineColor();
						int m = i + 1;
						float len = .055f + 0.1f;
						int leftRatio = 0,
								rightRatio = 1;
						if(station.getInterchangeLines().size() > 12){
							m = i % 6 + 1;
							leftRatio = i > 5 ? -1 : 0;
							rightRatio = leftRatio + 1;
						}else if(station.getInterchangeLines().size() >= 3){
							m = i % MathHelper.ceil(station.getInterchangeLines().size() / 2f) + 1;
							leftRatio = i > MathHelper.ceil(station.getInterchangeLines().size() / 2f) - 1 ? -1 : 0;
							rightRatio = leftRatio + 1;
						}
						drawRect(-2f + offsetMiddleTop + len * leftRatio, -1.5f - ( 0.055f + .025f * m + .05f * m), -.075f, -2f + offsetMiddleTop + len * rightRatio, -1.5f -( 0.055f + .025f * m + .05f * (m - 1)), over ? 0xff7f7f7f : color | 0xff000000);
					}
					drawCircle(-2f + offsetMiddleTop, -1.5f - offlen, -0.0707776, over ? 0xff7f7f7f : 0xff000000, 0, 180, 0.055f);
					drawCircle(-2f + offsetMiddleTop, -1.5f - offlen, -0.071, 0xffffffff, 0, 180, 0.045f);
					drawRect(-2f + offsetMiddleTop - .055f, -1.5f - offlen, -.0707776, -2f + offsetMiddleTop + .055f, -1.5f,over ? 0xff7f7f7f : 0xff000000);
					drawRect(-2f + offsetMiddleTop - .045f, -1.5f - offlen, -.071, -2f + offsetMiddleTop + .045f, -1.5f,0xffffffff);
					drawCircle(-2f + offsetMiddleTop, -1.5f, -0.0707776, over ? 0xff7f7f7f : 0xff000000, 180, 360, 0.055f);
					drawCircle(-2f + offsetMiddleTop, -1.5f, -0.071, 0xffffffff, 180, 360, 0.045f);
				}
			}
		}
		else {
			drawCircle(-2f + offsetMiddleTop, -1.5f, -0.0707776, over ? 0xff7f7f7f : 0xff000000, 0, 360, 0.055f);
			drawCircle(-2f + offsetMiddleTop, -1.5f, -0.071, 0xffffffff, 0, 360, 0.045f);
		}
		GlStateManager.depthMask(true);
	}

	@SideOnly(Side.CLIENT)
	static class TimedBannerTexture {
		/** The current system time, in milliseconds. */
		public long systemTime;
		/** The layered texture for the banner to render out. */
		public ResourceLocation bannerTexture;

		private TimedBannerTexture() {
		}

		TimedBannerTexture(Object p_i46209_1_) {
			this();
		}
	}
}