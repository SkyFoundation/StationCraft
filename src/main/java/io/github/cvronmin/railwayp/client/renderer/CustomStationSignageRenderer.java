package io.github.cvronmin.railwayp.client.renderer;

import java.util.Map;

import io.github.cvronmin.railwayp.client.renderer.texture.SimpleDetailedTexture;
import io.github.cvronmin.railwayp.json.CustomStationSignageDefinition;
import io.github.cvronmin.railwayp.json.CustomStationSignageDefinition.Polygon;
import io.github.cvronmin.railwayp.json.CustomStationSignageDefinition.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class CustomStationSignageRenderer {
	public static void render(CustomStationSignageDefinition definition, Tessellator tessellator, VertexBuffer buffer, FontRenderer font, Map<String, Object> var){
		Map<String, Polygon> mapp = definition.getPolygons();
		for (Polygon polygon : mapp.values()) {
			renderPolygon(polygon, tessellator, buffer, var);
		}
		Map<String, Text> mapt = definition.getTexts();
		for (Text text : mapt.values()) {
			renderText(text, font, var);
		}
	}
	
	private static void renderPolygon(Polygon polygon, Tessellator tessellator, VertexBuffer buffer, Map<String, Object> var){
		float[] pos = polygon.getVertices();
		/*if(polygon.isColorVariable() | polygon.isBackgroundVariable()){
			return; //TODO: variable not support
		}*/
		if(polygon.isNoColor() && polygon.isNoBackground()){
			buffer.begin(7, DefaultVertexFormats.POSITION);
			for(int i = 0;i < polygon.getVertices().length; i+=3){
				buffer.pos(pos[i], pos[i+1], pos[i+2]).endVertex();
			}
			tessellator.draw();
		}
		else if (polygon.isColorHexColor() && polygon.isNoBackground()) {
			int color = Integer.parseInt(polygon.getColor().replace("0x", ""),16);
			float red = ((color >> 16) & 255) / 255f,
					green = ((color >> 8) & 255) / 255f,
					blue = (color & 255) / 255f;
			buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			for(int i = 0;i < polygon.getVertices().length; i+=3){
				buffer.pos(pos[i], pos[i+1], pos[i+2]).color(red, green, blue, 1.0f).endVertex();
			}
			tessellator.draw();
		}
		else if (!polygon.isNoColor() && polygon.isNoBackground()) {
			String key = polygon.getColor();
			if(var.containsKey(key)){
				Object var1 = var.get(key);
				if(var1 instanceof Integer){
					int color = (int)var1;
					float red = ((color >> 16) & 255) / 255f,
							green = ((color >> 8) & 255) / 255f,
							blue = (color & 255) / 255f;
					buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
					for(int i = 0;i < polygon.getVertices().length; i+=3){
						buffer.pos(pos[i], pos[i+1], pos[i+2]).color(red, green, blue, 1.0f).endVertex();
					}
					tessellator.draw();
				}else return;
			}else {
				return;
			}
		}
		else if (polygon.isNoColor() && !polygon.isNoBackground()) {
			if(pos.length < 12)return;
			String bg = polygon.getBackground();
			ResourceLocation location;
			if(polygon.isBackgroundVariable()){
				Object var1 = var.get(bg);
				if (var1 instanceof String) {
					location = new ResourceLocation((String)var1);
				}else if (var1 instanceof ResourceLocation){
					location = (ResourceLocation)var1;
				}else{
					return;
				}
			}else{
				location = new ResourceLocation(bg);
			}
			SimpleDetailedTexture texture = new SimpleDetailedTexture(location);
			FMLClientHandler.instance().getClient().getTextureManager().loadTexture(location, texture);
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(location);
			texture = (SimpleDetailedTexture) Minecraft.getMinecraft().getTextureManager().getTexture(location);
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
				buffer.pos(pos[0], pos[0+1], pos[0+2]).tex(0, 0).endVertex();
				buffer.pos(pos[3], pos[3+1], pos[3+2]).tex(texture.getImage().getWidth(), 0).endVertex();
				buffer.pos(pos[6], pos[6+1], pos[6+2]).tex(texture.getImage().getWidth(), texture.getImage().getHeight()).endVertex();
				buffer.pos(pos[9], pos[9+1], pos[9+2]).tex(0, texture.getImage().getHeight()).endVertex();
			tessellator.draw();
		}else {
			if (pos.length < 12)
				return;
			int color = 0;
			if (polygon.isColorVariable()) {
				String key = polygon.getColor();
				if (var.containsKey(key)) {
					Object var1 = var.get(key);
					if (var1 instanceof Integer) {
						color = (int) var1;
					}
				}
			} else if (polygon.isColorHexColor()) {
				color = Integer.parseInt(polygon.getColor().replace("0x", ""), 16);
			} else
				return;
			float red = ((color >> 16) & 255) / 255f,
					green = ((color >> 8) & 255) / 255f,
					blue = (color & 255) / 255f;
			String bg = polygon.getBackground();
			ResourceLocation location;
			if(polygon.isBackgroundVariable()){
				Object var1 = var.get(bg);
				if (var1 instanceof String) {
					location = new ResourceLocation((String)var1);
				}else if (var1 instanceof ResourceLocation){
					location = (ResourceLocation)var1;
				}else{
					return;
				}
			}else{
				location = new ResourceLocation(bg);
			}
			SimpleDetailedTexture texture = new SimpleDetailedTexture(location);
			FMLClientHandler.instance().getClient().getTextureManager().loadTexture(location, texture);
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(location);
			texture = (SimpleDetailedTexture) Minecraft.getMinecraft().getTextureManager().getTexture(location);
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			buffer.pos(pos[0], pos[0+1], pos[0+2]).tex(0, 0).color(red, green, blue, 1.0f).endVertex();
			buffer.pos(pos[3], pos[3+1], pos[3+2]).tex(texture.getImage().getWidth(), 0).color(red, green, blue, 1.0f).endVertex();
			buffer.pos(pos[6], pos[6+1], pos[6+2]).tex(texture.getImage().getWidth(), texture.getImage().getHeight()).color(red, green, blue, 1.0f).endVertex();
			buffer.pos(pos[9], pos[9+1], pos[9+2]).tex(0, texture.getImage().getHeight()).color(red, green, blue, 1.0f).endVertex();
		tessellator.draw();
		}
		
	}
	private static void renderText(Text text, FontRenderer font, Map<String, Object> var) {
		int color = 0;
		String key = text.getColor();
		if(text.isColorVariable()){
		if(var.containsKey(key)){
			Object var1 = var.get(key);
			if(var1 instanceof Integer){
				color = (int)var1;
			}else return;
		}}
		else if(text.isColorHexColor()){
			color = Integer.parseInt(text.getColor().replace("0x", ""), 16);
		}else if(text.isNoColor()) color = 0;
		font.drawString(text.getText(),text.getX(), text.getY(), color);
	}
}
