package tk.cvrunmin.railwayp.init;

import tk.cvrunmin.railwayp.block.BlockColorful;
import tk.cvrunmin.railwayp.block.BlockNameBanner;
import tk.cvrunmin.railwayp.block.BlockPlatformBanner;
import tk.cvrunmin.railwayp.block.BlockPlatformDoor;
import tk.cvrunmin.railwayp.block.BlockPlatformDoor.Base;
import tk.cvrunmin.railwayp.block.BlockPlatformGlass;
import tk.cvrunmin.railwayp.block.BlockRouteSignage;
import tk.cvrunmin.railwayp.item.ItemPlatformBanner;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RPBlocks {
	public static final BlockPlatformBanner wall_platform_banner = (BlockPlatformBanner)(new BlockPlatformBanner.BlockBannerHanging()).setUnlocalizedName("banner");
	public static final BlockNameBanner wall_name_banner = (BlockNameBanner)new BlockNameBanner().setUnlocalizedName("banner"); 
	public static final BlockRouteSignage wall_route_sign = (BlockRouteSignage)new BlockRouteSignage.BlockBannerHanging().setUnlocalizedName("signage"); 
	public static final BlockPlatformDoor.Base platform_door_base = (Base) new BlockPlatformDoor.Base().setUnlocalizedName("pfdoor");
	public static final BlockPlatformDoor.Extension platform_door_head = new BlockPlatformDoor.Extension();
	public static final BlockPlatformDoor.Moving platform_door_extension = new BlockPlatformDoor.Moving();
	public static final BlockPlatformGlass platform_glass = (BlockPlatformGlass) new BlockPlatformGlass().setUnlocalizedName("pfglass");
	//焗漆板
	public static final BlockColorful plate = (BlockColorful) new BlockColorful(Material.iron).setUnlocalizedName("plate");
	//紙皮石
	public static final BlockColorful mosaic_tile = (BlockColorful) new BlockColorful(Material.rock).setUnlocalizedName("mosaic_tile");
	public static void register(){
		registerBlock(wall_platform_banner, "wall_platform_banner");
		registerBlock(wall_name_banner, "wall_name_banner");
		registerBlock(wall_route_sign, "wall_route_sign");
		registerBlock(platform_door_base, "platform_door");
		registerBlock(platform_door_head, "platform_door_head");
		registerBlock(platform_door_extension, "platform_door_extension");
		registerBlock(platform_glass, "platform_glass");
		registerBlock(plate, "plate");
		registerBlock(mosaic_tile, "mosaic_tile");
	}
	private static void registerBlock(Block block, String name){
		GameRegistry.registerBlock(block, name);
	}
}
