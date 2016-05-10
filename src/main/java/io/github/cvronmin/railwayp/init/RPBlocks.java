package io.github.cvronmin.railwayp.init;

import io.github.cvronmin.railwayp.Reference;
import io.github.cvronmin.railwayp.block.BlockColorful;
import io.github.cvronmin.railwayp.block.BlockNameBanner;
import io.github.cvronmin.railwayp.block.BlockPlatformBanner;
import io.github.cvronmin.railwayp.block.BlockPlatformDoor;
import io.github.cvronmin.railwayp.block.BlockPlatformGlass;
import io.github.cvronmin.railwayp.block.BlockRColorful;
import io.github.cvronmin.railwayp.block.BlockRailNoticer;
import io.github.cvronmin.railwayp.block.BlockRouteSignage;
import io.github.cvronmin.railwayp.block.BlockWHPF;
import io.github.cvronmin.railwayp.block.BlockPlatformDoor.Base;
import io.github.cvronmin.railwayp.item.ItemPlatformBanner;
import io.github.cvronmin.railwayp.item.ItemRColorful;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemColored;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RPBlocks {
	public static final BlockPlatformBanner wall_platform_banner = (BlockPlatformBanner)(new BlockPlatformBanner.BlockBannerHanging()).setUnlocalizedName("banner");
	public static final BlockNameBanner wall_name_banner = (BlockNameBanner)new BlockNameBanner().setUnlocalizedName("banner"); 
	public static final BlockRouteSignage wall_route_sign = (BlockRouteSignage)new BlockRouteSignage.BlockBannerHanging().setUnlocalizedName("signage"); 
	public static final BlockPlatformDoor.Base platform_door_base = (Base) new BlockPlatformDoor.Base().setUnlocalizedName("pfdoor");
	public static final BlockPlatformDoor.Extension platform_door_head = new BlockPlatformDoor.Extension();
	public static final BlockPlatformDoor.Moving platform_door_extension = new BlockPlatformDoor.Moving();
	public static final BlockPlatformGlass platform_glass = (BlockPlatformGlass) new BlockPlatformGlass().setUnlocalizedName("pfglass");
	public static final BlockWHPF roof_where_pf = (BlockWHPF) new BlockWHPF.BlockBannerStanding().setUnlocalizedName("whpf");
	public static final BlockWHPF wall_where_pf = (BlockWHPF) new BlockWHPF.BlockBannerHanging().setUnlocalizedName("whpf");
	//焗漆板
	public static final BlockRColorful plate = (BlockRColorful) new BlockRColorful(Material.iron).setUnlocalizedName("plate");
	//紙皮石
	public static final BlockRColorful mosaic_tile = (BlockRColorful) new BlockRColorful(Material.rock).setUnlocalizedName("mosaic_tile");
	public static final BlockRailNoticer noticer = (BlockRailNoticer) new BlockRailNoticer().setUnlocalizedName("noticer").setCreativeTab(CreativeTabs.tabTransport);
	public static void register(){
		registerBlock(wall_platform_banner, "wall_platform_banner");
		registerBlock(wall_name_banner, "wall_name_banner");
		registerBlock(wall_route_sign, "wall_route_sign");
		registerBlock(platform_door_base, "platform_door");
		registerBlock(platform_door_head, "platform_door_head");
		registerBlock(platform_door_extension, "platform_door_extension");
		registerBlock(platform_glass, "platform_glass");
		registerBlock(roof_where_pf, "roof_whpf");
		registerBlock(wall_where_pf, "wall_whpf");
		registerBlock(plate, new ItemRColorful(plate), "plate");
		registerBlock(mosaic_tile, new ItemRColorful(mosaic_tile), "mosaic_tile");
		registerBlock(noticer, "rail_noticer");
	}
	private static void registerBlock(Block block, String name){
		registerBlock(block, new ItemBlock(block), name);
	}
	private static void registerBlock(Block block, ItemBlock itemBlock, String name){
		ResourceLocation rl = new ResourceLocation(Reference.MODID, name);
		GameRegistry.register(block.setRegistryName(rl));
		GameRegistry.register(itemBlock.setRegistryName(rl));
	}
}
