package io.github.cvronmin.railwayp.client.renderer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import io.github.cvronmin.railwayp.client.model.ModelRouteSignage;
import io.github.cvronmin.railwayp.client.renderer.texture.UnifedBannerTextures;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage.Station;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityRouteSignageRenderer extends TileEntitySpecialRenderer<TileEntityRouteSignage> {
	/** An array of all the patterns that are being currently rendered. */
	private static final Map DESIGNS = Maps.newHashMap();
	private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("railwayp",
			"textures/entity/signage_base.png");
	private ModelRouteSignage bannerModel = new ModelRouteSignage();

	public void renderTileEntityAt(TileEntityRouteSignage entityBanner, double x, double y, double z, float p_180545_8_,
			int p_180545_9_) {
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
				GlStateManager.disableTexture2D();
				//GlStateManager.disableLighting();
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer buffer = tessellator.getBuffer();
				buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				int color = entityBanner.getRouteColor();
				buffer.pos(-2f, -1.55f, -0.075)
				.color(((color >> 16) & 255) / 255f, ((color >> 8) & 255) / 255f, (color & 255) / 255f, 1f)
				.endVertex();
				buffer.pos(-2f, -1.45, -0.075)
						.color(((color >> 16) & 255) / 255f, ((color >> 8) & 255) / 255f, (color & 255) / 255f, 1f)
						.endVertex();
				buffer.pos(2f, -1.45, -0.075)
				.color(((color >> 16) & 255) / 255f, ((color >> 8) & 255) / 255f, (color & 255) / 255f, 1f)
				.endVertex();
				buffer.pos(2f, -1.55f, -0.075)
						.color(((color >> 16) & 255) / 255f, ((color >> 8) & 255) / 255f, (color & 255) / 255f, 1f)
						.endVertex();
				tessellator.draw();
				buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				buffer.pos(start, -1.55f, -0.075556).color(0.5f, 0.5f, 0.5f, 1f).endVertex();
				buffer.pos(start, -1.45, -0.075556).color(0.5f, 0.5f, 0.5f, 1f).endVertex();
				buffer.pos(end, -1.45, -0.075556).color(0.5f, 0.5f, 0.5f, 1f).endVertex();
				buffer.pos(end, -1.55f, -0.075556).color(0.5f, 0.5f, 0.5f, 1f).endVertex();
				tessellator.draw();
				for (int i = 0; i < stmidpts.length; i++) {
					Station station = entityBanner.getStationList().get(i);
						drawStation(stmidpts[i], station, i, hereId == i ? false : (entityBanner.getDirection() == 0 ? (i < hereId ? false : true) : (entityBanner.getDirection() == 2 ? (i > hereId ? false : true) : false)));
				}
				GlStateManager.enableTexture2D();
				//GlStateManager.enableLighting();
				GlStateManager.depthMask(false);
			}
			GlStateManager.popMatrix();
		}
		if (stmidpts != null) {
			FontRenderer fontrenderer = this.getFontRenderer();
			f3 = 0.015625F * f1;
			boolean overed = entityBanner.getDirection() == 2 ? true : false;
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
				GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
				GlStateManager.scale(f3 * .65, -f3 * .65, f3 * .65);
				GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
				GlStateManager.depthMask(false);
				if (p_180545_9_ < 0) {
					if (station.stationName[0] != null) {
						ITextComponent ichatcomponent = station.stationName[0];
						List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
						String s = list != null && list.size() > 0 ? ((ITextComponent) list.get(0)).getFormattedText()
								: "";
						fontrenderer.drawString(s, (float) (-fontrenderer.getStringWidth(s) / 2),
								0 * 10 - station.stationName.length * 5, color, false);
					}
				}
				GlStateManager.depthMask(true);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.translate((offset - 2f) / 1.5f, offsetY, 0);
				GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
				GlStateManager.scale(f3 * 0.45, -f3 * 0.45, f3 * 0.45);
				GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
				GlStateManager.depthMask(false);
				if (p_180545_9_ < 0) {
					if (station.stationName[1] != null) {
						ITextComponent ichatcomponent = station.stationName[1];
						List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
						String s = list != null && list.size() > 0 ? ((ITextComponent) list.get(0)).getFormattedText()
								: "";
						fontrenderer.drawString(s, (float) (-fontrenderer.getStringWidth(s) / 2),
								(float) (1 * 10 - station.stationName.length * 5), color, false);
					}
				}
				GlStateManager.depthMask(true);
				GlStateManager.popMatrix();
				// MainEnd

				if (station.isInterchangeStation()) {
					f3 = 0.015625F * f1;
					offsetY = 0.025f;
					if (i % 2 == 1)
						offsetY = -offsetY - .225f;
					// Next
					GlStateManager.pushMatrix();
					GlStateManager.translate((offset - 2f) / 1.5f, -offsetY, 0);
					GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
					GlStateManager.scale(f3 * .45, -f3 * .45, f3 * .45);
					GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
					GlStateManager.depthMask(false);
					if (p_180545_9_ < 0) {
						if (station.getInterchangeLineName()[0] != null) {
							ITextComponent ichatcomponent = station.getInterchangeLineName()[0];
							List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false,
									true);
							String s = list != null && list.size() > 0
									? ((ITextComponent) list.get(0)).getFormattedText() : "";
							fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2),
									0 * 10 - station.getInterchangeLineName().length * 5 + 20 * 0.6666667f, color,
									false);
						}
					}
					GlStateManager.depthMask(true);
					GlStateManager.popMatrix();
					GlStateManager.pushMatrix();
					GlStateManager.translate((offset - 2f) / 1.5f, -offsetY, 0);
					GlStateManager.translate(-0.4F + 0.4, 0.5F * f1 + 0.6, 0.07F * f1);
					GlStateManager.scale(f3 * 0.25, -f3 * 0.25, f3 * 0.25);
					GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
					GlStateManager.depthMask(false);
					if (p_180545_9_ < 0) {
						if (station.getInterchangeLineName()[1] != null) {
							ITextComponent ichatcomponent = station.getInterchangeLineName()[1];
							List list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false,
									true);
							String s = list != null && list.size() > 0
									? ((ITextComponent) list.get(0)).getFormattedText() : "";
							fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2),
									1 * 10 - station.getInterchangeLineName().length * 5 + 20, color, false);
						}
					}
					GlStateManager.depthMask(true);
					GlStateManager.popMatrix();
				} // NextEnd
				if (needpost) {
					overed = true;
					color = overed ? 0x7F7F7F : 0x000000;
					needpost = false;
					changed = true;
				}
			}
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private ResourceLocation func_178463_a(TileEntityRouteSignage bannerObj) {
		/*
		 * String s = bannerObj.func_175116_e();
		 * 
		 * if (s.isEmpty()) { return null; } else {
		 * TileEntityRouteSignageRenderer.TimedBannerTexture timedbannertexture
		 * = (TileEntityRouteSignageRenderer.TimedBannerTexture)DESIGNS.get(s);
		 * 
		 * if (timedbannertexture == null) { if (DESIGNS.size() >= 256) { long i
		 * = System.currentTimeMillis(); Iterator iterator =
		 * DESIGNS.keySet().iterator();
		 * 
		 * while (iterator.hasNext()) { String s1 = (String)iterator.next();
		 * TileEntityRouteSignageRenderer.TimedBannerTexture timedbannertexture1
		 * = (TileEntityRouteSignageRenderer.TimedBannerTexture)DESIGNS.get(s1);
		 * 
		 * if (i - timedbannertexture1.systemTime > 60000L) {
		 * Minecraft.getMinecraft().getTextureManager().deleteTexture(
		 * timedbannertexture1.bannerTexture); iterator.remove(); } }
		 * 
		 * if (DESIGNS.size() >= 256) { return null; } }
		 * 
		 * List list1 = bannerObj.getPatternList(); List list =
		 * bannerObj.getColorList(); ArrayList arraylist = Lists.newArrayList();
		 * Iterator iterator1 = list1.iterator();
		 * 
		 * while (iterator1.hasNext()) {
		 * TileEntityRouteSignage.EnumBannerPattern enumbannerpattern =
		 * (TileEntityRouteSignage.EnumBannerPattern)iterator1.next();
		 * arraylist.add("railwayp" + ":" + "textures/entity/banner/" +
		 * enumbannerpattern.getPatternName() + ".png"); }
		 * 
		 * timedbannertexture = new
		 * TileEntityRouteSignageRenderer.TimedBannerTexture(null);
		 * timedbannertexture.bannerTexture = new ResourceLocation("railwayp",
		 * s); Minecraft.getMinecraft().getTextureManager().loadTexture(
		 * timedbannertexture.bannerTexture, new
		 * LayeredCustomColorMaskTexture(BANNERTEXTURES, arraylist, list));
		 * DESIGNS.put(s, timedbannertexture); }
		 * 
		 * timedbannertexture.systemTime = System.currentTimeMillis(); return
		 * timedbannertexture.bannerTexture; }
		 */
		return UnifedBannerTextures.ROUTESIGN_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(),
				bannerObj.getPatternList(), bannerObj.getColorList());

	}

	private void drawStation(double offsetMiddleTop, Station station, int index, boolean overed) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		float bordercolor = overed ? 0.5f : 0f;
		GlStateManager.depthMask(false);
		if (station.isInterchangeStation()) {
			int color = station.getInterchangeLineColor();
			float r = ((color >> 16) & 255) / 255f, g = ((color >> 8) & 255) / 255f, b = (color & 255) / 255f;
			if (index % 2 == 0) {
				buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				buffer.pos(-2f + offsetMiddleTop - .025, -1.45f, -0.075)
				.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
				.endVertex();
				buffer.pos(-2f + offsetMiddleTop - .025, -1.35, -0.075)
						.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
						.endVertex();
				buffer.pos(-2f + offsetMiddleTop + .025, -1.35, -.075)
						.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
						.endVertex();
				buffer.pos(-2f + offsetMiddleTop + .025, -1.45f, -0.075)
				.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
				.endVertex();
				tessellator.draw();
			} else {
				buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				buffer.pos(-2f + offsetMiddleTop - .025, -1.65f, -0.075)
				.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
				.endVertex();
				buffer.pos(-2f + offsetMiddleTop - .025, -1.55, -0.075)
						.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
						.endVertex();
				buffer.pos(-2f + offsetMiddleTop + .025, -1.55, -.075)
				.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
				.endVertex();
				buffer.pos(-2f + offsetMiddleTop + .025, -1.65f, -0.075)
						.color(overed ? bordercolor : r, overed ? bordercolor : g, overed ? bordercolor : b, 1f)
						.endVertex();
				tessellator.draw();
			}
		}
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(-2f + offsetMiddleTop - .025, -1.525f, -0.077).color(1f, 1, 1, 1).endVertex();
		buffer.pos(-2f + offsetMiddleTop - .025, -1.475, -0.077).color(1f, 1, 1, 1).endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.475, -.077).color(1f, 1, 1, 1).endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.525f, -0.077).color(1f, 1, 1, 1).endVertex();
		tessellator.draw();

		// buffer.reset();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(-2f + offsetMiddleTop - .05, -1.525f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
		.endVertex();
		buffer.pos(-2f + offsetMiddleTop - .05, -1.475, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop - .025, -1.475, -.077).color(bordercolor, bordercolor, bordercolor, 1f)
		.endVertex();
		buffer.pos(-2f + offsetMiddleTop - .025, -1.525f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		tessellator.draw();
		// buffer.reset();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(-2f + offsetMiddleTop + .05, -1.475, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop + .05, -1.525f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.525f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.475, -.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		tessellator.draw();

		// buffer.reset();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(-2f + offsetMiddleTop - .025, -1.475, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop - .025, -1.45f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.45f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.475, -.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		tessellator.draw();
		// buffer.reset();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(-2f + offsetMiddleTop - .025, -1.55, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop - .025, -1.525f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.525f, -0.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		buffer.pos(-2f + offsetMiddleTop + .025, -1.55, -.077).color(bordercolor, bordercolor, bordercolor, 1f)
				.endVertex();
		tessellator.draw();
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