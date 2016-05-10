package io.github.cvronmin.railwayp.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRouteSignage extends TileEntityBanner{
	public final ITextComponent[] stationText = new ITextComponent[] {new TextComponentString(""), new TextComponentString("")};
	public final ITextComponent[] nextText = new ITextComponent[] {new TextComponentString(""), new TextComponentString("")};
//    private NBTTagList stations;
	private boolean useCustomColor;
    private int baseColor;
    private boolean field_175119_g;
    /** A list of all patterns stored on this banner. */
    private List patternList;
    /** A list of all the color values stored on this banner. */
    private List colorList;
    private int routeColor;
    private int existColor;
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
            if(nbttagcompound.hasKey("UseCustomColor")){
            	useCustomColor = nbttagcompound.getBoolean("UseCustomColor");
            }
            if(nbttagcompound.hasKey("LineColor", 8) && useCustomColor){
            	routeColorEncoded = nbttagcompound.getString("LineColor");
            	decodeColor();
            }
            else if (nbttagcompound.hasKey("ExistColor", 3) && !useCustomColor) {
				existColor = nbttagcompound.getInteger("ExistColor");
			}
            if (nbttagcompound.hasKey("Direction", 1)) {
				direction = nbttagcompound.getByte("Direction");
			}
            for (int i = 0; i < 2; ++i)
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
        compound.setBoolean("UseCustomColor", useCustomColor);
        if((routeColor >= 0x0 && routeColor < 0x1000000) && useCustomColor){
        	compound.setString("LineColor", Integer.toHexString(this.routeColor));
        }
        else if ((existColor >= 0 && existColor < 15) && !useCustomColor) {
			compound.setInteger("ExistColor", existColor);
			compound.setString("LineColor", Integer.toHexString(EnumDyeColor.byDyeDamage(existColor).getMapColor().colorValue));
		}
        if(direction >= 0 && direction <= 2){
        	compound.setByte("Direction", direction);
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = ITextComponent.Serializer.componentToJson(this.stationText[i]);
            compound.setString("StationText" + (i + 1), s);
        }
        for (int i = 0; i < 2; ++i)
        {
            String s = ITextComponent.Serializer.componentToJson(this.nextText[i]);
            compound.setString("NextText" + (i + 1), s);
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        useCustomColor = compound.getBoolean("UseCustomColor");
        if(useCustomColor){
    	routeColorEncoded = compound.getString("LineColor");
    	decodeColor();
        }
        else {
			existColor = compound.getInteger("ExistColor");
		}
        this.direction = compound.getByte("Direction");
        for (int i = 0; i < 2; ++i)
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
                this.patternList.add(TileEntityRouteSignage.EnumBannerPattern.BASE);
                this.colorList.add(EnumDyeColor.byDyeDamage(15));
                this.patternResourceLocation = "b" + this.baseColor;
                
                if (this.checkGoodBanner()) {
                    TileEntityRouteSignage.EnumBannerPattern enumbannerpattern = TileEntityRouteSignage.EnumBannerPattern.getPatternByID("rb");

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        if(this.useCustomColor){
                        	this.colorList.add(this.routeColor);
                        }
                        else {
                        	this.colorList.add(EnumDyeColor.byDyeDamage(this.existColor));	
						}
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + (useCustomColor ? this.routeColor : this.existColor);
                    }
                    enumbannerpattern = TileEntityRouteSignage.EnumBannerPattern.getPatternByID("station");

                    if (enumbannerpattern != null)
                    {
                        this.patternList.add(enumbannerpattern);
                        this.colorList.add(0);
                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 0;
                    }
                    enumbannerpattern = TileEntityRouteSignage.EnumBannerPattern.getPatternByID(Byte.toString(direction));

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
    	boolean flag11 = existColor >= 0 && existColor < 16;
    	boolean flag2 = direction >= 0 && direction <= 2;
    	return (flag1 || flag11) && flag2;
    }
    public byte getDirection(){
    	return this.direction;
    }
    public static enum EnumBannerPattern
    {
        BASE("base", "b"),
        LONGRIBBON("longribbon", "rb"),
        LAL("lal", "0"),
        LAR("lar", "2"),
        SPOINT("spoint", "station");
        /** The name used to represent this pattern. */
        private String patternName;
        /** A short string used to represent the pattern. */
        private String patternID;
        /** An array of three strings where each string represents a layer in the crafting grid. Goes from top to bottom. */
        private String[] craftingLayers;
        /** An ItemStack used to apply this pattern. */
        private ItemStack patternCraftingStack;

        private EnumBannerPattern(String name, String id)
        {
            this.craftingLayers = new String[3];
            this.patternName = name;
            this.patternID = id;
        }

        private EnumBannerPattern(String name, String id, ItemStack craftingItem)
        {
            this(name, id);
            this.patternCraftingStack = craftingItem;
        }

        private EnumBannerPattern(String name, String id, String craftingTop, String craftingMid, String craftingBot)
        {
            this(name, id);
            this.craftingLayers[0] = craftingTop;
            this.craftingLayers[1] = craftingMid;
            this.craftingLayers[2] = craftingBot;
        }

        /**
         * Retrieves the name used to represent this pattern.
         */
        @SideOnly(Side.CLIENT)
        public String getPatternName()
        {
            return this.patternName;
        }

        /**
         * Retrieves the short string used to represent this pattern.
         */
        public String getPatternID()
        {
            return this.patternID;
        }

        /**
         * Retrieves the string array which represents the associated crafting recipe for this banner effect. The first
         * object in the array is the top layer while the second is middle and third is last.
         */
        public String[] getCraftingLayers()
        {
            return this.craftingLayers;
        }

        /**
         * Checks to see if this pattern has a valid crafting stack, or if the top crafting layer is not null.
         */
        public boolean hasValidCrafting()
        {
            return this.patternCraftingStack != null || this.craftingLayers[0] != null;
        }

        /**
         * Checks to see if this pattern has a specific ItemStack associated with it's crafting.
         */
        public boolean hasCraftingStack()
        {
            return this.patternCraftingStack != null;
        }

        /**
         * Retrieves the ItemStack associated with the crafting of this pattern.
         */
        public ItemStack getCraftingStack()
        {
            return this.patternCraftingStack;
        }

        /**
         * Retrieves an instance of a banner pattern by its short string id.
         *  
         * @param id A short string to represent this pattern. (For example, bts will give an instance of
         * TRIANGLES_BOTTOM)
         */
        @SideOnly(Side.CLIENT)
        public static TileEntityRouteSignage.EnumBannerPattern getPatternByID(String id)
        {
            TileEntityRouteSignage.EnumBannerPattern[] aenumbannerpattern = values();
            int i = aenumbannerpattern.length;

            for (int j = 0; j < i; ++j)
            {
                TileEntityRouteSignage.EnumBannerPattern enumbannerpattern = aenumbannerpattern[j];

                if (enumbannerpattern.patternID.equals(id))
                {
                    return enumbannerpattern;
                }
            }

            return null;
        }
    }
}
