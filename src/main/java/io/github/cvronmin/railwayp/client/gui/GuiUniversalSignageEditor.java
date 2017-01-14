package io.github.cvronmin.railwayp.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.cvronmin.railwayp.CommonProxy;
import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.network.CUpdateBannerByGui;
import io.github.cvronmin.railwayp.network.RPPacket;
import io.github.cvronmin.railwayp.network.RPPacket.EnumRPPacket;
import io.github.cvronmin.railwayp.tileentity.TileEntityUniversalSignage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

public class GuiUniversalSignageEditor extends GuiScreen {
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private TileEntityUniversalSignage te, teedit;
	private NBTTagCompound nbt, nbtbu;
	public GuiUniversalSignageEditor(TileEntityUniversalSignage te) {
		this.te = te;
		this.teedit = new TileEntityUniversalSignage();
		teedit.readFromNBT(this.te.serializeNBT());
		nbtbu = new NBTTagCompound();
		nbt = new NBTTagCompound();
		te.writeToNBT(nbtbu);
		te.writeToNBT(nbt);
	}
	@Override
	public void initGui() {
		this.buttonList.clear();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		this.buttonList.clear();
		this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(cancelBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel", new Object[0])));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 1)
            {
                this.actionPerformed(this.cancelBtn);
            }
        }
        else
        {
            this.actionPerformed(this.doneBtn);
        }
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			te.readFromNBT(nbt);
			te.markDirty();
            List<Object> list = new ArrayList<Object>();
            list.add(te.getPos().getX());
            list.add(te.getPos().getY());
            list.add(te.getPos().getZ());
            //list.add(!colorField.getText().isEmpty() ? colorField.getText() : "191919");
            CommonProxy.snw.sendToServer(new CUpdateBannerByGui(te.getPos(), nbt));
            //RailwayP.channelHandle.sendToServer(new RPPacket(EnumRPPacket.C_UPDATE_COLORFUL, list));
            this.mc.displayGuiScreen(null);
		}

		if (button.id == 1) {
			teedit.readFromNBT(nbtbu);
            this.mc.displayGuiScreen(null);
		}
	}
	public boolean doesGuiPauseGame() {
		return false;
	}
}
