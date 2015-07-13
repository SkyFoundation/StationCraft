package tk.cvrunmin.railwayp.client.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiPlatformBannerEditor extends GuiScreen {
    private GuiTextField platformTextField;
    private GuiTextField colorTextField;
    private GuiTextField text1TextField;
    private GuiTextField text2TextField;

public void initGui(){
Keyboard.enableRepeatEvents(true);
this.buttonList.clear();
int posX = (this.width) / 2;
int posY = (this.height) / 2;

this.platformTextField = new GuiTextField(10, this.fontRendererObj, posX+(-147), posY+(-79), 300, 20);
this.platformTextField.setMaxStringLength(32767);

this.platformTextField.setText("");

this.colorTextField = new GuiTextField(11, this.fontRendererObj, posX+(-147), posY+(-53), 300, 20);
this.colorTextField.setMaxStringLength(32767);

this.colorTextField.setText("");

this.text1TextField = new GuiTextField(12, this.fontRendererObj, posX+(-147), posY+(-23), 300, 20);
this.text1TextField.setMaxStringLength(32767);

this.text1TextField.setText("");

this.text2TextField = new GuiTextField(12, this.fontRendererObj, posX+(-148), posY+(3), 300, 20);
this.text2TextField.setMaxStringLength(32767);

this.text2TextField.setText("");
this.buttonList.add(new GuiButton(0, posX+(-116), posY+(62), 74, 20, "Confirm"));
this.buttonList.add(new GuiButton(1, posX+(45), posY+(63), 66, 20, "Cancel"));

}
protected void drawGuiContainerForegroundLayer(int par1, int par2){
	int posX = (this.width) /2;
	int posY = (this.height) /2;
	this.fontRendererObj.drawString("Platform Signage Editor", posX, posY, 0xffffff);

this.platformTextField.drawTextBox();
this.colorTextField.drawTextBox();
this.text1TextField.drawTextBox();
this.text2TextField.drawTextBox();
}
protected void actionPerformed(GuiButton button)
{
MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
World world = server.worldServers[0];


if (button.id == 0){

}


if (button.id == 1){

}

}
public boolean doesGuiPauseGame()
{
    return false;
}
}
