package tk.cvrunmin.railwayp.init;

import tk.cvrunmin.railwayp.block.BlockNameBanner;
import tk.cvrunmin.railwayp.block.BlockPlatformBanner;
import tk.cvrunmin.railwayp.block.BlockPlatformDoor;
import tk.cvrunmin.railwayp.block.BlockRouteSignage;
import tk.cvrunmin.railwayp.item.ItemPlatformBanner;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RPBlocks {
	public static final BlockPlatformBanner wall_platform_banner = (BlockPlatformBanner)(new BlockPlatformBanner.BlockBannerHanging()).setUnlocalizedName("banner");
	public static final BlockNameBanner wall_name_banner = (BlockNameBanner)new BlockNameBanner().setUnlocalizedName("banner"); 
	public static final BlockRouteSignage wall_route_sign = (BlockRouteSignage)new BlockRouteSignage.BlockBannerHanging().setUnlocalizedName("signage"); 
	public static final Block platform_door_base = new BlockPlatformDoor.Base(false);
	public static final Block platform_door_head = new BlockPlatformDoor.Extension();
	public static final Block platform_door_extension = new BlockPlatformDoor.Moving();
	public static void register(){
		registerBlock(wall_platform_banner, "wall_platform_banner");
		registerBlock(wall_name_banner, "wall_name_banner");
		registerBlock(wall_route_sign, "wall_route_sign");
		registerBlock(platform_door_base, "platform_door");
		registerBlock(platform_door_head, "platform_door_head");
		registerBlock(platform_door_extension, "platform_door_extension");
	}
	private static void registerBlock(Block block, String name){
		GameRegistry.registerBlock(block, name);
	}
}
