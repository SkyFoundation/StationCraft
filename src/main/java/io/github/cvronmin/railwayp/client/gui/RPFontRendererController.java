package io.github.cvronmin.railwayp.client.gui;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

public class RPFontRendererController {
	public static void replaceUnicodeGlyph(FontRenderer fontRenderer, ResourceLocation location){
		replaceGlyphSizes(fontRenderer, location);
		replaceUnicodePageLocation(fontRenderer, location);
	}
	public static void restoreUnicodeGlyph(FontRenderer fontRenderer){
		fontRenderer.onResourceManagerReload(null);
	}
	private static void replaceUnicodePageLocation(FontRenderer fontRenderer, ResourceLocation location){
		ResourceLocation[] rls = new ResourceLocation[256];
		for (int i = 0; i < 256; i++) {
			rls[i] = new ResourceLocation(location.getResourceDomain(), location.getResourcePath() +"/"+ String.format("textures/font/unicode_page_%02x.png", new Object[] {Integer.valueOf(i)}));
		}
		try {
			Field upl = fontRenderer.getClass().getDeclaredField("field_111274_c");
			upl.setAccessible(true);
			upl.set(fontRenderer, rls);
		} catch (Exception e) {
			FMLLog.log(Reference.NAME, Level.WARN, e,"");
		}
	}
    private static void replaceGlyphSizes(FontRenderer fontRenderer, ResourceLocation location)
    {
        IResource iresource = null;

        try
        {
			Field gs = fontRenderer.getClass().getDeclaredField("field_78287_e");
			gs.setAccessible(true);
			byte[] tmp = new byte[65536];
            iresource = getResource(new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "/" + "font/glyph_sizes.bin"));
            iresource.getInputStream().read(tmp);
            gs.set(fontRenderer, tmp);
        }
        catch (IOException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }
    }
    protected static IResource getResource(ResourceLocation location) throws IOException
    {
        return Minecraft.getMinecraft().getResourceManager().getResource(location);
    }
}
