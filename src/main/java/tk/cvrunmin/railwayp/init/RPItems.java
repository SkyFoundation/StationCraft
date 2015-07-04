package tk.cvrunmin.railwayp.init;

import tk.cvrunmin.railwayp.item.ItemNameBanner;
import tk.cvrunmin.railwayp.item.ItemPlatformBanner;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RPItems {
	public static final ItemPlatformBanner platform_banner = (ItemPlatformBanner)new ItemPlatformBanner().setUnlocalizedName("platform_banner");
	public static final ItemNameBanner name_banner = (ItemNameBanner)new ItemNameBanner().setUnlocalizedName("name_banner"); 
	public static void register(){
		registerItem(platform_banner, "platform_banner");
		registerItem(name_banner, "name_banner");
	}
	private static void registerItem(Item item, String name){
		GameRegistry.registerItem(item, name);
	}
}
