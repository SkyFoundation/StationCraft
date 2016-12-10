package io.github.cvronmin.railwayp.client.gui;


import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import io.github.cvronmin.railwayp.RailwayP;
import io.github.cvronmin.railwayp.network.RPPacket;
import io.github.cvronmin.railwayp.network.RPPacket.EnumRPPacket;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage.Station;
import io.github.cvronmin.railwayp.util.TextComponentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiRouteSignageEditor extends GuiScreen {
	/** Text Field to set NBT Direction **/
	private GuiTextField directionTextField;
	/** Text Field to set NBT Color **/
	private GuiTextField colorTextField;
	private TileEntityRouteSignage te, teedit;
	private NBTTagCompound nbtbu, nbt;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private GuiButton editStationBtn;
	private List<Station> stations;
	public GuiRouteSignageEditor(TileEntityRouteSignage te){
		this.te = te;
		teedit = new TileEntityRouteSignage();
		teedit.readFromNBT(this.te.serializeNBT());
		nbtbu = new NBTTagCompound();
		nbt = new NBTTagCompound();
		te.writeToNBT(nbtbu);
		te.writeToNBT(nbt);
		stations = teedit.getStationList();
	}
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		loadMain();
	}
	private void loadMain(){
		this.buttonList.clear();
		int posX = (this.width) / 2;
		int posY = (this.height) / 2;
		
		this.directionTextField = new GuiTextField(14, this.fontRendererObj, posX + (-75 / 2), posY + (-80), 75, 20);
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

		this.colorTextField = new GuiTextField(11, this.fontRendererObj, posX + (+75 / 2 + 20), posY + (-80), 75, 20);
		this.colorTextField.setMaxStringLength(6);
		this.colorTextField.setText(Integer.toHexString(te.getRouteColor()));
		
		this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(cancelBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel", new Object[0])));
		this.buttonList.add(editStationBtn = new GuiButton(10, posX + (-75 / 2 - 95), posY + (-80), 75, 20, I18n.format("gui.station.count", stations.size())));
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
        this.teedit.setData(
        		Byte.parseByte(!directionTextField.getText().isEmpty() ? directionTextField.getText() : "0"),
        		!colorTextField.getText().isEmpty() ? colorTextField.getText() : "191919",
        		stations);
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
		this.drawCenteredString(this.fontRendererObj,I18n.format("gui.editor.title.pb", new Object[0]), posX, 20, 0xffffff);
		//this.drawString(fontRendererObj, I18n.format("gui.platform", new Object[0]), posX + (-75 / 2 - 95), posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRendererObj, I18n.format("gui.direction", new Object[0]), posX + (-75 / 2), posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRendererObj, I18n.format("gui.color", new Object[0]), posX + (75 / 2 + 20), posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		//this.drawString(fontRendererObj, "Text1", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-50) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		//this.drawString(fontRendererObj, "Text2", posX + (-(75 * 3 + 20 * 2) / 2), posY + (-20) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
		this.drawString(fontRendererObj, I18n.format("gui.preview", new Object[0]), posX - 50, posY + 10 - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
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

        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.teedit, -0.5D, -0.75D, -0.5D, 0.0F);
        GlStateManager.popMatrix();
	}
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
            List<Object> list = new ArrayList<Object>();
            list.add(te.getPos().getX());
            list.add(te.getPos().getY());
            list.add(te.getPos().getZ());
            list.add(!directionTextField.getText().isEmpty() ? directionTextField.getText() : "0");
            list.add(!colorTextField.getText().isEmpty() ? colorTextField.getText() : "191919");
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("Stations", Station.writeStationsToTagList(stations));
            list.add(compound);
            /*this.teedit.setData(
            		Byte.parseByte(!directionTextField.getText().isEmpty() ? directionTextField.getText() : "0"),
            		!colorTextField.getText().isEmpty() ? colorTextField.getText() : "191919",
            		stations);*/
            RailwayP.channelHandle.sendToServer(new RPPacket(EnumRPPacket.C_UPDATE_ROUTE_SIGN, list));
            this.mc.displayGuiScreen(null);
		}

		if (button.id == 1) {
			teedit.readFromNBT(nbtbu);
            this.mc.displayGuiScreen(null);
		}
		
		if(button.id == 10){
			this.mc.displayGuiScreen(new GuiStationList(this, stations));
		}

	}

	public boolean doesGuiPauseGame() {
		return false;
	}
	public static class GuiStationList extends GuiScreen{
	    protected GuiRouteSignageEditor parentScreen;
	    protected ListEntries entryList;
	    protected List<Station> stationList;
	    private GuiButtonExt doneBtn;
	    public GuiStationList(GuiRouteSignageEditor screen, List<Station> list){
	    	parentScreen = screen;
	    	stationList = list;
	    }
	    /**
	     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	     * window resizes, the buttonList is cleared beforehand.
	     */
	    @Override
	    public void initGui()
	    {
	        this.entryList = new ListEntries(this);

	        int undoGlyphWidth = mc.fontRendererObj.getStringWidth(UNDO_CHAR) * 2;
	        int resetGlyphWidth = mc.fontRendererObj.getStringWidth(RESET_CHAR) * 2;
	        int doneWidth = Math.max(mc.fontRendererObj.getStringWidth(I18n.format("gui.done")) + 20, 100);
	        int undoWidth = mc.fontRendererObj.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
	        int resetWidth = mc.fontRendererObj.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
	        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
	        this.buttonList.add(doneBtn = new GuiButtonExt(1, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
	    }
	    /**
	     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	     */
	    @Override
	    protected void keyTyped(char eventChar, int eventKey)
	    {
	        if (eventKey == Keyboard.KEY_ESCAPE)
	            this.mc.displayGuiScreen(parentScreen);
	        else if(eventKey == Keyboard.KEY_RETURN)
	        	actionPerformed(doneBtn);
	        else
	            this.entryList.keyTyped(eventChar, eventKey);
	    }
	    /**
	     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	     */
	    @Override
	    protected void actionPerformed(GuiButton button)
	    {
	        if (button.id == 1)
	        {
	            try
	            {
	            	this.parentScreen.stations = stationList;
	            }
	            catch (Throwable e)
	            {
	                e.printStackTrace();
	            }
	            this.mc.displayGuiScreen(this.parentScreen);
	        }

	    }
	    /**
	     * Called from the main game loop to update the screen.
	     */
	    @Override
	    public void updateScreen()
	    {
	        super.updateScreen();
	        this.entryList.updateScreen();
	    }

	    /**
	     * Draws the screen and all the components in it.
	     *  
	     * @param mouseX Mouse x coordinate
	     * @param mouseY Mouse y coordinate
	     * @param partialTicks How far into the current tick (1/20th of a second) the game is
	     */
	    @Override
	    public void drawScreen(int par1, int par2, float par3)
	    {
	        //this.drawDefaultBackground();
	        this.entryList.drawScreen(par1, par2, par3);
	        this.drawCenteredString(this.fontRendererObj, I18n.format("gui.editor.title.stations"), this.width / 2, 8, 16777215);

	        super.drawScreen(par1, par2, par3);
	        //this.entryList.drawScreenPost(par1, par2, par3);
	    }
	    /**
	     * Handles mouse input.
	     */
	    public void handleMouseInput() throws IOException
	    {
	        super.handleMouseInput();
	        this.entryList.handleMouseInput();
	    }
		public boolean doesGuiPauseGame() {
			return false;
		}
	    /**
	     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	     */
	    @Override
	    protected void mouseClicked(int x, int y, int mouseEvent) throws IOException
	    {
	        if (mouseEvent != 0 || !this.entryList.mouseClicked(x, y, mouseEvent))
	        {
	            this.entryList.mouseClickedPassThru(x, y, mouseEvent);
	            super.mouseClicked(x, y, mouseEvent);
	        }
	    }

	    /**
	     * Called when a mouse button is released.
	     *  
	     * @param mouseX Current mouse x coordinate
	     * @param mouseY Current mouse y coordinate
	     * @param state The mouse button that was released
	     */
	    @Override
	    protected void mouseReleased(int x, int y, int mouseEvent)
	    {
	        if (mouseEvent != 0 || !this.entryList.mouseReleased(x, y, mouseEvent))
	        {
	            super.mouseReleased(x, y, mouseEvent);
	        }
	    }
	    public static class ListEntries extends GuiListExtended{
	    	protected GuiStationList owner;
	    	protected List<Station> stations;
	    	protected List<IGuiEntryExtended> entries;
	    	public ListEntries(GuiStationList owner){
	    		super(owner.mc, owner.width, owner.height, 33, owner.height - 32, 36);
	    		this.owner = owner;
	    		stations = owner.stationList;
	    		initEntries();
	    	}
	    	@Override
	    	protected void drawBackground(){
	    		//owner.drawDefaultBackground();
	            owner.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
	    	}
	    	@Override
	        public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks)
	        {
	            if (this.visible)
	            {
	                this.mouseX = mouseXIn;
	                this.mouseY = mouseYIn;
	                this.drawBackground();
	                int i = this.getScrollBarX();
	                int j = i + 6;
	                int viewHeight     = this.bottom - this.top;
	                this.bindAmountScrolled();
	                GlStateManager.disableLighting();
	                GlStateManager.disableFog();
	                Tessellator tessellator = Tessellator.getInstance();
	                VertexBuffer vertexbuffer = tessellator.getBuffer();
	                ScaledResolution res = new ScaledResolution(mc);
	                double scaleW = mc.displayWidth / res.getScaledWidth_double();
	                double scaleH = mc.displayHeight / res.getScaledHeight_double();
	                GL11.glEnable(GL11.GL_SCISSOR_TEST);
	                GL11.glScissor((int)(left      * scaleW), (int)(mc.displayHeight - (bottom * scaleH)),
	                               (int)(width * scaleW), (int)(viewHeight * scaleH));
	                // Forge: background rendering moved into separate method.
	                //this.drawContainerBackground(tessellator);
	                int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
	                int l = this.top + 4 - (int)this.amountScrolled;

	                if (this.hasListHeader)
	                {
	                    this.drawListHeader(k, l, tessellator);
	                }

	                this.drawSelectionBox(k, l, mouseXIn, mouseYIn);
	                GlStateManager.disableDepth();
	                GlStateManager.enableBlend();
	                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
	                GlStateManager.disableAlpha();
	                GlStateManager.shadeModel(7425);
	                GlStateManager.disableTexture2D();
	                int j1 = this.getMaxScroll();

	                if (j1 > 0)
	                {
	                    int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
	                    k1 = MathHelper.clamp(k1, 32, this.bottom - this.top - 8);
	                    int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

	                    if (l1 < this.top)
	                    {
	                        l1 = this.top;
	                    }

	                    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	                    vertexbuffer.pos((double)i, (double)this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	                    vertexbuffer.pos((double)j, (double)this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	                    vertexbuffer.pos((double)j, (double)this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	                    vertexbuffer.pos((double)i, (double)this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	                    tessellator.draw();
	                    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	                    vertexbuffer.pos((double)i, (double)(l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	                    vertexbuffer.pos((double)j, (double)(l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	                    vertexbuffer.pos((double)j, (double)l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	                    vertexbuffer.pos((double)i, (double)l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	                    tessellator.draw();
	                    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	                    vertexbuffer.pos((double)i, (double)(l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
	                    vertexbuffer.pos((double)(j - 1), (double)(l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
	                    vertexbuffer.pos((double)(j - 1), (double)l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
	                    vertexbuffer.pos((double)i, (double)l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
	                    tessellator.draw();
	                }

	                GlStateManager.enableTexture2D();
	                GlStateManager.shadeModel(7424);
	                GlStateManager.enableAlpha();
	                GlStateManager.disableBlend();
	                GL11.glDisable(GL11.GL_SCISSOR_TEST);
	            }
	        }

	    	protected void initEntries() {
				entries = Lists.newArrayList();
				for (Station station : stations) {
					entries.add(new EntryEntries(this, station));	
				}
				entries.add(new BlankEntries(this));
			}
			@Override
			public IGuiListEntry getListEntry(int index) {
				return entries.get(index);
			}
			@Override
			protected int getSize() {
				return this.entries.size();
			}
		    protected int getScrollBarX()
		    {
		        return width / 4 * 3;
		    }

		    /**
		     * Gets the width of the list
		     */
		    public int getListWidth()
		    {
		        return width / 4 * 3;
		    }
		    public void addNewEntry(int index)
		    {
		    	entries.add(index, new EntryEntries(this, new Station()));
		        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
		    }

		    public void removeEntry(int index)
		    {
		        this.entries.remove(index);
		        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
		    }
		    protected void updateScreen()
		    {
		        for (IGuiEntryExtended entry : this.entries)
		            entry.updateCursorCounter();
		    }
		    protected void keyTyped(char eventChar, int eventKey)
		    {
		        for (IGuiEntryExtended entry : this.entries)
		            entry.keyTyped(eventChar, eventKey);
		    }
		    protected void mouseClickedPassThru(int x, int y, int mouseEvent)
		    {
		        for (IGuiEntryExtended entry : this.entries)
		            entry.mouseClicked(x, y, mouseEvent);
		    }
			protected static class EntryEntries implements IGuiEntryExtended{
				protected Station station;
				protected ListEntries parent;
				protected Minecraft client;
		        protected final GuiButtonExt btnAddNewEntryAbove;
		        protected final GuiButtonExt btnRemoveEntry;
				protected EntryEntries(ListEntries parent, Station station){
					this.parent = parent;
					client = parent.mc;
					this.station = station;
		            this.btnAddNewEntryAbove = new GuiButtonExt(0, 0, 0, 18, 18, "+");
		            this.btnAddNewEntryAbove.packedFGColour = GuiUtils.getColorCode('2', true);
		            this.btnAddNewEntryAbove.enabled = parent.getEnabled();
		            this.btnRemoveEntry = new GuiButtonExt(0, 0, 0, 18, 18, "x");
		            this.btnRemoveEntry.packedFGColour = GuiUtils.getColorCode('c', true);
		            this.btnRemoveEntry.enabled = parent.getEnabled();
				}
				
				public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}

				@Override
				public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX,
						int mouseY, boolean isSelected) {
					if(isSelected)Gui.drawRect(x, y, listWidth, y + slotHeight, 0x66FFFFFF);
					String s = TextComponentUtil.getPureText(station.stationName[0]);
					if(s.isEmpty())s = I18n.format("gui.station.nullname");
					this.client.fontRendererObj.drawString(s, x + 33 + 2, y + 1, 0xFFFFFF);
					s = TextComponentUtil.getPureText(station.stationName[1]);
					if(s.isEmpty())s = I18n.format("gui.station.nullname");
					this.client.fontRendererObj.drawString(s, x + 33 + 2, y + this.client.fontRendererObj.FONT_HEIGHT + 1, 0xFFFFFF);
					if(station.isInterchangeStation()){
						s = TextComponentUtil.getPureText( station.getInterchangeLineName()[0]);
						if(s.isEmpty())s = I18n.format("gui.station.nullname");
						String tmp = TextComponentUtil.getPureText( station.getInterchangeLineName()[1]);
						if(tmp.isEmpty())s = I18n.format("gui.station.nullname");
						s += " " + tmp;
						this.client.fontRendererObj.drawString("Interchange station of " + s, x + 33 + 2, y + client.fontRendererObj.FONT_HEIGHT * 2 + 3, 0xFFFFFF);
					}
					Gui.drawRect(x + 2, y + 7, x + 16 + 5, y + 16 + 1, 0xFF000000);
					Gui.drawRect(x + 7, y + 2, x + 16 + 1, y + 16 + 5, 0xFF000000);
					Gui.drawRect(x + 7, y + 7, x + 16 + 1, y + 16 + 1, station.amIHere() ? 0xFFAFFF00 : 0xFFFFFFFF);
	                this.btnAddNewEntryAbove.visible = true;
	                this.btnAddNewEntryAbove.xPosition = listWidth - 18-18-2-2;
	                this.btnAddNewEntryAbove.yPosition = y;
	                this.btnAddNewEntryAbove.drawButton(client, mouseX, mouseY);
	                //this.btnRemoveEntry.visible = false;
	                this.btnRemoveEntry.visible = true;
	                this.btnRemoveEntry.xPosition = listWidth - 18-2;
	                this.btnRemoveEntry.yPosition = y;
	                this.btnRemoveEntry.drawButton(client, mouseX, mouseY);
				}

				@Override
				public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX,
						int relativeY) {
		            if (this.btnAddNewEntryAbove.mousePressed(client, mouseX, mouseY))
		            {
		                btnAddNewEntryAbove.playPressSound(client.getSoundHandler());
		                parent.stations.add(slotIndex, new Station());
		                parent.addNewEntry(slotIndex);
		                //parent.recalculateState();
		                return true;
		            }
		            else if (this.btnRemoveEntry.mousePressed(client, mouseX, mouseY))
		            {
		                btnRemoveEntry.playPressSound(client.getSoundHandler());
		                parent.stations.remove(slotIndex);
		                parent.removeEntry(slotIndex);
		                //parent.recalculateState();
		                return true;
		            }
		            else{
					client.displayGuiScreen(new GuiStationEditor(parent.owner, station, slotIndex));
					return true;
		            }
				}

				public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}

				@Override
				public void keyTyped(char eventChar, int eventKey) {}

				@Override
				public void updateCursorCounter() {}

				@Override
				public void mouseClicked(int x, int y, int mouseEvent) {}
				
			}
			protected static class BlankEntries implements IGuiEntryExtended{
				protected ListEntries parent;
				protected Minecraft client;
		        //protected final GuiButtonExt btnAddNewEntryAbove;
		        //protected final GuiButtonExt btnRemoveEntry;
				protected BlankEntries(ListEntries parent){
					this.parent = parent;
					client = parent.mc;
		            /*this.btnAddNewEntryAbove = new GuiButtonExt(0, 0, 0, 18, 18, "+");
		            this.btnAddNewEntryAbove.packedFGColour = GuiUtils.getColorCode('2', true);
		            this.btnAddNewEntryAbove.enabled = parent.getEnabled();
		            this.btnRemoveEntry = new GuiButtonExt(0, 0, 0, 18, 18, "x");
		            this.btnRemoveEntry.packedFGColour = GuiUtils.getColorCode('c', true);
		            this.btnRemoveEntry.enabled = parent.getEnabled();*/
				}
				
				public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}

				@Override
				public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX,
						int mouseY, boolean isSelected) {
					if(isSelected)Gui.drawRect(x, y, listWidth, y + slotHeight, 0x66FFFFFF);
					this.client.fontRendererObj.drawString(I18n.format("gui.station.add"), x + 33 + 2, y + 1, 0xFFFFFF);
					//this.client.fontRendererObj.drawString(station.stationName[1].getFormattedText(), x + 33 + 2, y + this.client.fontRendererObj.FONT_HEIGHT + 1, 0xFFFFFF);
					/*if(station.isInterchangeStation()){
						this.client.fontRendererObj.drawString("Interchange station of " + station.getInterchangeLineName()[0].getFormattedText() + " " + station.getInterchangeLineName()[1].getFormattedText(), x + 33 + 2, y + client.fontRendererObj.FONT_HEIGHT * 2 + 3, 0xFFFFFF);
					}*/
					Gui.drawRect(x + 2, y + 7, x + 16 + 5, y + 16 + 1, 0xFF000000);
					Gui.drawRect(x + 7, y + 2, x + 16 + 1, y + 16 + 5, 0xFF000000);
					Gui.drawRect(x + 7, y + 7, x + 16 + 1, y + 16 + 1, 0xFF7F7F7F);
	                /*this.btnAddNewEntryAbove.visible = true;
	                this.btnAddNewEntryAbove.xPosition = listWidth - 22;
	                this.btnAddNewEntryAbove.yPosition = y;
	                this.btnAddNewEntryAbove.drawButton(client, mouseX, mouseY);
	                this.btnRemoveEntry.visible = false;*/
	                /*this.btnRemoveEntry.visible = true;
	                this.btnRemoveEntry.xPosition = listWidth - 22;
	                this.btnRemoveEntry.yPosition = y + 20;
	                this.btnRemoveEntry.drawButton(client, mouseX, mouseY);*/
				}

				@Override
				public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX,
						int relativeY) {
		            /*if (this.btnAddNewEntryAbove.mousePressed(client, mouseX, mouseY))
		            {
		                btnAddNewEntryAbove.playPressSound(client.getSoundHandler());*/
		                parent.stations.add(slotIndex, new Station());
		                parent.addNewEntry(slotIndex);
		                //parent.recalculateState();
		                return true;
		            /*}
		            else if (this.btnRemoveEntry.mousePressed(client, mouseX, mouseY))
		            {
		                btnRemoveEntry.playPressSound(client.getSoundHandler());
		                parent.stations.remove(slotIndex);
		                parent.removeEntry(slotIndex);
		                //parent.recalculateState();
		                return true;
		            }

		            return false;*/
				}

				public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}

				@Override
				public void keyTyped(char eventChar, int eventKey) {}

				@Override
				public void updateCursorCounter() {}

				@Override
				public void mouseClicked(int x, int y, int mouseEvent) {}
				
			}
			protected interface IGuiEntryExtended extends IGuiListEntry{
		        public void keyTyped(char eventChar, int eventKey);

		        public void updateCursorCounter();

		        public void mouseClicked(int x, int y, int mouseEvent);
			}
	    }
	}
	public static class GuiStationEditor extends GuiScreen{
		protected Station station, stationEdit;
		protected GuiStationList parent;
		protected GuiTextField stationName1TF, stationName2TF,ilName1TF,ilName2TF,ilColorTF;
		protected GuiButton doneBtn,BackBtn;
		protected GuiButtonExt hereBtn;
		protected final int index;
		public GuiStationEditor(GuiStationList parent, Station station, int index){
			this.parent = parent;
			this.station = station;
			this.index = index;
			stationEdit = new Station(station);
		}
		public void initGui() {
			Keyboard.enableRepeatEvents(true);
			loadMain();
		}
		private void loadMain(){
			this.buttonList.clear();
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;
			
			this.stationName1TF = new GuiTextField(11, this.fontRendererObj, posX  -135, posY + (-80), 130, 20);
			this.stationName1TF.setMaxStringLength(32767);
			this.stationName1TF.setText(TextComponentUtil.getPureText(station.stationName[0]));

			this.stationName2TF = new GuiTextField(12, this.fontRendererObj, posX + 5, posY + (-80), 130, 20);
			this.stationName2TF.setMaxStringLength(32767);
			this.stationName2TF.setText(TextComponentUtil.getPureText( station.stationName[1]));
			
			this.ilName1TF = new GuiTextField(13, this.fontRendererObj, posX - 135, posY + (-40), 130, 20);
			this.ilName1TF.setMaxStringLength(32767);
			this.ilName1TF.setText(TextComponentUtil.getPureText( station.getInterchangeLineName()[0]));

			this.ilName2TF = new GuiTextField(14, this.fontRendererObj, posX + 5, posY + (-40), 130, 20);
			this.ilName2TF.setMaxStringLength(32767);
			this.ilName2TF.setText(TextComponentUtil.getPureText( station.getInterchangeLineName()[1]));
			
			this.ilColorTF = new GuiTextField(15, this.fontRendererObj, posX + 5, posY + (-10), 130, 20);
			this.ilColorTF.setMaxStringLength(6);
			this.ilColorTF.setText(Integer.toHexString(station.getInterchangeLineColor()));
			
			this.buttonList.add(doneBtn = new GuiButton(0, posX - 4 - 150, this.height - 40, 150, 20, I18n.format("gui.done", new Object[0])));
			this.buttonList.add(BackBtn = new GuiButton(1, posX + 4, this.height - 40, 150, 20, I18n.format("gui.cancel", new Object[0])));
			this.buttonList.add(hereBtn = new GuiButtonExt(10, posX - 135, posY, 130, 20, Boolean.toString(station.amIHere())));
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
	    	this.stationName1TF.textboxKeyTyped(typedChar, keyCode);
	    	this.stationName2TF.textboxKeyTyped(typedChar, keyCode);
	    	this.ilName1TF.textboxKeyTyped(typedChar, keyCode);
	    	this.ilName2TF.textboxKeyTyped(typedChar, keyCode);
	    	this.ilColorTF.textboxKeyTyped(typedChar, keyCode);
	        if (keyCode != 28 && keyCode != 156)
	        {
	            if (keyCode == 1)
	            {
	                this.actionPerformed(this.BackBtn);
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
	        this.stationName1TF.mouseClicked(mouseX, mouseY, mouseButton);
	        this.stationName2TF.mouseClicked(mouseX, mouseY, mouseButton);
	        this.ilName1TF.mouseClicked(mouseX, mouseY, mouseButton);
	        this.ilName2TF.mouseClicked(mouseX, mouseY, mouseButton);
	        this.ilColorTF.mouseClicked(mouseX, mouseY, mouseButton);
	    }
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			this.drawDefaultBackground();
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;
			this.drawCenteredString(this.fontRendererObj,I18n.format("gui.editor.title.station", new Object[0]), posX, 20, 0xffffff);
			this.drawString(fontRendererObj, I18n.format("gui.station.name.1"), posX -135, posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
			this.drawString(fontRendererObj, I18n.format("gui.station.name.2"), posX + 5, posY + (-80) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
			this.drawString(fontRendererObj, I18n.format("gui.station.interchange.name.1"), posX -135, posY + (-40) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
			this.drawString(fontRendererObj, I18n.format("gui.station.interchange.name.2"), posX + 5, posY + (-40) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
			this.drawString(fontRendererObj, I18n.format("gui.station.interchange.color"), posX + 5, posY + (-10) - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
			this.drawString(fontRendererObj, I18n.format("gui.station.here"), posX -135, posY - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
			this.stationName1TF.drawTextBox();
			this.stationName2TF.drawTextBox();
			this.ilName1TF.drawTextBox();
			this.ilName2TF.drawTextBox();
		    this.ilColorTF.drawTextBox();
		    drawPreview();
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
		private void drawPreview() {
			drawRect(0, this.height / 2 + 25, this.width, this.height / 2 + 30 + 25, parent.parentScreen.teedit.getRouteColor() | 0xff000000);
			drawRect(this.width / 2 -15, this.height / 2 + 30, this.width / 2 +15, this.height / 2 + 30 + 20, 0xFF000000);
			drawRect(this.width / 2 -10, this.height / 2 + 25, this.width / 2 +10, this.height / 2 + 30 + 25, 0xFF000000);
			drawRect(this.width / 2 -10, this.height / 2 + 30, this.width / 2 +10, this.height / 2 + 30 + 20, 0xFFFFFFFF);
			drawRect(this.width / 2 - 10, this.height / 2 + 55, this.width / 2 + 10, this.height / 2 + 100, getColor(ilColorTF.getText()) | 0xff000000);
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
				stationEdit.setStationName(stationName1TF.getText(), stationName2TF.getText())
				.setInterchangeLineName(ilName1TF.getText(), ilName2TF.getText())
				.setInterchangeLineColor(ilColorTF.getText());
				station = stationEdit;
				if (parent.stationList == null) {
					parent.stationList = Lists.newArrayList();
				}
				if(parent.stationList.size() - 1 < index)
					parent.stationList.add(station);
				else if(parent.stationList.get(index) == null)
					parent.stationList.add(index, station);
				else
					parent.stationList.set(index, station);
	            this.mc.displayGuiScreen(parent);
			}

			if (button.id == 1) {
	            this.mc.displayGuiScreen(parent);
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
}
