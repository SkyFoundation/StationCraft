package io.github.cvronmin.railwayp.client.renderer.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayeredCustomColorMaskTexture extends AbstractTexture {
    /** Access to the Logger, for all your logging needs. */
    private static final Logger LOG = LogManager.getLogger();
    /** The location of the texture. */
    private final ResourceLocation textureLocation;
    private final List textureList;
    private final List<Integer> colorList;

    public LayeredCustomColorMaskTexture(ResourceLocation textureLocationIn, List p_i46101_2_, List<Integer> p_i46101_3_)
    {
        this.textureLocation = textureLocationIn;
        this.textureList = p_i46101_2_;
        this.colorList = p_i46101_3_;
    }
	@Override
    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        this.deleteGlTexture();
        IResource iresource = null;
        BufferedImage bufferedimage;
        label6:
        {
            try
            {
                iresource = resourceManager.getResource(this.textureLocation);
                BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());
                int i = bufferedimage1.getType();

                if (i == 0)
                {
                    i = 6;
                }

                bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
                Graphics graphics = bufferedimage.getGraphics();
                graphics.drawImage(bufferedimage1, 0, 0, null);
                int j = 0;

                while (true)
                {
                    if (j >= 17 || j >= this.textureList.size() || j >= this.colorList.size())
                    {
                        break label6;
                    }

                    IResource iresource1 = null;

                    try
                    {
                        String s = (String)this.textureList.get(j);
                        //MapColor mapcolor = ((EnumDyeColor)this.c.get(j)).getMapColor();
                        int color = this.colorList.get(j);
                        if (s != null)
                        {
                            iresource1 = resourceManager.getResource(new ResourceLocation(s));
                            BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(iresource1.getInputStream());

                            if (bufferedimage2.getWidth() == bufferedimage.getWidth() && bufferedimage2.getHeight() == bufferedimage.getHeight() && bufferedimage2.getType() == 6)
                            {
                                for (int k = 0; k < bufferedimage2.getHeight(); ++k)
                                {
                                    for (int l = 0; l < bufferedimage2.getWidth(); ++l)
                                    {
                                        int i1 = bufferedimage2.getRGB(l, k);

                                        if ((i1 & -16777216) != 0)
                                        {
                                            int j1 = (i1 & 16711680) << 8 & -16777216;
                                            int k1 = bufferedimage1.getRGB(l, k);
                                            int l1 = MathHelper.multiplyColor(k1, color) & 16777215;
                                            bufferedimage2.setRGB(l, k, j1 | l1);
                                        }
                                    }
                                }

                                bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, null);
                            }
                        }
                    }
                    finally
                    {
                        IOUtils.closeQuietly(iresource1);
                    }

                    ++j;
                }
            }
            catch (IOException ioexception)
            {
                LOG.error("Couldn\'t load layered image", ioexception);
            }
            finally
            {
                IOUtils.closeQuietly(iresource);
            }

            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
        
    }


}
