package io.github.cvronmin.railwayp.client.gui;

import com.google.common.collect.Lists;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.util.TextComponentUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class GuiStationEditor extends GuiScreen {
    protected TileEntityRouteSignage.Station station, stationEdit;
    protected GuiStationList parent;
    protected GuiTextField stationName1TF, stationName2TF;
    protected GuiButton doneBtn, BackBtn, ilBtn;
    protected GuiButtonExt hereBtn;
    protected final int index;

    public GuiStationEditor(GuiStationList parent, TileEntityRouteSignage.Station station, int index) {
        this.parent = parent;
        this.station = station;
        this.index = index;
        stationEdit = new TileEntityRouteSignage.Station(station);
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        loadMain();
    }

    private void loadMain() {
        this.buttonList.clear();
        int posX = (this.width) / 2;
        int posY = (this.height) / 2;

        this.stationName1TF = new GuiTextField(11, this.fontRenderer, posX - 135, posY + (-80), 130, 20);
        this.stationName1TF.setMaxStringLength(32767);
        this.stationName1TF.setText(TextComponentUtil.getPureText(station.stationName[0]));

        this.stationName2TF = new GuiTextField(12, this.fontRenderer, posX + 5, posY + (-80), 130, 20);
        this.stationName2TF.setMaxStringLength(32767);
        this.stationName2TF.setText(TextComponentUtil.getPureText(station.stationName[1]));

        this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done")));
        this.buttonList.add(BackBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel")));
        this.buttonList.add(ilBtn = new GuiButton(2, posX - 135 / 2, posY - 40, 130,20, I18n.format("gui.station.interchange.count", stationEdit.getInterchangeLines().size())));
        this.buttonList.add(hereBtn = new GuiButtonExt(10, posX - 135, posY, 130, 20, Boolean.toString(station.amIHere())));
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.stationName1TF.textboxKeyTyped(typedChar, keyCode);
        this.stationName2TF.textboxKeyTyped(typedChar, keyCode);
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.actionPerformed(this.BackBtn);
            }
        } else {
            this.actionPerformed(this.doneBtn);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.stationName1TF.mouseClicked(mouseX, mouseY, mouseButton);
        this.stationName2TF.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int posX = (this.width) / 2;
        int posY = (this.height) / 2;
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.editor.title.station"), posX, 20, 0xffffff);
        this.drawString(fontRenderer, I18n.format("gui.station.name.1"), posX - 135, posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        this.drawString(fontRenderer, I18n.format("gui.station.name.2"), posX + 5, posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        this.drawString(fontRenderer, I18n.format("gui.station.here"), posX - 135, posY - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        this.stationName1TF.drawTextBox();
        this.stationName2TF.drawTextBox();
        drawPreview();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPreview() {
        drawRect(0, this.height / 2 + 25, this.width, this.height / 2 + 30 + 25, parent.parentScreen.getEditingTileEntity().getRouteColor() | 0xff000000);
        drawRect(this.width / 2 - 15, this.height / 2 + 30, this.width / 2 + 15, this.height / 2 + 30 + 20, 0xFF000000);
        drawRect(this.width / 2 - 10, this.height / 2 + 25, this.width / 2 + 10, this.height / 2 + 30 + 25, 0xFF000000);
        drawRect(this.width / 2 - 10, this.height / 2 + 30, this.width / 2 + 10, this.height / 2 + 30 + 20, 0xFFFFFFFF);
    }

    private int getColor(String s) {
        try {
            return Integer.parseInt(s, 16);
        } catch (Exception e) {
            return 0;
        }
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            stationEdit.setStationName(stationName1TF.getText(), stationName2TF.getText());
            //stationEdit.getInterchangeLines().set(0, new TileEntityRouteSignage.RailLine().setLineName(ilName1TF.getText(), ilName2TF.getText()).setLineColor(ilColorTF.getText()));
            station = stationEdit;
            if (parent.stationList == null) {
                parent.stationList = Lists.newArrayList();
            }
            if (parent.stationList.size() - 1 < index)
                parent.stationList.add(station);
            else if (parent.stationList.get(index) == null)
                parent.stationList.add(index, station);
            else
                parent.stationList.set(index, station);
            this.mc.displayGuiScreen(parent);
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(parent);
        }
        if (button.id == 2){
            this.mc.displayGuiScreen(new GuiRailwayLineList(this,stationEdit.getInterchangeLines()));
        }
        if (button.id == 10) {
            stationEdit.setIfIAmHere(!stationEdit.amIHere());
            this.hereBtn.displayString = Boolean.toString(stationEdit.amIHere());
            this.hereBtn.packedFGColour = stationEdit.amIHere() ? GuiUtils.getColorCode('2', true) : GuiUtils.getColorCode('4', true);
        }

    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
