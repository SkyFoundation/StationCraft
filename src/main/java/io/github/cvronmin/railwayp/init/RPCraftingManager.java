package io.github.cvronmin.railwayp.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class RPCraftingManager {
	public static void register() {
		GameRegistry.addRecipe(new ItemStack(RPItems.platform_banner, 2), new Object[]{
				"AAA",
				"BBB",
				'A', new ItemStack(Blocks.stained_hardened_clay,1,EnumDyeColor.WHITE.getMetadata()),
				'B',Blocks.planks});
		GameRegistry.addRecipe(new ItemStack(RPItems.whpf, 2), new Object[]{
				"AAA",
				"BBB",
				'A', new ItemStack(Blocks.stained_hardened_clay,1,EnumDyeColor.BLACK.getMetadata()),
				'B',Blocks.planks});
		GameRegistry.addRecipe(new ItemStack(RPItems.name_banner, 4), new Object[]{
				" A ",
				"BBB",
				'A', Items.sign,
				'B',Blocks.stained_hardened_clay});
		GameRegistry.addRecipe(new ItemStack(RPItems.EDITOR, 1), new Object[]{
				"  A",
				" A ",
				"B  ",
				'A', Items.stick,
				'B',Items.dye});
		registerMosaticTile();
		registerPlate();
		net.minecraftforge.oredict.RecipeSorter.register("railwayp:cloning", CloningRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");
		registerCloningRecipe();
	}
	private static void registerMosaticTile(){
		for(int i = 0;i < 16;i++){
			GameRegistry.addRecipe(new ItemStack(RPBlocks.mosaic_tile, 8, i), new Object[]{
					"ABA",
					"BBB",
					"ABA",
					'A', new ItemStack(Blocks.stained_hardened_clay, 1, i),
					'B',Items.quartz});
		}
	}
	private static void registerPlate(){
		for(int i = 0;i < 16;i++){
			GameRegistry.addRecipe(new ItemStack(RPBlocks.plate, 8, i), new Object[]{
					"AAA",
					"ABA",
					"AAA",
					'B', new ItemStack(Blocks.stained_hardened_clay, 1, i),
					'A',Items.iron_ingot});
		}
	}
	private static void registerCloningRecipe(){
		GameRegistry.addRecipe(new CloningRecipe(RPItems.name_banner));
		GameRegistry.addRecipe(new CloningRecipe(RPItems.platform_banner));
		GameRegistry.addRecipe(new CloningRecipe(RPItems.whpf));
		GameRegistry.addRecipe(new CloningRecipe(RPItems.route_sign));
		GameRegistry.addRecipe(new CloningRecipe(Item.getItemFromBlock(RPBlocks.mosaic_tile)));
		GameRegistry.addRecipe(new CloningRecipe(Item.getItemFromBlock(RPBlocks.plate)));
	}
	private static class CloningRecipe implements IRecipe{
		Item item;
		public CloningRecipe(Item item) {
			this.item = item;
		}
		@Override
		public boolean matches(InventoryCrafting inv, World worldIn) {
	        int i = 0;
	        ItemStack itemstack = null;

	        for (int j = 0; j < inv.getSizeInventory(); ++j)
	        {
	            ItemStack itemstack1 = inv.getStackInSlot(j);

	            if (itemstack1 != null)
	            {
	            	if(itemstack1.getItem() == item){
	                if (itemstack1.hasTagCompound())
	                {
	                    if (itemstack != null)
	                    {
	                        return false;
	                    }

	                    itemstack = itemstack1;
	                }
	                else ++i;
	            	}else return false;
	            }
	        }

	        return itemstack != null && i > 0;
		}
		
		@Override
		public ItemStack[] getRemainingItems(InventoryCrafting inv) {
	        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

	        for (int i = 0; i < aitemstack.length; ++i)
	        {
	            ItemStack itemstack = inv.getStackInSlot(i);
	            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
	        }

	        return aitemstack;
		}
		
		@Override
		public int getRecipeSize() {
			return 9;
		}
		
		@Override
		public ItemStack getRecipeOutput() {
			return null;
		}
		
		@Override
		public ItemStack getCraftingResult(InventoryCrafting inv) {
	        int i = 0;
	        ItemStack itemstack = null;

	        for (int j = 0; j < inv.getSizeInventory(); ++j)
	        {
	            ItemStack itemstack1 = inv.getStackInSlot(j);

	            if (itemstack1 != null)
	            {
	            	if(itemstack1.getItem() == item){
		                if (itemstack1.hasTagCompound())
		                {
		                    if (itemstack != null)
		                    {
		                        return null;
		                    }

		                    itemstack = itemstack1;
		                }
		                else ++i;
		            	}else return null;
	            }
	        }

	        if (itemstack != null && i >= 1)
	        {
	            ItemStack itemstack2 = new ItemStack(item, i + 1, itemstack.getMetadata());

	            if (itemstack.hasDisplayName())
	            {
	                itemstack2.setStackDisplayName(itemstack.getDisplayName());
	            }
	            itemstack2.setTagCompound(itemstack.getTagCompound());

	            return itemstack2;
	        }
	        else
	        {
	            return null;
	        }
		}
	}
}
