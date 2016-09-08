package io.github.cvronmin.railwayp.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.network.RPPacket;
import io.github.cvronmin.railwayp.network.RPPacket.EnumRPPacket;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GuiColorfulEditor extends GuiScreen {
	private GuiTextField colorField;
	private TileEntityColorful te, teedit;
	private NBTTagCompound nbtbu, nbt;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	public GuiColorfulEditor(TileEntityColorful te) {
		this.te = te;
		this.teedit = new TileEntityColorful();
		teedit.setPos(this.te.getPos());
		teedit.setWorldObj(this.te.getWorld());
		teedit.readFromNBT(this.te.serializeNBT());
		nbtbu = new NBTTagCompound();
		nbt = new NBTTagCompound();
		te.writeToNBT(nbtbu);
		te.writeToNBT(nbt);
	}
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		this.colorField = new GuiTextField(11, this.fontRendererObj, posX + (+75 / 2 + 20), posY + (-80), 75, 20);
		this.colorField.setMaxStringLength(6);
		this.colorField.setText(Integer.toHexString(te.getColor()));
		this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(cancelBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel", new Object[0])));
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		this.drawString(fontRendererObj, I18n.format("gui.color", new Object[0]), posX + (75 / 2 + 20), posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRendererObj, I18n.format("gui.preview", new Object[0]), posX +(75 / 2 + 20), posY - 50 - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.colorField.drawTextBox();
		renderPreview(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	private void renderPreview(int mouseX, int mouseY, float partialTicks){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2) + 90, (float)(this.height / 2) - 110, 50f);
        float f = 93.75F * 0.9f;
        GlStateManager.scale(-f, -f, -f);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        int i = this.te.getBlockMetadata();
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            //GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        //this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(this.teedit.getBlockType()), 0, 0);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.teedit, -0.5D, -0.75D, -0.5D, 0.0F);
        GlStateManager.popMatrix();
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
        this.colorField.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	this.colorField.textboxKeyTyped(typedChar, keyCode);
        this.teedit.setColorEncoded(!colorField.getText().isEmpty() ? colorField.getText() : "191919");
        teedit.decodeColor();
        this.teedit.writeToNBT(nbt);
        this.teedit.readFromNBT(nbt);
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
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
            List<Object> list = new ArrayList<Object>();
            list.add(te.getPos().getX());
            list.add(te.getPos().getY());
            list.add(te.getPos().getZ());
            list.add(!colorField.getText().isEmpty() ? colorField.getText() : "191919");
            RailwayP.channelHandle.sendToServer(new RPPacket(EnumRPPacket.C_UPDATE_COLORFUL, list));
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
