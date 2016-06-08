package io.github.cvronmin.railwayp.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RPCraftingManager {
	public static void register() {
		GameRegistry.addRecipe(new ItemStack(RPItems.platform_banner, 2), new Object[]{
				"AAA",
				"BBB",
				'A', new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,EnumDyeColor.WHITE.getMetadata()),
				'B',Blocks.PLANKS});
		GameRegistry.addRecipe(new ItemStack(RPItems.whpf, 2), new Object[]{
				"AAA",
				"BBB",
				'A', new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,EnumDyeColor.BLACK.getMetadata()),
				'B',Blocks.PLANKS});
		GameRegistry.addRecipe(new ItemStack(RPItems.name_banner, 4), new Object[]{
				" A ",
				"BBB",
				'A', Items.SIGN,
				'B',Blocks.STAINED_HARDENED_CLAY});
		GameRegistry.addRecipe(new ItemStack(RPItems.EDITOR, 1), new Object[]{
				"  A",
				" A ",
				"B  ",
				'A', Items.STICK,
				'B',Items.DYE});
		registerMosaticTile();
		registerPlate();
	}
	private static void registerMosaticTile(){
		for(int i = 0;i < 16;i++){
			GameRegistry.addRecipe(new ItemStack(RPBlocks.mosaic_tile, 8, i), new Object[]{
					"ABA",
					"BBB",
					"ABA",
					'A', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, i),
					'B',Items.QUARTZ});
		}
	}
	private static void registerPlate(){
		for(int i = 0;i < 16;i++){
			GameRegistry.addRecipe(new ItemStack(RPBlocks.plate, 8, i), new Object[]{
					"AAA",
					"ABA",
					"AAA",
					'B', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, i),
					'A',Items.IRON_INGOT});
		}
	}
}
