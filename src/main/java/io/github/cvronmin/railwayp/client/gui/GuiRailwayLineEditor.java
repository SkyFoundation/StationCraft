package io.github.cvronmin.railwayp.client.gui;

import com.google.common.collect.Lists;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.util.TextComponentUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiRailwayLineEditor extends GuiScreen {
    protected TileEntityRouteSignage.RailLine railLine, railLineEdit;
    protected GuiRailwayLineList parent;
    protected GuiTextField railwayLineName1TF, railwayLineName2TF, railwayLineColorTF;
    protected GuiButton doneBtn, BackBtn;
    protected final int index;

    public GuiRailwayLineEditor(GuiRailwayLineList parent, TileEntityRouteSignage.RailLine railLine, int index) {
        this.parent = parent;
        this.railLine = railLine;
        this.index = index;
        railLineEdit = new TileEntityRouteSignage.RailLine(railLine);
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        loadMain();
    }

    private void loadMain() {
        this.buttonList.clear();
        int posX = (this.width) / 2;
        int posY = (this.height) / 2;

        this.railwayLineName1TF = new GuiTextField(11, this.fontRenderer, posX - 135, posY + (-80), 130, 20);
        this.railwayLineName1TF.setMaxStringLength(32767);
        this.railwayLineName1TF.setText(TextComponentUtil.getPureText(railLine.getLineName()[0]));

        this.railwayLineName2TF = new GuiTextField(12, this.fontRenderer, posX + 5, posY + (-80), 130, 20);
        this.railwayLineName2TF.setMaxStringLength(32767);
        this.railwayLineName2TF.setText(TextComponentUtil.getPureText(railLine.getLineName()[1]));

        this.railwayLineColorTF = new GuiTextField(13, this.fontRenderer, posX - 135, posY + (-40), 130, 20);
        this.railwayLineColorTF.setMaxStringLength(6);
        this.railwayLineColorTF.setText(Integer.toHexString(railLine.getLineColor()));

        this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done")));
        this.buttonList.add(BackBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel")));
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
        this.railwayLineName1TF.textboxKeyTyped(typedChar, keyCode);
        this.railwayLineName2TF.textboxKeyTyped(typedChar, keyCode);
        this.railwayLineColorTF.textboxKeyTyped(typedChar, keyCode);
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
        this.railwayLineName1TF.mouseClicked(mouseX, mouseY, mouseButton);
        this.railwayLineName2TF.mouseClicked(mouseX, mouseY, mouseButton);
        this.railwayLineColorTF.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int posX = (this.width) / 2;
        int posY = (this.height) / 2;
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.editor.title.railwayline"), posX, 20, 0xffffff);
        this.drawString(fontRenderer, I18n.format("gui.railwayline.name.1"), posX - 135, posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        this.drawString(fontRenderer, I18n.format("gui.railwayline.name.2"), posX + 5, posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        this.drawString(fontRenderer, I18n.format("gui.railwayline.color"), posX - 135, posY - 40 - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        this.railwayLineName1TF.drawTextBox();
        this.railwayLineName2TF.drawTextBox();
        this.railwayLineColorTF.drawTextBox();
        drawPreview();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPreview() {
        drawRect(0, this.height / 2 + 25, this.width, this.height / 2 + 30 + 25, getColor(railwayLineColorTF.getText()) | 0xff000000);
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
            railLineEdit.setLineName(railwayLineName1TF.getText(),railwayLineName2TF.getText());
            railLineEdit.setLineColor(railwayLineColorTF.getText());
            railLine = railLineEdit;
            if (parent.railLines == null) {
                parent.railLines = Lists.newArrayList();
            }
            if (parent.railLines.size() - 1 < index)
                parent.railLines.add(railLine);
            else if (parent.railLines.get(index) == null)
                parent.railLines.add(index, railLine);
            else
                parent.railLines.set(index, railLine);
            this.mc.displayGuiScreen(parent);
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(parent);
        }

    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
