package io.github.cvronmin.railwayp.client.gui;


import com.google.common.base.Predicate;
import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.network.RPPacket;
import io.github.cvronmin.railwayp.network.RPPacket.EnumRPPacket;
import io.github.cvronmin.railwayp.tileentity.TileEntityNameBanner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiNameBannerEditor extends GuiScreen {
	/** Text Field to set NBT SignType **/
	private GuiTextField signTypeTextField;
	/** Text Field to set NBT StationColor **/
	private GuiTextField stationColorTextField;
	/** Text Field to set NBT Color **/
	private GuiTextField colorTextField;
	/** Text Field to set NBT Text1 **/
	private GuiTextField text1TextField;
	/** Text Field to set NBT Text2 **/
	private GuiTextField text2TextField;
	private TileEntityNameBanner te, teedit;
	private NBTTagCompound nbtbu, nbt;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	public GuiNameBannerEditor(TileEntityNameBanner te){
		this.te = te;
		this.teedit = new TileEntityNameBanner();
		teedit.readFromNBT(this.te.serializeNBT());
		nbtbu = new NBTTagCompound();
		nbt = new NBTTagCompound();
		te.writeToNBT(nbtbu);
		te.writeToNBT(nbt);
	}
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;

		this.signTypeTextField = new GuiTextField(10, this.fontRenderer, posX + (-75 / 2 - 95), posY + (-80), 75, 20);
		this.signTypeTextField.setMaxStringLength(1);
		this.signTypeTextField.setValidator(input -> {
            for(char ch : input.toCharArray()){
                if(ch != '0' && ch != '1' && ch != '2')return false;
            }
            return true;
        });
		this.signTypeTextField.setText(Integer.toString(te.getSignType()));
		
		this.stationColorTextField = new GuiTextField(14, this.fontRenderer, posX + (-75 / 2), posY + (-80), 75, 20);
		this.stationColorTextField.setMaxStringLength(6);
		this.stationColorTextField.setText(Integer.toHexString(te.getStationColor()));

		this.colorTextField = new GuiTextField(11, this.fontRenderer, posX + (+75 / 2 + 20), posY + (-80), 75, 20);
		this.colorTextField.setMaxStringLength(6);
		this.colorTextField.setText(Integer.toHexString(te.getColor()));

		this.text1TextField = new GuiTextField(12, this.fontRenderer, posX + (-(75 * 3 + 20 * 2) / 2), posY + (-50), 75 * 2 + 20 * 1, 20);
		this.text1TextField.setMaxStringLength(32767);
		this.text1TextField.setText(te.signText[0].getUnformattedText());

		this.text2TextField = new GuiTextField(13, this.fontRenderer, posX + (-(75 * 3 + 20 * 2) / 2), posY + (-20), 75 * 2 + 20 * 1, 20);
		this.text2TextField.setMaxStringLength(32767);
		this.text2TextField.setText(te.signText[1].getUnformattedText());
		
		this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done")));
		this.buttonList.add(cancelBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel")));
	}
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    	this.signTypeTextField.textboxKeyTyped(typedChar, keyCode);
    	this.stationColorTextField.textboxKeyTyped(typedChar, keyCode);
    	this.colorTextField.textboxKeyTyped(typedChar, keyCode);
    	this.text1TextField.textboxKeyTyped(typedChar, keyCode);
    	this.text2TextField.textboxKeyTyped(typedChar, keyCode);
        this.teedit.setData(
        		Integer.parseInt(!signTypeTextField.getText().isEmpty() ? signTypeTextField.getText() : "0"),
        		!stationColorTextField.getText().isEmpty() ? stationColorTextField.getText() : "BBBBBB",
        		!colorTextField.getText().isEmpty() ? colorTextField.getText() : "191919",
        		!text1TextField.getText().isEmpty() ? text1TextField.getText() : "",
        				!text2TextField.getText().isEmpty() ? text2TextField.getText() : "");
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
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.signTypeTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.stationColorTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.colorTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.text1TextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.text2TextField.mouseClicked(mouseX, mouseY, mouseButton);
    }
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		this.drawCenteredString(fontRenderer,I18n.format("gui.editor.title.nb"), posX, 20, 0xffffff);
		this.drawString(fontRenderer, I18n.format("gui.signage.type"), posX + (-75 / 2 - 95), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.color.station"), posX + (-75 / 2), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.color"), posX + (75 / 2 + 20), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, "Text1", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-50) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, "Text2", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-20) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.preview"), posX +(75 / 2 + 20), posY - 50 - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.signTypeTextField.drawTextBox();
		this.stationColorTextField.drawTextBox();
		this.colorTextField.drawTextBox();
		this.text1TextField.drawTextBox();
		this.text2TextField.drawTextBox();
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

        TileEntityRendererDispatcher.instance.render(this.teedit, -0.5D, -0.75D, -0.5D, 0.0F);
        GlStateManager.popMatrix();
	}
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
            List<Object> list = new ArrayList<Object>();
            list.add(te.getPos().getX());
            list.add(te.getPos().getY());
            list.add(te.getPos().getZ());
            list.add(this.signTypeTextField.getText());
            list.add(stationColorTextField.getText());
            list.add(this.colorTextField.getText());
            list.add(this.text1TextField.getText());
            list.add(this.text2TextField.getText());
            RailwayP.channelHandle.sendToServer(new RPPacket(EnumRPPacket.C_UPDATENAME_BANNER, list));
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
