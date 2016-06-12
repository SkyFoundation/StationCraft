package io.github.cvronmin.railwayp.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityNameBanner extends TileEntityBanner
{
	public final IChatComponent[] signText = new IChatComponent[] {new ChatComponentText(""), new ChatComponentText("")};
    private int color;
    private String colorEncoded;
    /** A list of all patterns stored on this banner. */
    private List<EnumUnifiedBannerPattern> patternList;
    /** A list of all the color values stored on this banner. */
    private List<Integer> colorList;
    private int stationColor;
    private String stationColorEncoded;
    /**
     * type of the sign
     * 0 : text only
     * 1 : horizontal name sign with model as base
     * 2 : horizontal name sign with model as base (former style)
     **/
    private int bannerType;
	private boolean patternDataSet;
	private String patternResourceLocation;

    private void decodeColor(){
    	if(colorEncoded.length() <= 6)
    	if(!colorEncoded.startsWith("0x")){
    		try{
    			color = Integer.decode("0x" + colorEncoded);
    		}
    		catch(Exception e){
    			color = 0;
    		}
    	}
    	else{
    		try{
    			color = Integer.decode(colorEncoded);
    		}
    		catch(Exception e){
    			color = 0;
    		}
    	}
    }
    private void decodeStationColor(){
    	if(stationColorEncoded.length() <= 6)
    	if(!stationColorEncoded.startsWith("0x")){
    		try{
    			stationColor = Integer.decode("0x" + stationColorEncoded);
    		}
    		catch(Exception e){
    			stationColor = 0;
    		}
    	}
    	else{
    		try{
    			stationColor = Integer.decode(stationColorEncoded);
    		}
    		catch(Exception e){
    			stationColor = 0;
    		}
    	}
    }
	@Override
    public void setItemValues(ItemStack stack)
    {

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if(nbttagcompound.hasKey("Color", 8)){
            	colorEncoded = nbttagcompound.getString("Color");
            	decodeColor();
            }
            if(nbttagcompound.hasKey("StationColor", 8)){
            	stationColorEncoded = nbttagcompound.getString("StationColor");
            	decodeStationColor();
            }
            if(nbttagcompound.hasKey("SignType", 3)){
            	bannerType = nbttagcompound.getInteger("SignType");
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
        if(color >= 0x0 && color < 0x1000000){
        	compound.setString("Color", Integer.toHexString(this.color));
        }
        if(stationColor >= 0x0 && stationColor < 0x1000000){
        	compound.setString("StationColor", Integer.toHexString(this.stationColor));
        }
        compound.setInteger("SignType", bannerType);
        for (int i = 0; i < 2; ++i)
        {
            String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }
    }
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
    	colorEncoded = compound.getString("Color");
    	decodeColor();
    	stationColorEncoded = compound.getString("StationColor");
    	decodeStationColor();
    	bannerType = compound.getInteger("SignType");
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

	public int getColor(){
		return color;
	}
	public int getStationColor(){
		return stationColor;
	}
	public int getSignType(){
		return bannerType;
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
    public String getPatternResourceLocation()
    {
        this.initializeBannerData();
        return this.patternResourceLocation;
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
                this.colorList.add(0xFFFFFF);
                this.patternResourceLocation = "b" + 0xFFFFFF;
                
                if (this.checkGoodBanner()) {
                	EnumUnifiedBannerPattern enumbannerpattern = EnumUnifiedBannerPattern.NAMEBANNER_BASE;

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(stationColor);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + stationColor;
                    }
                    if (bannerType != 2) {
						
					
                    enumbannerpattern = EnumUnifiedBannerPattern.NAMEBANNER_RIBBON;

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(0xFFFFFF);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 0xFFFFFF;
                    }
                    enumbannerpattern = EnumUnifiedBannerPattern.NAMEBANNER_RIBBON_ALT;

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(0x0);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 0;
                    }
                    }
				}
            }
        }
    }
    private boolean checkGoodBanner(){
    	boolean flag = color >= 0x0 && color < 0x1000000;
    	boolean flag1 = stationColor >= 0x0 && stationColor < 0x1000000;
    	return flag && flag1;
    }
	public void setData(int st, String sc, String c, String t1, String t2) {
		bannerType = st;
		stationColorEncoded = sc;
		colorEncoded = c;
		signText[0] = new ChatComponentText(t1);
		signText[1] = new ChatComponentText(t2);
		decodeColor();
		decodeStationColor();
	}
}