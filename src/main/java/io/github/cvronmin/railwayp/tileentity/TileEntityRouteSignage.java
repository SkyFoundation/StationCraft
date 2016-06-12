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

public class TileEntityRouteSignage extends TileEntityBanner{
	public final IChatComponent[] stationText = new IChatComponent[] {new ChatComponentText(""), new ChatComponentText("")};
	public final IChatComponent[] nextText = new IChatComponent[] {new ChatComponentText(""), new ChatComponentText("")};
//    private NBTTagList stations;
    private int baseColor;
    private boolean field_175119_g;
    /** A list of all patterns stored on this banner. */
    private List<EnumUnifiedBannerPattern> patternList;
    /** A list of all the color values stored on this banner. */
    private List<Integer> colorList;
    private int routeColor;
	private String routeColorEncoded;
    /** 0 = Left, 1 = Right*/
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
    @Override
    public void setItemValues(ItemStack stack)
    {

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
/*            if (nbttagcompound.hasKey("Stations"))
            {
                this.stations = (NBTTagList)nbttagcompound.getTagList("Stations", 10).copy();
            }*/
            if(nbttagcompound.hasKey("LineColor", 8)){
            	routeColorEncoded = nbttagcompound.getString("LineColor");
            	decodeColor();
            }
            if (nbttagcompound.hasKey("Direction", 1)) {
				direction = nbttagcompound.getByte("Direction");
			}
            for (int i = 0; i < 2; ++i)
            {
            	if(nbttagcompound.hasKey("StationText" + (i + 1))){
                String s = nbttagcompound.getString("StationText" + (i + 1));


                    IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                    this.stationText[i] = ichatcomponent;

            	}
            }
            for (int i = 0; i < 2; ++i)
            {
            	if(nbttagcompound.hasKey("NextText" + (i + 1))){
                String s = nbttagcompound.getString("NextText" + (i + 1));


                    IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                    this.nextText[i] = ichatcomponent;

            	}
            }
        }

        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.field_175119_g = true;
    }
    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if((routeColor >= 0x0 && routeColor < 0x1000000)){
        	compound.setString("LineColor", Integer.toHexString(this.routeColor));
        }
        if(direction >= 0 && direction <= 2){
        	compound.setByte("Direction", direction);
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = IChatComponent.Serializer.componentToJson(this.stationText[i]);
            compound.setString("StationText" + (i + 1), s);
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = IChatComponent.Serializer.componentToJson(this.nextText[i]);
            compound.setString("NextText" + (i + 1), s);
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
    	routeColorEncoded = compound.getString("LineColor");
    	decodeColor();
        this.direction = compound.getByte("Direction");
        for (int i = 0; i < 2; ++i)
        {
            String s = compound.getString("StationText" + (i + 1));
                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                this.stationText[i] = ichatcomponent;
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = compound.getString("NextText" + (i + 1));
                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                this.nextText[i] = ichatcomponent;
        }
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.field_175119_g = true;
    }

    /**
     * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
     * server to the client easily. For example this is used by signs to synchronise the text to be displayed.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
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

    @SideOnly(Side.CLIENT)
    public String func_175116_e()
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
            if (!this.field_175119_g)
            {
                this.patternResourceLocation = "";
            }
            else
            {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                this.patternList.add(EnumUnifiedBannerPattern.BASE);
                this.colorList.add(EnumDyeColor.byDyeDamage(15).getMapColor().colorValue);
                this.patternResourceLocation = "b" + this.baseColor;
                
                if (this.checkGoodBanner()) {
                    EnumUnifiedBannerPattern enumbannerpattern = EnumUnifiedBannerPattern.LONGRIBBON;

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(this.routeColor);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + this.routeColor;
                    }
                    enumbannerpattern = EnumUnifiedBannerPattern.SPOINT;

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(0);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 0;
                    }
                    enumbannerpattern = direction == 0  ? EnumUnifiedBannerPattern.LAL : (direction == 2 ? EnumUnifiedBannerPattern.LAR : null);
                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(0);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 0;
                    }
				}
            }
        }
    }
    private boolean checkGoodBanner(){
    	boolean flag1 = routeColor >= 0x0 && routeColor < 0x1000000;
    	boolean flag2 = direction >= 0 && direction <= 2;
    	return flag1 && flag2;
    }
    public byte getDirection(){
    	return this.direction;
    }
}
