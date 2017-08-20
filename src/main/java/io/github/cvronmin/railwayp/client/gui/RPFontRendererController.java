package io.github.cvronmin.railwayp.client.gui;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import io.github.cvronmin.railwayp.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class RPFontRendererController {
	public static void replaceUnicodeGlyph(FontRenderer fontRenderer, ResourceLocation location){
		replaceGlyphSizes(fontRenderer, location);
		replaceUnicodePageLocation(fontRenderer, location);
	}
	public static void restoreUnicodeGlyph(FontRenderer fontRenderer){
		restoreUnicodePageLocation(fontRenderer);
		fontRenderer.onResourceManagerReload(FMLClientHandler.instance().getClient().getResourceManager());
	}
	private static void replaceUnicodePageLocation(FontRenderer fontRenderer, ResourceLocation location){
		ResourceLocation[] rls = new ResourceLocation[256];
		for (int i = 0; i < 256; i++) {
			rls[i] = new ResourceLocation(location.getResourceDomain(), location.getResourcePath() +"/"+ String.format("textures/font/unicode_page_%02x.png", new Object[] {Integer.valueOf(i)}));
		}
		try {
			Field upl = ReflectionHelper.findField(FontRenderer.class, "field_111274_c", "UNICODE_PAGE_LOCATIONS");
			upl.setAccessible(true);
			//Field modifiersField = Field.class.getDeclaredField("modifiers");
		    //modifiersField.setAccessible(true);
		   //modifiersField.setInt(upl, upl.getModifiers() & ~Modifier.FINAL);
		   EnumHelper.setFailsafeFieldValue(upl, null, rls);
			//upl.set(fontRenderer, rls);
		} catch (Exception e) {
			FMLLog.log(Reference.NAME, Level.WARN, e,"");
		}
	}
	private static void restoreUnicodePageLocation(FontRenderer fontRenderer){
		ResourceLocation[] rls = new ResourceLocation[256];
		try {
			Field upl = ReflectionHelper.findField(FontRenderer.class, "field_111274_c", "UNICODE_PAGE_LOCATIONS");
			upl.setAccessible(true);
		   EnumHelper.setFailsafeFieldValue(upl, null, rls);
		} catch (Exception e) {
			FMLLog.log(Reference.NAME, Level.WARN, e,"");
		}
	}
    private static void replaceGlyphSizes(FontRenderer fontRenderer, ResourceLocation location)
    {
        IResource iresource = null;

        try
        {
			Field gs = ReflectionHelper.findField(FontRenderer.class, "field_78287_e","glyphWidth");
			gs.setAccessible(true);
			byte[] tmp = new byte[65536];
            iresource = getResource(new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "/" + "font/glyph_sizes.bin"));
            iresource.getInputStream().read(tmp);
            EnumHelper.setFailsafeFieldValue(gs, fontRenderer, tmp);
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
        finally
        {
            IOUtils.closeQuietly(iresource);
        }
    }
    protected static IResource getResource(ResourceLocation location) throws IOException
    {
        return Minecraft.getMinecraft().getResourceManager().getResource(location);
    }
}
