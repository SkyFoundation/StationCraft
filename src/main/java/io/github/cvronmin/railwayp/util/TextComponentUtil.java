package io.github.cvronmin.railwayp.util;

import net.minecraft.util.text.ITextComponent;

public class TextComponentUtil {
	public static String getPureText(ITextComponent component){
		String string = component.getFormattedText();
		//XXX:remove color code
		string = string.replace("§1", "").replace("§2", "").replace("§3", "")
				.replace("§4", "").replace("§5", "").replace("§6", "")
				.replace("§7", "").replace("§8", "").replace("§9", "").replace("§0", "")
				.replace("§a", "").replace("§b", "").replace("§c", "")
				.replace("§d", "").replace("§e", "").replace("§f", "");
		//XXX:remove style code
		string = string.replace("§k", "").replace("§l", "").replace("§m", "")
				.replace("§o", "").replace("§r", "");
		return string;
	}
}
