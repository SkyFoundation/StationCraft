package io.github.cvronmin.railwayp.client;


import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GLRenderUtil {
    public static void drawRect(double x1, double y1, double z1, double x2, double y2, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        float a = (color >> 24 & 255) / 255f,
                r = (color >> 16 & 255) / 255f,
                g = (color >> 8 & 255) / 255f,
                b = (color & 255) / 255f;
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.pos(x1, y2, z1).color(r, g, b, a).endVertex();
        buffer.pos(x2, y2, z1).color(r, g, b, a).endVertex();
        buffer.pos(x2, y1, z1).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

    public static void drawCircle(double x, double y, double z, int initialAngle, int finalAngle, float radius) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        for (int i = initialAngle; i <= finalAngle; i++) {
            double angle = 2 * Math.PI * i / 360;
            double deltaX = -Math.cos(angle);
            double deltaY = Math.sin(angle);
            buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
            buffer.pos(x - deltaX * radius, y + deltaY * radius, z).endVertex();
            angle = 2 * Math.PI * (i + 1) / 360;
            deltaX = Math.cos(angle);
            deltaY = Math.sin(angle);
            buffer.pos(x - deltaX * radius, y + deltaY * radius, z).endVertex();
            buffer.pos(x, y, z).endVertex();
            tessellator.draw();
        }
    }

    public static void drawCircle(double x, double y, double z, int color, int initialAngle, int finalAngle, float radius) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        float a = (color >> 24 & 255) / 255f,
                r = (color >> 16 & 255) / 255f,
                g = (color >> 8 & 255) / 255f,
                b = (color & 255) / 255f;
        for (int i = initialAngle; i <= finalAngle; i++) {
            double angle = 2 * Math.PI * i / 360;
            double deltaX = -Math.cos(angle);
            double deltaY = -Math.sin(angle);
            buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(x - deltaX * radius, y + deltaY * radius, z).color(r, g, b, a).endVertex();
            angle = 2 * Math.PI * (i + 1) / 360;
            deltaX = -Math.cos(angle);
            deltaY = -Math.sin(angle);
            buffer.pos(x - deltaX * radius, y + deltaY * radius, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            tessellator.draw();
        }
    }

    public static void drawCircle(double x, double y, double z, int color, int initialAngle, int finalAngle, float radius, Tessellator tessellator, VertexBuffer buffer) {
        float a = (color >> 24 & 255) / 255f,
                r = (color >> 16 & 255) / 255f,
                g = (color >> 8 & 255) / 255f,
                b = (color & 255) / 255f;
        for (int i = initialAngle; i <= finalAngle; i++) {
            double angle = 2 * Math.PI * i / 360;
            double deltaX = Math.cos(angle);
            double deltaY = Math.sin(angle);
            buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(x - deltaX * radius, y + deltaY * radius, z).color(r, g, b, a).endVertex();
            angle = 2 * Math.PI * (i + 1) / 360;
            deltaX = Math.cos(angle);
            deltaY = Math.sin(angle);
            buffer.pos(x - deltaX * radius, y + deltaY * radius, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            tessellator.draw();
        }
    }

    public static void drawPairTexts(FontRenderer fontrenderer, float offsetX, float offsetY, float offsetZ, float scale, int color, ITextComponent[] textComponents, int[] maxLengths, EnumAlignment alignment) {
        drawPairTexts(fontrenderer, offsetX, offsetY, offsetZ, scale * 2, scale, color, textComponents, maxLengths, alignment);
    }

    public static void drawPairTexts(FontRenderer fontrenderer, float offsetX, float offsetY, float offsetZ, float scale1, float scale2, int color, ITextComponent[] textComponents, int[] maxLengths, EnumAlignment alignment) {
        drawPairTexts(fontrenderer, offsetX, offsetY, offsetZ, scale1, scale2, 0, 0, color, textComponents, maxLengths, alignment);
    }

    public static void drawPairTexts(FontRenderer fontrenderer, float offsetX, float offsetY, float offsetZ, float scale1, float scale2, float modVal1, float modVal2, int color, ITextComponent[] textComponents, int[] maxLengths, EnumAlignment alignment) {
        if (textComponents.length < 1) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(offsetX, offsetY, offsetZ);
        GlStateManager.scale(scale1, -scale1, scale1);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * scale1);
        int xx;
        if (textComponents[0] != null) {
            ITextComponent ichatcomponent = textComponents[0];
            List<ITextComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false,
                    true);
            String s = list != null && list.size() > 0 ? list.get(0).getFormattedText() : "";

            int xxx = fontrenderer.getStringWidth(s);
            switch (alignment){
                default:
                case LEFT:
                    xx = 0;
                    break;
                case RIGHT:
                    xx = xxx;
                    break;
                case CENTER:
                    xx = xxx / 2;
                    break;
            }
            if (maxLengths.length > 0 && xxx > maxLengths[0]) {
                GlStateManager.scale(maxLengths[0] / (float) xxx, 1, 1);
                switch (alignment){
                    case LEFT:
                        xx *= (float)xxx / maxLengths[0];
                        break;
                    case RIGHT:
                        xx /= (float)xxx / maxLengths[0];
                        break;
                    default:
                    case CENTER:
                        break;
                }
            }
            fontrenderer.drawString(s, -xx, 0 - textComponents.length * 5 + modVal1, color, false);
        }
        GlStateManager.popMatrix();

        if (textComponents.length < 2) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(offsetX, offsetY, offsetZ);
        GlStateManager.scale(scale2, -scale2, scale2);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * scale2);
        if (textComponents[1] != null) {
            ITextComponent ichatcomponent = textComponents[1];
            List<ITextComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false,
                    true);
            String s = list.size() > 0 ? list.get(0).getFormattedText() : "";
            int xxx = fontrenderer.getStringWidth(s);
            switch (alignment){
                default:
                case LEFT:
                    xx = 0;
                    break;
                case RIGHT:
                    xx = xxx;
                    break;
                case CENTER:
                    xx = xxx / 2;
                    break;
            }
            if (maxLengths.length > 1 && xxx > maxLengths[1]) {
                GlStateManager.scale(maxLengths[1] / (float) xxx, 1, 1);
                switch (alignment){
                    case LEFT:
                        xx *= (float)xxx / maxLengths[1];
                        break;
                    case RIGHT:
                        xx /= (float)xxx / maxLengths[1];
                        break;
                    default:
                    case CENTER:
                        break;
                }
            }
            fontrenderer.drawString(s, -xx, 10 - textComponents.length * 5 + modVal2, color, false);
        }
        GlStateManager.popMatrix();
    }


    public enum EnumAlignment {LEFT, RIGHT, CENTER}
}
