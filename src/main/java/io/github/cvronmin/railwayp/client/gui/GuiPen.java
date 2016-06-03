package io.github.cvronmin.railwayp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiPen extends Gui {
    private Minecraft mc;
    private static String nextStation;
    private static String thisStation;
    private static String interchange;
    private static final String rt = "\u2794";
    private static final String pt = "\u25cf";
    public GuiPen(Minecraft mc) {
	super();
	this.mc = mc;
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void eventHandler(RenderGameOverlayEvent event){
	EntityPlayer entity = Minecraft.getMinecraft().thePlayer;
	int cenX = event.getResolution().getScaledWidth() / 6 / 2, y = event.getResolution().getScaledHeight() / 5 * 2;
	if(entity.getRidingEntity() instanceof EntityMinecartEmpty){
	    drawRect(0, event.getResolution().getScaledHeight() / 5 * 2, event.getResolution().getScaledWidth() / 6, event.getResolution().getScaledHeight() / 5 * 3, 419430400);
	    String s = I18n.format("gui.railway");
	    this.mc.fontRendererObj.drawString(s, cenX - mc.fontRendererObj.getStringWidth(s) / 2, y + event.getResolution().getScaledHeight() / 5 / 10, 0xffffff);
	    if(thisStation != null && thisStation != ""){
		if(nextStation != null && nextStation != ""){
		    String s1 = thisStation + rt + nextStation;
		    mc.fontRendererObj.drawString(s1, cenX - mc.fontRendererObj.getStringWidth(s1) / 2, y + event.getResolution().getScaledHeight() / 5 / 10 * 2, 0xffffff);
		}
		else{
		    String s1 = pt + thisStation;
		    mc.fontRendererObj.drawString(s1, cenX - mc.fontRendererObj.getStringWidth(s1) / 2, y + event.getResolution().getScaledHeight() / 5 / 10 * 2, 0xffffff);
		}
	    }
	}
    }
    public void setNext(String s){
	nextStation = s;
    }
    public void setThis(String s){
	thisStation = s;
    }
    public void setInterchange(String s){
	interchange = s;
    }
}
