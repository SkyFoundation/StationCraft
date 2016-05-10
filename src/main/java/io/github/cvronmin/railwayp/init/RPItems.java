package io.github.cvronmin.railwayp.init;

import io.github.cvronmin.railwayp.Reference;
import io.github.cvronmin.railwayp.item.ItemNameBanner;
import io.github.cvronmin.railwayp.item.ItemPlatformBanner;
import io.github.cvronmin.railwayp.item.ItemRouteSignage;
import io.github.cvronmin.railwayp.item.ItemWHPF;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RPItems {
	public static final ItemPlatformBanner platform_banner = (ItemPlatformBanner)new ItemPlatformBanner().setUnlocalizedName("platform_banner");
	public static final ItemNameBanner name_banner = (ItemNameBanner)new ItemNameBanner().setUnlocalizedName("name_banner"); 
	public static final ItemRouteSignage route_sign = (ItemRouteSignage)new ItemRouteSignage().setUnlocalizedName("route_sign");
	public static final ItemWHPF whpf = (ItemWHPF) new ItemWHPF().setUnlocalizedName("whpf");
	public static void register(){
		registerItem(platform_banner, "platform_banner");
		registerItem(name_banner, "name_banner");
		registerItem(route_sign, "route_sign");
		registerItem(whpf, "whpf");
	}
	private static void registerItem(Item item, String name){
		GameRegistry.register(item.setRegistryName(new ResourceLocation(Reference.MODID, name)));
	}
}
