package io.github.cvronmin.railwayp.init;

import io.github.cvronmin.railwayp.Reference;
import io.github.cvronmin.railwayp.block.BlockColorful;
import io.github.cvronmin.railwayp.block.BlockNameBanner;
import io.github.cvronmin.railwayp.block.BlockPlatformBanner;
import io.github.cvronmin.railwayp.block.BlockPlatformDoor;
import io.github.cvronmin.railwayp.block.BlockPlatformGlass;
import io.github.cvronmin.railwayp.block.BlockRailNoticer;
import io.github.cvronmin.railwayp.block.BlockRouteSignage;
import io.github.cvronmin.railwayp.block.BlockWHPF;
import io.github.cvronmin.railwayp.block.BlockPlatformDoor.Base;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RPBlocks {
	@ObjectHolder("railwayp:wall_platform_banner")
	public static final BlockPlatformBanner wall_platform_banner = (BlockPlatformBanner)(new BlockPlatformBanner.BlockBannerHanging()).setUnlocalizedName("banner");
	@ObjectHolder("railwayp:wall_name_banner")
	public static final BlockNameBanner wall_name_banner = (BlockNameBanner)new BlockNameBanner().setUnlocalizedName("banner"); 
	public static final BlockRouteSignage wall_route_sign = (BlockRouteSignage)new BlockRouteSignage.BlockBannerHanging().setUnlocalizedName("signage"); 
	public static final BlockPlatformDoor.Base platform_door_base = (Base) new BlockPlatformDoor.Base().setUnlocalizedName("pfdoor");
	public static final BlockPlatformDoor.Extension platform_door_head = new BlockPlatformDoor.Extension();
	public static final BlockPlatformDoor.Moving platform_door_extension = new BlockPlatformDoor.Moving();
	public static final BlockPlatformGlass platform_glass = (BlockPlatformGlass) new BlockPlatformGlass().setUnlocalizedName("pfglass");
	public static final BlockWHPF roof_where_pf = (BlockWHPF) new BlockWHPF.BlockBannerStanding().setUnlocalizedName("whpf");
	public static final BlockWHPF wall_where_pf = (BlockWHPF) new BlockWHPF.BlockBannerHanging().setUnlocalizedName("whpf");
	//焗漆板
	public static final BlockColorful plate = (BlockColorful) new BlockColorful(Material.IRON).setUnlocalizedName("plate");
	//紙皮石
	public static final BlockColorful mosaic_tile = (BlockColorful) new BlockColorful(Material.ROCK).setUnlocalizedName("mosaic_tile");
	public static final BlockRailNoticer noticer = (BlockRailNoticer) new BlockRailNoticer().setUnlocalizedName("noticer").setCreativeTab(CreativeTabs.TRANSPORTATION);
	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event){
		register(wall_platform_banner, "wall_platform_banner",event.getRegistry());
		register(wall_name_banner, "wall_name_banner",event.getRegistry());
		register(wall_route_sign, "wall_route_sign",event.getRegistry());
		register(platform_door_base, "platform_door",event.getRegistry());
		register(platform_door_head, "platform_door_head",event.getRegistry());
		register(platform_door_extension, "platform_door_extension",event.getRegistry());
		register(platform_glass, "platform_glass",event.getRegistry());
		register(roof_where_pf, "roof_whpf", event.getRegistry());
		register(wall_where_pf, "wall_whpf",event.getRegistry());
		register(plate, "plate",event.getRegistry());
		register(mosaic_tile, "mosaic_tile",event.getRegistry());
		register(noticer, "rail_noticer",event.getRegistry());
	}

	@SubscribeEvent
	public static void onRegisterBlockItem(RegistryEvent.Register<Item> event){
		register(new ItemBlock(platform_door_base), "platform_door",event.getRegistry());
		//register(new ItemBlock(platform_door_head), "platform_door_head",event.getRegistry());
		//register(new ItemBlock(platform_door_extension), "platform_door_extension",event.getRegistry());
		register(new ItemBlock(platform_glass), "platform_glass",event.getRegistry());
		register(new ItemBlock(plate), "plate",event.getRegistry());
		register(new ItemBlock(mosaic_tile), "mosaic_tile",event.getRegistry());
		register(new ItemBlock(noticer), "rail_noticer",event.getRegistry());
	}
	private static void register(Block block, String name, IForgeRegistry<Block> registry){
		ResourceLocation rl = new ResourceLocation(Reference.MODID, name);
		registry.register(block.setRegistryName(rl));
	}
	private static void register(Item block, String name, IForgeRegistry<Item> registry){
		ResourceLocation rl = new ResourceLocation(Reference.MODID, name);
		registry.register(block.setRegistryName(rl));
	}

}
