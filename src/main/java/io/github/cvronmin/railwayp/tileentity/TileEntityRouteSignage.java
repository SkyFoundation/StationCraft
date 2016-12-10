package io.github.cvronmin.railwayp.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.cvronmin.railwayp.util.TextComponentUtil;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRouteSignage extends TileEntityBanner{
	//public final ITextComponent[] stationText = new ITextComponent[] {new TextComponentString(""), new TextComponentString("")};
	//public final ITextComponent[] nextText = new ITextComponent[] {new TextComponentString(""), new TextComponentString("")};
    private NBTTagList stations;
    private List<Station> stationList;
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

    public void setItemValues(ItemStack stack)
    {

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if (nbttagcompound.hasKey("Stations"))
            {
                this.stations = (NBTTagList)nbttagcompound.getTagList("Stations", 10).copy();
            }
            if(nbttagcompound.hasKey("LineColor", 8)){
            	routeColorEncoded = nbttagcompound.getString("LineColor");
            	decodeColor();
            }
            if (nbttagcompound.hasKey("Direction", 1)) {
				direction = nbttagcompound.getByte("Direction");
			}
            /*for (int i = 0; i < 2; ++i)
            {
            	if(nbttagcompound.hasKey("StationText" + (i + 1))){
                String s = nbttagcompound.getString("StationText" + (i + 1));


                    ITextComponent ichatcomponent = ITextComponent.Serializer.jsonToComponent(s);
                    this.stationText[i] = ichatcomponent;

            	}
            }
            for (int i = 0; i < 2; ++i)
            {
            	if(nbttagcompound.hasKey("NextText" + (i + 1))){
                String s = nbttagcompound.getString("NextText" + (i + 1));


                    ITextComponent ichatcomponent = ITextComponent.Serializer.jsonToComponent(s);
                    this.nextText[i] = ichatcomponent;

            	}
            }*/
        }
        this.stationList = null;
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.field_175119_g = true;
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if((routeColor >= 0x0 && routeColor < 0x1000000)){
        	compound.setString("LineColor", Integer.toHexString(this.routeColor));
        }
        if(direction >= 0 && direction <= 2){
        	compound.setByte("Direction", direction);
        }
        if (stations != null)
        {
        	compound.setTag("Stations", stations);
        }
        /*for (int i = 0; i < 2; ++i)
        {
            String s = ITextComponent.Serializer.componentToJson(this.stationText[i]);
            compound.setString("StationText" + (i + 1), s);
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = ITextComponent.Serializer.componentToJson(this.nextText[i]);
            compound.setString("NextText" + (i + 1), s);
        }*/
		return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
    	routeColorEncoded = compound.getString("LineColor");
    	decodeColor();
        this.direction = compound.getByte("Direction");
        this.stations = compound.getTagList("Stations", 10);
        /*for (int i = 0; i < 2; ++i)
        {
            String s = compound.getString("StationText" + (i + 1));
                ITextComponent ichatcomponent = ITextComponent.Serializer.jsonToComponent(s);
                this.stationText[i] = ichatcomponent;
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = compound.getString("NextText" + (i + 1));
                ITextComponent ichatcomponent = ITextComponent.Serializer.jsonToComponent(s);
                this.nextText[i] = ichatcomponent;
        }*/
        this.stationList = null;
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
        return new SPacketUpdateTileEntity(this.pos, 6, nbttagcompound);
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
    @SideOnly(Side.CLIENT)
    public List<Station> getStationList()
    {
        this.initializeBannerData();
        return this.stationList;
    }
    public static int getStations(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        return nbttagcompound != null && nbttagcompound.hasKey("Stations") ? nbttagcompound.getTagList("Stations", 10).tagCount() : 0;
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
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

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
        if (this.stationList == null || this.patternList == null || this.colorList == null || this.patternResourceLocation == null)
        {
            if (!this.field_175119_g)
            {
                this.patternResourceLocation = "";
            }
            else
            {
            	this.stationList = Lists.newArrayList();
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
                    for (int i = 0; i < stations.tagCount(); i++) {
                        NBTTagCompound nbttagcompound = this.stations.getCompoundTagAt(i);
                        stationList.add(Station.getStationFormCompound(nbttagcompound));
					}
                    /*enumbannerpattern = EnumUnifiedBannerPattern.SPOINT;

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
                    }*/
				}
            }
        }
    }
    public boolean checkGoodBanner(){
    	boolean flag1 = routeColor >= 0x0 && routeColor < 0x1000000;
    	boolean flag2 = direction >= 0 && direction <= 2;
    	boolean flag3 = stations.tagCount() >= 2;
    	return flag1 && flag2 && flag3;
    }
    public byte getDirection(){
    	return this.direction;
    }
	public int getRouteColor() {
		return routeColor;
	}
	public static class Station{
		public final ITextComponent[] stationName = new ITextComponent[]{new TextComponentString(""),new TextComponentString("")};
		private boolean isInterchangeStation;
		private boolean iAmHere;
		private int interchangeLineColor;
		private final ITextComponent[] interchangeLineName = new ITextComponent[]{new TextComponentString(""),new TextComponentString("")};
		public Station(String[] stationNameArray, boolean here, String... interlinename){
			stationName[0] = new TextComponentString(stationNameArray[0]);
			stationName[1] = new TextComponentString(stationNameArray[1]);
			iAmHere = here;
			if (interlinename != null) {
				if (interlinename.length >= 2) {
					isInterchangeStation = true;
					interchangeLineName[0] = new TextComponentString(interlinename[0]);
					interchangeLineName[1] = new TextComponentString(interlinename[1]);
				}
			}
		}
		public Station(Station station){
			stationName[0] = station.stationName[0];
			stationName[1] = station.stationName[1];
			iAmHere = station.iAmHere;
			isInterchangeStation = station.isInterchangeStation;
			interchangeLineName[0] = station.interchangeLineName[0];
			interchangeLineName[1] = station.interchangeLineName[1];
			interchangeLineColor = station.interchangeLineColor;
		}
		public Station(){
			
		}
		public boolean isInterchangeStation() {
			return isInterchangeStation;
		}
		public ITextComponent[] getInterchangeLineName() {
			return interchangeLineName;
		}
		public boolean amIHere(){
			return iAmHere;
		}
		public Station IAmHere(){
			iAmHere = true;
			return this;
		}
		public Station IAmNotHere(){
			iAmHere = false;
			return this;
		}
		public Station setIfIAmHere(boolean flag){
			iAmHere = flag;
			return this;
		}
		public static Station getStationFormCompound(NBTTagCompound compound){
			NBTTagList ss = compound.getTagList("Name", 8);
			boolean here = compound.getBoolean("Here");
			NBTTagCompound ls = compound.getCompoundTag("InterChangeLine");
			if(ls != null){
				NBTTagList list = ls.getTagList("Name", 8);
				if(list.tagCount() < 2)
					return new Station(new String[]{ss.getStringTagAt(0), ss.getStringTagAt(1)}, here);
				return new Station(new String[]{ss.getStringTagAt(0), ss.getStringTagAt(1)}, here,list.getStringTagAt(0),list.getStringTagAt(1)).setInterchangeLineColor(ls.getString("Color"));
			}
			return new Station(new String[]{ss.getStringTagAt(0), ss.getStringTagAt(1)}, here);
		}
		public NBTTagCompound writeStationToCompound(){
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagList ss = new NBTTagList();
			ss.appendTag(new NBTTagString(TextComponentUtil.getPureText(stationName[0])));
			ss.appendTag(new NBTTagString(TextComponentUtil.getPureText(stationName[1])));
			compound.setTag("Name", ss);
			compound.setBoolean("Here", iAmHere);
			if(!interchangeLineName[0].getUnformattedComponentText().isEmpty() | !interchangeLineName[1].getUnformattedComponentText().isEmpty()){
			NBTTagCompound ls = new NBTTagCompound();
				NBTTagList list = new NBTTagList();
				list.appendTag(new NBTTagString(TextComponentUtil.getPureText(interchangeLineName[0])));
				list.appendTag(new NBTTagString(TextComponentUtil.getPureText(interchangeLineName[1])));
				ls.setTag("Name", list);
				ls.setString("Color", Integer.toHexString(interchangeLineColor));
				compound.setTag("InterChangeLine", ls);
			}
				return compound;
		}
		public static NBTTagList writeStationsToTagList(List<Station> list){
			NBTTagList compound = new NBTTagList();
			for (Station station : list) {
				compound.appendTag(station.writeStationToCompound());
			}
			return compound;
		}
		public static List<Station> readStationsFromTagList(NBTTagList nbtTagList){
			List<Station> list = Lists.newArrayList();
			for (int i = 0; i < nbtTagList.tagCount(); i++) {
				list.add(getStationFormCompound(nbtTagList.getCompoundTagAt(i)));
			}
			return list;
		}
		public Station setStationName(String name1, String name2){
			this.stationName[0] = new TextComponentString(name1);
			this.stationName[1] = new TextComponentString(name2);
			return this;
		}
		public Station setInterchangeLineName(String name1, String name2){
			if(!name1.isEmpty() | !name2.isEmpty()) this.isInterchangeStation = true;
			else this.isInterchangeStation = false;
			this.interchangeLineName[0] = new TextComponentString(name1);
			this.interchangeLineName[1] = new TextComponentString(name2);
			return this;
		}
		public Station setInterchangeLineColor(int color){
			interchangeLineColor = color;
			return this;
		}
		public int getInterchangeLineColor(){
			return interchangeLineColor;
		}
		public Station setInterchangeLineColor(String color){
			return setInterchangeLineColor(decodeColor(color));
		}
	    private static int decodeColor(String colorEncoded){
	    	if(colorEncoded.length() <= 6)
	    	if(!colorEncoded.startsWith("0x")){
	    		try{
	    			return Integer.decode("0x" + colorEncoded);
	    		}
	    		catch(Exception e){
	    			return 0;
	    		}
	    	}
	    	else{
	    		try{
	    			return Integer.decode(colorEncoded);
	    		}
	    		catch(Exception e){
	    			return 0;
	    		}
	    	}
	    	return 0;
	    }
	}
	public void setData(byte dir, String color, List<Station> stations2) {
    	this.direction = dir;
    	this.routeColorEncoded = color;
    	this.stationList = stations2;
    	NBTTagList list=new NBTTagList();
    	for (Station station : stations2) {
			list.appendTag(station.writeStationToCompound());
		}
    	this.stations = list;
    	decodeColor();
	}
}
