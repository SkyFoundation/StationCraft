package io.github.cvronmin.railwayp.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.google.common.base.Predicate;

import io.github.cvronmin.railwayp.CommonProxy;
import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.network.CUpdateBannerByGui;
import io.github.cvronmin.railwayp.network.RPPacket;
import io.github.cvronmin.railwayp.network.RPPacket.EnumRPPacket;
import io.github.cvronmin.railwayp.tileentity.TileEntityColorful;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GuiColorfulEditor extends GuiScreen {
	private GuiTextField colorField;
	private TileEntityColorful te, teedit;
	private NBTTagCompound nbtbu, nbt;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private GuiButton selectColorBtn;
	private static final ResourceLocation MTILE = new ResourceLocation("railwayp", "textures/blocks/mtile.png");
	private static final ResourceLocation PLATE = new ResourceLocation("railwayp", "textures/blocks/plate.png");
	public GuiColorfulEditor(TileEntityColorful te) {
		this.te = te;
		this.teedit = new TileEntityColorful();
		//teedit.setPos(this.te.getPos());
		//teedit.setWorldObj(this.te.getWorld());
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
		this.colorField = new GuiTextField(11, this.fontRendererObj, posX + (+75 / 2 + 20), posY + (-80), 75, 17);
		this.colorField.setMaxStringLength(6);
		this.colorField.setText(Integer.toHexString(teedit.getColor()));
		this.colorField.setValidator(new Predicate<String>() {
			@Override
			public boolean apply(String s) {
				return s.isEmpty() | s.matches("-?[0-9a-fA-F]+");
				}
		});
		this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(cancelBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel", new Object[0])));
		this.buttonList.add(selectColorBtn = new GuiButton(10,posX + (+75 / 2 + 20 + 75 + 2),posY -80 - 1, 20,20,"..."));
	}
	@SuppressWarnings("deprecation")
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		this.drawString(fontRendererObj, I18n.format("gui.color", new Object[0]), posX + (75 / 2 + 20), posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRendererObj, I18n.format("gui.preview", new Object[0]), posX +(75 / 2 + 20), posY - 50 - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.colorField.drawTextBox();
        float f = 1.0F / 16;
        float f1 = 1.0F / 16;
        int r = (teedit.getColor() >> 16)&255, g = (teedit.getColor() >> 8)&255, b = (teedit.getColor())&255;
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(te.getBlockType().getMaterial(te.getBlockType().getDefaultState()) == Material.ROCK ? MTILE : PLATE);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos((double)(posX + 75/2 + 70 - 50), (double)(posY - 45 + 100), 0.0D).tex((double)(0               * f), (double)((0 + (float)16) * f1)).color(r, g, b, 255).endVertex();
        vertexbuffer.pos((double)(posX + 75/2 + 70 + 50), (double)(posY - 45 + 100), 0.0D).tex((double)((0 + (float)16) * f), (double)((0 + (float)16) * f1)).color(r, g, b, 255).endVertex();
        vertexbuffer.pos((double)(posX + 75/2 + 70 + 50), (double) posY - 45       , 0.0D).tex((double)((0 + (float)16) * f), (double)(0               * f1)).color(r, g, b, 255).endVertex();
        vertexbuffer.pos((double)(posX + 75/2 + 70 - 50), (double) posY - 45       , 0.0D).tex((double)(0               * f), (double)(0               * f1)).color(r, g, b, 255).endVertex();
        tessellator.draw();
		//renderPreview(mouseX, mouseY, partialTicks);
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
    	/*IStorage<IHexColor> storage = RPCapabilities.hexColor.getStorage();
    	IHexColor hexColor = teedit.getCapability(RPCapabilities.hexColor, null);
    	storage.readNBT(RPCapabilities.hexColor, hexColor, null, new NBTTagString(!colorField.getText().isEmpty() ? colorField.getText() : "191919"));*/
    	updateNbt();
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
	private void updateNbt(){
    	teedit.setColorEncoded(!colorField.getText().isEmpty() ? colorField.getText() : "191919");
    	teedit.decodeColor();
        this.teedit.writeToNBT(nbt);
        this.teedit.readFromNBT(nbt);
	}
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			te.readFromNBT(nbt);
			te.markDirty();
            List<Object> list = new ArrayList<Object>();
            list.add(te.getPos().getX());
            list.add(te.getPos().getY());
            list.add(te.getPos().getZ());
            list.add(!colorField.getText().isEmpty() ? colorField.getText() : "191919");
            CommonProxy.snw.sendToServer(new CUpdateBannerByGui(te.getPos(), nbt));
            RailwayP.channelHandle.sendToServer(new RPPacket(EnumRPPacket.C_UPDATE_COLORFUL, list));
            this.mc.displayGuiScreen(null);
		}

		if (button.id == 1) {
			teedit.readFromNBT(nbtbu);
            this.mc.displayGuiScreen(null);
		}
		if (button.id == 10) {
			Color color = javax.swing.JColorChooser.showDialog(Display.getParent(), "", new Color(Integer.parseInt(colorField.getText(), 16)));
			if(color != null)
			colorField.setText(Integer.toHexString(color.getRGB() & 0xffffff).replace("0x", ""));
	        updateNbt();
		}

	}

	public boolean doesGuiPauseGame() {
		return false;
	}
}
