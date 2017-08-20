package io.github.cvronmin.railwayp.init;

import io.github.cvronmin.railwayp.Reference;
import io.github.cvronmin.railwayp.item.ItemEditor;
import io.github.cvronmin.railwayp.item.ItemNameBanner;
import io.github.cvronmin.railwayp.item.ItemPlatformBanner;
import io.github.cvronmin.railwayp.item.ItemRouteSignage;
import io.github.cvronmin.railwayp.item.ItemWHPF;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RPItems {
	public static final ItemPlatformBanner platform_banner = (ItemPlatformBanner)new ItemPlatformBanner().setUnlocalizedName("platform_banner");
	public static final ItemNameBanner name_banner = (ItemNameBanner)new ItemNameBanner().setUnlocalizedName("name_banner"); 
	public static final ItemRouteSignage route_sign = (ItemRouteSignage)new ItemRouteSignage().setUnlocalizedName("route_sign");
	public static final ItemWHPF whpf = (ItemWHPF) new ItemWHPF().setUnlocalizedName("whpf");
	public static final ItemEditor EDITOR = (ItemEditor) new ItemEditor().setUnlocalizedName("editor");
	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event){
		registerItem(platform_banner, "platform_banner",event.getRegistry());
		registerItem(name_banner, "name_banner",event.getRegistry());
		registerItem(route_sign, "route_sign",event.getRegistry());
		registerItem(whpf, "whpf",event.getRegistry());
		registerItem(EDITOR, "editor",event.getRegistry());
	}
	private static void registerItem(Item item, String name, IForgeRegistry<Item> registry){
		registry.register(item.setRegistryName(new ResourceLocation(Reference.MODID, name)));
	}
}
