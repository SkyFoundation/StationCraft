package io.github.cvronmin.railwayp.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPlatformBanner extends TileEntityBanner
{
	public final IChatComponent[] signText = new IChatComponent[] {new ChatComponentText(""), new ChatComponentText("")};
    public boolean shouldExtend;
    private boolean patternDataSet;
    /** A list of all patterns stored on this banner. */
    private List<EnumUnifiedBannerPattern> patternList;
    /** A list of all the color values stored on this banner. */
    private List<Integer> colorList;
    private int route;
    private int routeColor;
    private String routeColorEncoded;
    /** 0 = Left, 1 = no direction, 2 = Right*/
    private byte direction;
    /** This is a String representation of this banners pattern and color lists, used for texture caching. */
    private String patternResourceLocation;
    private void decodeColor(){
    	if(routeColorEncoded.length() <= 6)
    	if(!routeColorEncoded.startsWith("0x")){
    		try{
    			routeColor = Integer.decode("0x" + routeColorEncoded);
    		}
    		catch(Exception e){
    			routeColor = 0;
    		}
    	}
    	else{
    		try{
    			routeColor = Integer.decode(routeColorEncoded);
    		}
    		catch(Exception e){
    			routeColor = 0;
    		}
    	}
    }

    public void setItemValues(ItemStack stack)
    {
    	if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if(nbttagcompound.hasKey("LineColor", 8)){
            	routeColorEncoded = nbttagcompound.getString("LineColor");
            	decodeColor();
            }
            if((nbttagcompound.hasKey("PlatformNumber", 3) || nbttagcompound.hasKey("Platform", 3))){
            	if(nbttagcompound.hasKey("PlatformNumber")){
            	route = nbttagcompound.getInteger("PlatformNumber");
            	}
            	else {
                	route = nbttagcompound.getInteger("Platform");	
				}
            }
            if (nbttagcompound.hasKey("Direction", 1)) {
				direction = nbttagcompound.getByte("Direction");
			}
            if(nbttagcompound.hasKey("ShouldExtend")){
            	shouldExtend = nbttagcompound.getBoolean("ShouldExtend");
            }
            for (int i = 0; i < 2; ++i)
            {
            	if(nbttagcompound.hasKey("Text" + (i + 1))){
                String s = nbttagcompound.getString("Text" + (i + 1));


                    IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                    this.signText[i] = ichatcomponent;

            	}
            }
        }

        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.patternDataSet = true;
    }
    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if(route > 0 && route < 10){
        	compound.setInteger("Platform", this.route);
        }
        if((routeColor >= 0x0 && routeColor < 0x1000000)){
        	compound.setString("LineColor", Integer.toHexString(this.routeColor));
        }
        if(direction >= 0 && direction <= 2){
        	compound.setByte("Direction", direction);
        }
        compound.setBoolean("ShouldExtend", shouldExtend);
        for (int i = 0; i < 2; ++i)
        {
            String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }
    }
    public static void setBaseColorAndPatterns(NBTTagCompound compound, int baseColorIn, NBTTagList patternsIn)
    {
        compound.setInteger("Base", baseColorIn);

        if (patternsIn != null)
        {
            compound.setTag("Patterns", patternsIn);
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.route = compound.getInteger("Platform");
        if(compound.hasKey("PlatformNumber")){
            this.route = compound.getInteger("PlatformNumber");   	
        }
    	routeColorEncoded = compound.getString("LineColor");
    	decodeColor();
        this.direction = compound.getByte("Direction");
        this.shouldExtend = compound.getBoolean("ShouldExtend");
        for (int i = 0; i < 2; ++i)
        {
            String s = compound.getString("Text" + (i + 1));

                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                this.signText[i] = ichatcomponent;
        }
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.patternDataSet = true;
    }

    /**
     * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
     * server to the client easily. For example this is used by signs to synchronise the text to be displayed.
     */
    public Packet<?> getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
    }

    public static int getBaseColor(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        return nbttagcompound != null && nbttagcompound.hasKey("Base") ? nbttagcompound.getInteger("Base") : stack.getMetadata();
    }

    /**
     * Retrieves the amount of patterns stored on an ItemStack. If the tag does not exist this value will be 0.
     */
    public static int getPatterns(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        return nbttagcompound != null && nbttagcompound.hasKey("Patterns") ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
    }
    /**
     * Retrieves the list of patterns for this tile entity. The banner data will be initialized/refreshed before this
     * happens.
     */
    @SideOnly(Side.CLIENT)
    public List getPatternList()
    {
        this.initializeBannerData();
        return this.patternList;
    }
    /**
     * Retrieves the list of colors for this tile entity. The banner data will be initialized/refreshed before this
     * happens.
     */
    @SideOnly(Side.CLIENT)
    public List getColorList()
    {
        this.initializeBannerData();
        return this.colorList;
    }

    public IChatComponent[] getSignText() {
		return signText;
	}

	public int getRoute() {
		return route;
	}

	public int getRouteColor() {
		return routeColor;
	}

	@SideOnly(Side.CLIENT)
    public String getPatternResourceLocation()
    {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }

    /**
     * Removes all the banner related data from a provided instance of ItemStack.
     *  
     * @param stack The instance of an ItemStack which will have the relevant nbt tags removed.
     */
    public static void removeBannerData(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9))
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);

            if (nbttaglist.tagCount() > 0)
            {
                nbttaglist.removeTag(nbttaglist.tagCount() - 1);

                if (nbttaglist.hasNoTags())
                {
                    stack.getTagCompound().removeTag("BlockEntityTag");

                    if (stack.getTagCompound().hasNoTags())
                    {
                        stack.setTagCompound((NBTTagCompound)null);
                    }
                }
            }
        }
    }
    /**
     * Establishes all of the basic properties for the banner. This will also apply the data from the tile entities nbt
     * tag compounds.
     */
    @SideOnly(Side.CLIENT)
    private void initializeBannerData()
    {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null)
        {
            if (!this.patternDataSet)
            {
                this.patternResourceLocation = "";
            }
            else
            {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                this.patternList.add(EnumUnifiedBannerPattern.BASE);
                this.colorList.add(EnumDyeColor.byDyeDamage(15).getMapColor().colorValue);
                this.patternResourceLocation = "b" + 0;
                
                if (this.checkGoodBanner()) {
                    EnumUnifiedBannerPattern enumbannerpattern = EnumUnifiedBannerPattern.RIBBON;

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        	this.colorList.add(this.routeColor);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + this.routeColor;
                    }
                    enumbannerpattern = direction == 0  ? EnumUnifiedBannerPattern.AL : (direction == 2 ? EnumUnifiedBannerPattern.AR : null);

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(0);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 0;
                    }
                    enumbannerpattern = EnumUnifiedBannerPattern.getPatternByID(this.route + "_" + this.direction);

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        	this.colorList.add(this.routeColor);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + this.routeColor;
                    }
				}
            }
        }
    }
    public static void func_184248_a(ItemStack p_184248_0_, EnumDyeColor p_184248_1_)
    {
        NBTTagCompound nbttagcompound = p_184248_0_.getSubCompound("BlockEntityTag", true);
        nbttagcompound.setInteger("Base", p_184248_1_.getDyeDamage());
    }

    private boolean checkGoodBanner(){
    	boolean flag = route > 0 && route < 10;
    	boolean flag1 = routeColor >= 0x0 && routeColor < 0x1000000;
    	boolean flag2 = direction >= 0 && direction <= 2;
    	return flag && flag1 && flag2;
    }
    public byte getDirection(){
    	return this.direction;
    }
    public void setData(int pn, byte dir, String color, String t1, String t2){
    	this.route = pn;
    	this.direction = dir;
    	this.routeColorEncoded = color;
    	this.signText[0] = new ChatComponentText(t1);
    	this.signText[1] = new ChatComponentText(t2);
    	decodeColor();
    }
}