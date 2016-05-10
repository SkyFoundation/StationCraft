package io.github.cvronmin.railwayp.client.renderer.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
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
    private final List colorList;

    public LayeredCustomColorMaskTexture(ResourceLocation textureLocationIn, List p_i46101_2_, List p_i46101_3_)
    {
        this.textureLocation = textureLocationIn;
        this.textureList = p_i46101_2_;
        this.colorList = p_i46101_3_;
    }
	@Override
    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        this.deleteGlTexture();
        BufferedImage bufferedimage;

        try
        {
            BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(resourceManager.getResource(this.textureLocation).getInputStream());
            int i = bufferedimage1.getType();

            if (i == 0)
            {
                i = 6;
            }

            bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
            Graphics graphics = bufferedimage.getGraphics();
            graphics.drawImage(bufferedimage1, 0, 0, (ImageObserver)null);

            for (int j = 0; j < this.textureList.size() && j < this.colorList.size(); ++j)
            {
                String s = (String)this.textureList.get(j);
//                MapColor mapcolor = ((EnumDyeColor)this.field_174950_i.get(j)).getMapColor();
                int color;
                if(this.colorList.get(j) instanceof EnumDyeColor){
                	color = ((EnumDyeColor)this.colorList.get(j)).getMapColor().colorValue;
                }
                else{
                color = (Integer) this.colorList.get(j);
                }
                if (s != null)
                {
                    InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
                    BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(inputstream);

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

                        bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, (ImageObserver)null);
                    }
                }
            }
        }
        catch (IOException ioexception)
        {
            LOG.error("Couldn\'t load layered image", ioexception);
            return;
        }

        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }


}
