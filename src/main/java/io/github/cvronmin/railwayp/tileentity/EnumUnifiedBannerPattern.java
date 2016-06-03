package io.github.cvronmin.railwayp.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumUnifiedBannerPattern {
	BASE("base", "b"),
    RIBBON("ribbon", "rb"),
    LONGRIBBON("longribbon", "lrb"),
    LAL("lal", "0"),
    LAR("lar", "2"),
    SPOINT("spoint", "station"),
    AL("al", "0"),
    AR("ar", "2"),
    AUL("aul", "1"),
    AUR("aur", "3"),
    BG_L("bg_l", "bg_0"),
    BG_R("bg_r", "bg_2"),
    NO1_L("no1_l", "1_0"),
    NO1_M("no1_m", "1_1"),
    NO1_R("no1_r", "1_2"),
    NO2_L("no2_l", "2_0"),
    NO2_M("no2_m", "2_1"),
    NO2_R("no2_r", "2_2"),
    NO3_L("no3_l", "3_0"),
    NO3_M("no3_m", "3_1"),
    NO3_R("no3_r", "3_2"),
    NO4_L("no4_l", "4_0"),
    NO4_M("no4_m", "4_1"),
    NO4_R("no4_r", "4_2"),
    NO5_L("no5_l", "5_0"),
    NO5_M("no5_m", "5_1"),
    NO5_R("no5_r", "5_2"),
    NO6_L("no6_l", "6_0"),
    NO6_M("no6_m", "6_1"),
    NO6_R("no6_r", "6_2"),
    NO7_L("no7_l", "7_0"),
    NO7_M("no7_m", "7_1"),
    NO7_R("no7_r", "7_2"),
    NO8_L("no8_l", "8_0"),
    NO8_M("no8_m", "8_1"),
    NO8_R("no8_r", "8_2"),
    NO9_L("no9_l", "9_0"),
    NO9_M("no9_m", "9_1"),
    NO9_R("no9_r", "9_2");
    /** The name used to represent this pattern. */
    private String patternName;
    /** A short string used to represent the pattern. */
    private String patternID;
    /** An array of three strings where each string represents a layer in the crafting grid. Goes from top to bottom. */
    private String[] craftingLayers;
    /** An ItemStack used to apply this pattern. */
    private ItemStack patternCraftingStack;

    private EnumUnifiedBannerPattern(String name, String id)
    {
        this.craftingLayers = new String[3];
        this.patternName = name;
        this.patternID = id;
    }

    private EnumUnifiedBannerPattern(String name, String id, ItemStack craftingItem)
    {
        this(name, id);
        this.patternCraftingStack = craftingItem;
    }

    private EnumUnifiedBannerPattern(String name, String id, String craftingTop, String craftingMid, String craftingBot)
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
    public static EnumUnifiedBannerPattern getPatternByID(String id)
    {
        EnumUnifiedBannerPattern[] aenumbannerpattern = values();
        int i = aenumbannerpattern.length;

        for (int j = 0; j < i; ++j)
        {
            EnumUnifiedBannerPattern enumbannerpattern = aenumbannerpattern[j];

            if (enumbannerpattern.patternID.equals(id))
            {
                return enumbannerpattern;
            }
        }

        return null;
    }
}
