package io.github.cvronmin.railwayp.client.gui;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;

import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.init.RPBlocks;
import io.github.cvronmin.railwayp.network.RPPacket;
import io.github.cvronmin.railwayp.network.RPPacketHandler;
import io.github.cvronmin.railwayp.network.RPPacket.EnumRPPacket;
import io.github.cvronmin.railwayp.tileentity.TileEntityPlatformBanner;
import io.github.cvronmin.railwayp.tileentity.TileEntityWHPF;
import io.github.cvronmin.railwayp.util.NTUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiWHPFEditor extends GuiScreen {
	/** Text Field to set NBT PlatformNumber **/
	private GuiTextField platformTextField;
	/** Text Field to set NBT Direction **/
	private GuiTextField directionTextField;
	/** Text Field to set NBT Rotation **/
	private GuiTextField rotationTextField;
	/** Text Field to set NBT Color **/
	private GuiTextField colorTextField;
	/** Text Field to set NBT Text1 **/
	private GuiTextField text1TextField;
	/** Text Field to set NBT Text2 **/
	private GuiTextField text2TextField;
	private TileEntityWHPF te, teedit;
	private NBTTagCompound nbtbu, nbt;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	public GuiWHPFEditor(TileEntityWHPF te){
		this.te = te;
		teedit = new TileEntityWHPF();
		teedit.setWorld(te.getWorld());
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

		this.platformTextField = new GuiTextField(10, this.fontRenderer, posX + (-75 / 2 - 95), posY + (-80), 75, 20);
		this.platformTextField.setMaxStringLength(1);
		this.platformTextField.setValidator(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				for(char ch : input.toCharArray()){
					if(!Character.isDigit(ch)) return false;
				}
				return true;
			}
		});
		this.platformTextField.setText(Integer.toString(te.getRoute()));
		
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

		this.rotationTextField = new GuiTextField(15, this.fontRenderer, posX + (-75 / 2 - 95), posY + (10), 75, 20);
		this.rotationTextField.setMaxStringLength(3);
		this.rotationTextField.setValidator(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				for(char ch : input.toCharArray()){
					if(!Character.isDigit(ch)) return false;
				}
				return true;
			}
		});
		this.rotationTextField.setText(Short.toString(te.getRotation()));
		
		this.colorTextField = new GuiTextField(11, this.fontRenderer, posX + (+75 / 2 + 20), posY + (-80), 75, 20);
		this.colorTextField.setMaxStringLength(6);
		this.colorTextField.setText(Integer.toHexString(te.getRouteColor()));

		this.text1TextField = new GuiTextField(12, this.fontRenderer, posX + (-(75 * 3 + 20 * 2) / 2), posY + (-50), 75 * 3 + 20 * 2, 20);
		this.text1TextField.setMaxStringLength(32767);
		this.text1TextField.setText(te.signText[0].getUnformattedText());

		this.text2TextField = new GuiTextField(13, this.fontRenderer, posX + (-(75 * 3 + 20 * 2) / 2), posY + (-20), 75 * 3 + 20 * 2, 20);
		this.text2TextField.setMaxStringLength(32767);
		this.text2TextField.setText(te.signText[1].getUnformattedText());
		
		this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(cancelBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel", new Object[0])));

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
    	this.platformTextField.textboxKeyTyped(typedChar, keyCode);
    	this.directionTextField.textboxKeyTyped(typedChar, keyCode);
    	this.colorTextField.textboxKeyTyped(typedChar, keyCode);
    	this.text1TextField.textboxKeyTyped(typedChar, keyCode);
    	this.text2TextField.textboxKeyTyped(typedChar, keyCode);
    	this.rotationTextField.textboxKeyTyped(typedChar, keyCode);
        this.teedit.setData(
        		Integer.parseInt(!platformTextField.getText().isEmpty() ? platformTextField.getText() : "1"),
        		Byte.parseByte(!directionTextField.getText().isEmpty() ? directionTextField.getText() : "0"),
        		Short.parseShort(!rotationTextField.getText().isEmpty() ? rotationTextField.getText() : "0"),
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
        this.platformTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.directionTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.colorTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.text1TextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.text2TextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.rotationTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		this.drawCenteredString(this.fontRenderer,I18n.format("gui.editor.title.whpf", new Object[0]), posX, 20, 0xffffff);
		this.drawString(fontRenderer, I18n.format("gui.platform", new Object[0]), posX + (-75 / 2 - 95), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.direction", new Object[0]), posX + (-75 / 2), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.color", new Object[0]), posX + (75 / 2 + 20), posY + (-80) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, "Text1", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-50) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, "Text2", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-20) - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.preview", new Object[0]), posX, posY + 10 - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRenderer, I18n.format("gui.rotation", new Object[0]), posX + (-75 / 2 - 95), posY + 10 - fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		this.platformTextField.drawTextBox();
		this.directionTextField.drawTextBox();
		this.colorTextField.drawTextBox();
		this.text1TextField.drawTextBox();
		this.text2TextField.drawTextBox();
		this.rotationTextField.drawTextBox();
		renderPreview(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	private void renderPreview(int mouseX, int mouseY, float partialTicks){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2) + 35, (float)(this.height / 2) - 50, 50f);
        float f = 93.75F * 0.75f;
        GlStateManager.scale(-f, -f, -f);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        Block block = this.te.getBlockType();

        if (block == RPBlocks.roof_where_pf)
        {
            float f1 = (float)teedit.getRotation();
            GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        }
        else
        {
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

            GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        }
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.teedit, -0.5D, -0.75D, -0.5D, 0.0F);
        GlStateManager.popMatrix();
	}
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
            List<Object> list = new ArrayList<Object>();
            list.add(te.getPos().getX());
            list.add(te.getPos().getY());
            list.add(te.getPos().getZ());
            list.add(this.platformTextField.getText());
            list.add(directionTextField.getText());
            list.add(rotationTextField.getText());
            list.add(this.colorTextField.getText());
            list.add(this.text1TextField.getText());
            list.add(this.text2TextField.getText());
            RailwayP.channelHandle.sendToServer(new RPPacket(EnumRPPacket.C_UPDATE_WHPF, list));
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
