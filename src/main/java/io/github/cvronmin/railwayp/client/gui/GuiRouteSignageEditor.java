package io.github.cvronmin.railwayp.client.gui;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;

import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.network.RPPacket;
import io.github.cvronmin.railwayp.network.RPPacket.EnumRPPacket;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage.Station;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

public class GuiRouteSignageEditor extends GuiScreen {
	/** Text Field to set NBT Direction **/
	private GuiTextField directionTextField;
	/** Text Field to set NBT Color **/
	private GuiTextField colorTextField;
	private TileEntityRouteSignage te;
	private TileEntityRouteSignage teedit;
	private NBTTagCompound nbtbu, nbt;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private GuiButton editStationBtn;
	private List<Station> stations;
	public GuiRouteSignageEditor(TileEntityRouteSignage te){
		this.te = te;
		teedit = new TileEntityRouteSignage();
		getEditingTileEntity().readFromNBT(this.te.serializeNBT());
		nbtbu = new NBTTagCompound();
		nbt = new NBTTagCompound();
		te.writeToNBT(nbtbu);
		te.writeToNBT(nbt);
		stations = getEditingTileEntity().getStationList();
	}
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		loadMain();
	}
	private void loadMain(){
		this.buttonList.clear();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		
		this.directionTextField = new GuiTextField(14, this.fontRenderer, posX + (-75 / 2), posY + (-80), 75, 20);
		this.directionTextField.setMaxStringLength(1);
		this.directionTextField.setValidator(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				for(char ch : input.toCharArray()){
					if(ch != '0' & ch != '1' & ch != '2' & ch != '3')return false;
				}
				return true;
			}
		});
		this.directionTextField.setText(Byte.toString(te.getDirection()));

		this.colorTextField = new GuiTextField(11, this.fontRenderer, posX + (+75 / 2 + 20), posY + (-80), 75, 20);
		this.colorTextField.setMaxStringLength(6);
		this.colorTextField.setText(Integer.toHexString(te.getRouteColor()));
		
		this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(cancelBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel", new Object[0])));
		this.buttonList.add(editStationBtn = new GuiButton(10, posX + (-75 / 2 - 95), posY + (-80), 75, 20, I18n.format("gui.station.count", getStations().size())));
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
    	//this.platformTextField.textboxKeyTyped(typedChar, keyCode);
    	this.directionTextField.textboxKeyTyped(typedChar, keyCode);
    	this.colorTextField.textboxKeyTyped(typedChar, keyCode);
    	//this.text1TextField.textboxKeyTyped(typedChar, keyCode);
    	//this.text2TextField.textboxKeyTyped(typedChar, keyCode);
        this.getEditingTileEntity().setData(
        		Byte.parseByte(!directionTextField.getText().isEmpty() ? directionTextField.getText() : "0"),
        		!colorTextField.getText().isEmpty() ? colorTextField.getText() : "191919",
				getStations());
        this.getEditingTileEntity().writeToNBT(nbt);
        this.getEditingTileEntity().readFromNBT(nbt);
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
        //this.platformTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.directionTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.colorTextField.mouseClicked(mouseX, mouseY, mouseButton);
        //this.text1TextField.mouseClicked(mouseX, mouseY, mouseButton);
        //this.text2TextField.mouseClicked(mouseX, mouseY, mouseButton);
    }
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		this.drawCenteredString(this.fontRenderer,I18n.format("gui.editor.title.pb", new Object[0]), posX, 20, 0xffffff);
		//this.drawString(fontRendererObj, I18n.format("gui.platform", new Object[0]), posX + (-75 / 2 - 95), posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.direction", new Object[0]), posX + (-75 / 2), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.color", new Object[0]), posX + (75 / 2 + 20), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		//this.drawString(fontRendererObj, "Text1", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-50) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		//this.drawString(fontRendererObj, "Text2", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-20) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.preview", new Object[0]), posX - 50, posY + 10 - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		//this.platformTextField.drawTextBox();
		this.directionTextField.drawTextBox();
		this.colorTextField.drawTextBox();
		//this.text1TextField.drawTextBox();
		//this.text2TextField.drawTextBox();
		renderPreview(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	private void renderPreview(int mouseX, int mouseY, float partialTicks){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2), (float)(this.height / 2) - 100, 50f);
        float f = 93.75F * 0f + 100;
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

        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.getEditingTileEntity(), -0.5D, -0.75D, -0.5D, 0.0F);
        GlStateManager.popMatrix();
	}
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
            List<Object> list = new ArrayList<>();
            list.add(te.getPos().getX());
            list.add(te.getPos().getY());
            list.add(te.getPos().getZ());
            list.add(!directionTextField.getText().isEmpty() ? directionTextField.getText() : "0");
            list.add(!colorTextField.getText().isEmpty() ? colorTextField.getText() : "191919");
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("Stations", Station.writeStationsToTagList(getStations()));
            list.add(compound);
            /*this.teedit.setData(
            		Byte.parseByte(!directionTextField.getText().isEmpty() ? directionTextField.getText() : "0"),
            		!colorTextField.getText().isEmpty() ? colorTextField.getText() : "191919",
            		stations);*/
            RailwayP.channelHandle.sendToServer(new RPPacket(EnumRPPacket.C_UPDATE_ROUTE_SIGN, list));
            this.mc.displayGuiScreen(null);
		}

		if (button.id == 1) {
			getEditingTileEntity().readFromNBT(nbtbu);
            this.mc.displayGuiScreen(null);
		}
		
		if(button.id == 10){
			this.mc.displayGuiScreen(new GuiStationList(this, getStations()));
		}

	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	List<Station> getStations() {
		return stations;
	}

	public TileEntityRouteSignage getEditingTileEntity() {
		return teedit;
	}
}
