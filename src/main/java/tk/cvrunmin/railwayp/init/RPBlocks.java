package tk.cvrunmin.railwayp.init;

import tk.cvrunmin.railwayp.block.BlockNameBanner;
import tk.cvrunmin.railwayp.block.BlockPlatformBanner;
import tk.cvrunmin.railwayp.item.ItemPlatformBanner;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RPBlocks {
	public static final BlockPlatformBanner wall_platform_banner = (BlockPlatformBanner)(new BlockPlatformBanner.BlockBannerHanging()).setUnlocalizedName("banner");
	public static final BlockNameBanner wall_name_banner = (BlockNameBanner)new BlockNameBanner().setUnlocalizedName("banner"); 
	public static void register(){
		registerBlock(wall_platform_banner, "wall_platform_banner");
		registerBlock(wall_name_banner, "wall_name_banner");
	}
	private static void registerBlock(Block block, String name){
		GameRegistry.registerBlock(block, name);
	}
}
