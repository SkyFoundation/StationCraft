package tk.cvrunmin.railwayp.tileentity;

import java.util.List;

import org.apache.http.auth.AUTH;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWHPF extends TileEntityBanner{
	public final IChatComponent[] signText = new IChatComponent[] {new ChatComponentText(""), new ChatComponentText("")};
	    private int baseColor;
	    private boolean field_175119_g;
	    /** A list of all patterns stored on this banner. */
	    private List patternList;
	    /** A list of all the color values stored on this banner. */
	    private List colorList;
	    private int route;
	    private int routeColor;
	    private String routeColorEncoded;
	    /** 0 = Left, 1 = Right*/
	    private byte direction;
	    /** This is a String representation of this banners pattern and color lists, used for texture caching. */
	    private String patternResourceLocation;
		private boolean useCustomColor;
		private int existColor;
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

	            for (int i = 0; i < 2; ++i)
	            {
	            	if(nbttagcompound.hasKey("Text" + (i + 1))){
	                String s = nbttagcompound.getString("Text" + (i + 1));

	                try
	                {
	                    IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
	                    this.signText[i] = ichatcomponent;
	                }
	                catch (JsonParseException jsonparseexception)
	                {
	                    this.signText[i] = new ChatComponentText(s);
	                }
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

	        if(route > 0 && route < 10){
	        	compound.setInteger("Platform", this.route);
	        }
	        compound.setBoolean("UseCustomColor", useCustomColor);
	        if((routeColor >= 0x0 && routeColor < 0x1000000) && useCustomColor){
	        	compound.setString("LineColor", Integer.toHexString(this.routeColor));
	        }
	        else if ((existColor >= 0 && existColor < 15) && !useCustomColor) {
				compound.setInteger("ExistColor", existColor);
				compound.setString("LineColor", Integer.toHexString(EnumDyeColor.byDyeDamage(existColor).getMapColor().colorValue));
		}
	        compound.setInteger("C", this.routeColor);
	        if(direction >= 0 && direction <= 3){
	        	compound.setByte("Direction", direction);
	        }
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
	        this.route = compound.getInteger("Platform");
	        if(compound.hasKey("PlatformNumber")){
	            this.route = compound.getInteger("PlatformNumber");   	
	        }
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
	            String s = compound.getString("Text" + (i + 1));

	            try
	            {
	                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
	                this.signText[i] = ichatcomponent;
	            }
	            catch (JsonParseException jsonparseexception)
	            {
	                this.signText[i] = new ChatComponentText(s);
	            }
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
	     * Retrieves the amount of patterns stored on an ItemStack. If the tag does not exist this value will be 0.
	     *  
	     * @param stack The ItemStack which contains the NBTTagCompound data for banner patterns.
	     */
/*	    public static int getPatterns(ItemStack stack)
	    {
	        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
	        return nbttagcompound != null && nbttagcompound.hasKey("Patterns") ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
	    }*/

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
	                this.patternList.add(TileEntityWHPF.EnumBannerPattern.BASE);
	                this.colorList.add(EnumDyeColor.byDyeDamage(0));
	                this.patternResourceLocation = "b" + 0;
	                
	                if (this.checkGoodBanner()) {
	                    TileEntityWHPF.EnumBannerPattern enumbannerpattern = TileEntityWHPF.EnumBannerPattern.getPatternByID(Byte.toString(direction));

	                    if (enumbannerpattern != null)
	                    {
	                        this.patternList.add(enumbannerpattern);
	                        this.colorList.add(EnumDyeColor.byDyeDamage(15));
	                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 15;
	                    }
	                    enumbannerpattern = TileEntityWHPF.EnumBannerPattern.getPatternByID("bg_" + (this.direction % 2 == 0 ? this.direction : this.direction - 1));

	                    if (enumbannerpattern != null)
	                    {
	                        this.patternList.add(enumbannerpattern);
	                        this.colorList.add(EnumDyeColor.byDyeDamage(15));
	                        this.patternResourceLocation = this.patternResourceLocation + enumbannerpattern.getPatternID() + 15;
	                    }
	                    enumbannerpattern = TileEntityWHPF.EnumBannerPattern.getPatternByID(this.route + "_" + (this.direction % 2 == 0 ? this.direction : this.direction - 1));

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
			}
	            }
	        }
	    }
	    private boolean checkGoodBanner(){
	    	boolean flag = route > 0 && route < 10;
	    	boolean flag1 = routeColor >= 0x0 && routeColor < 0x1000000;
	    	boolean flag11 = existColor >= 0 && existColor < 16;
	    	boolean flag2 = direction >= 0 && direction <= 3;
	    	return flag && (flag1 || flag11) && flag2;
	    }
	    public byte getDirection(){
	    	return this.direction;
	    }
	    public static enum EnumBannerPattern
	    {
	        BASE("base", "b"),
	        AL("al", "0"),
	        AR("ar", "2"),
	        AUL("aul", "1"),
	        AUR("aur", "3"),
	        BG_L("bg_l", "bg_0"),
	        BG_R("bg_r", "bg_2"),
	        NO1_L("no1_l", "1_0"),
	        NO1_R("no1_r", "1_2"),
	        NO2_L("no2_l", "2_0"),
	        NO2_R("no2_r", "2_2"),
	        NO3_L("no3_l", "3_0"),
	        NO3_R("no3_r", "3_2"),
	        NO4_L("no4_l", "4_0"),
	        NO4_R("no4_r", "4_2"),
	        NO5_L("no5_l", "5_0"),
	        NO5_R("no5_r", "5_2"),
	        NO6_L("no6_l", "6_0"),
	        NO6_R("no6_r", "6_2"),
	        NO7_L("no7_l", "7_0"),
	        NO7_R("no7_r", "7_2"),
	        NO8_L("no8_l", "8_0"),
	        NO8_R("no8_r", "8_2"),
	        NO9_L("no9_l", "9_0"),
	        NO9_R("no9_r", "9_2");
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
	        public static TileEntityWHPF.EnumBannerPattern getPatternByID(String id)
	        {
	            TileEntityWHPF.EnumBannerPattern[] aenumbannerpattern = values();
	            int i = aenumbannerpattern.length;

	            for (int j = 0; j < i; ++j)
	            {
	                TileEntityWHPF.EnumBannerPattern enumbannerpattern = aenumbannerpattern[j];

	                if (enumbannerpattern.patternID.equals(id))
	                {
	                    return enumbannerpattern;
	                }
	            }

	            return null;
	        }
	    }
}
