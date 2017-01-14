package io.github.cvronmin.railwayp.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SimpleDetailedTexture extends SimpleTexture {
    /** Access to the Logger, for all your logging needs. */
    private static final Logger LOG = LogManager.getLogger();
    private BufferedImage image;
	public SimpleDetailedTexture(ResourceLocation textureResourceLocation) {
		super(textureResourceLocation);
	}
	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        IResource iresource = null;

        try
        {
            iresource = resourceManager.getResource(this.textureLocation);
            image = TextureUtil.readBufferedImage(iresource.getInputStream());
            boolean flag = false;
            boolean flag1 = false;

            if (iresource.hasMetadata())
            {
                try
                {
                    TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");

                    if (texturemetadatasection != null)
                    {
                        flag = texturemetadatasection.getTextureBlur();
                        flag1 = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeexception)
                {
                    LOG.warn("Failed reading metadata of: {}", new Object[] {this.textureLocation, runtimeexception});
                }
            }

            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), image, flag, flag1);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }
	}
	public BufferedImage getImage(){return image;}
}
