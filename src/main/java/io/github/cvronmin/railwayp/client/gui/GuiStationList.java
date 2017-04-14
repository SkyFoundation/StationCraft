package io.github.cvronmin.railwayp.client.gui;

import com.google.common.collect.Lists;
import io.github.cvronmin.railwayp.tileentity.TileEntityRouteSignage;
import io.github.cvronmin.railwayp.util.TextComponentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

public class GuiStationList extends GuiScreen {
    protected GuiRouteSignageEditor parentScreen;
    protected ListEntries entryList;
    protected List<TileEntityRouteSignage.Station> stationList;
    private GuiButtonExt doneBtn;

    public GuiStationList(GuiRouteSignageEditor screen, List<TileEntityRouteSignage.Station> list) {
        parentScreen = screen;
        stationList = list;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        this.entryList = new ListEntries(this);

        int undoGlyphWidth = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
        int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
        int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
        int undoWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
        int resetWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
        this.buttonList.add(doneBtn = new GuiButtonExt(1, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    @Override
    protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == Keyboard.KEY_ESCAPE)
            this.mc.displayGuiScreen(parentScreen);
        else if (eventKey == Keyboard.KEY_RETURN)
            actionPerformed(doneBtn);
        else
            this.entryList.keyTyped(eventChar, eventKey);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            /*try {
                this.parentScreen.getStations().clear();
                this.parentScreen.getStations().addAll(stationList);
            } catch (Throwable e) {
                e.printStackTrace();
            }*/
            this.mc.displayGuiScreen(this.parentScreen);
        }

    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.entryList.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     *
     * @param mouseX       Mouse x coordinate
     * @param mouseY       Mouse y coordinate
     * @param partialTicks How far into the current tick (1/20th of a second) the game is
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //this.drawDefaultBackground();
        this.entryList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.editor.title.stations"), this.width / 2, 8, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
        //this.entryList.drawScreenPost(mouseX, mouseY, partialTicks);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
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
    protected void mouseClicked(int x, int y, int mouseEvent) throws IOException {
        if (mouseEvent != 0 || !this.entryList.mouseClicked(x, y, mouseEvent)) {
            this.entryList.mouseClickedPassThru(x, y, mouseEvent);
            super.mouseClicked(x, y, mouseEvent);
        }
    }

    /**
     * Called when a mouse button is released.
     *
     * @param mouseX Current mouse x coordinate
     * @param mouseY Current mouse y coordinate
     * @param state  The mouse button that was released
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state != 0 || !this.entryList.mouseReleased(mouseX, mouseY, state)) {
            super.mouseReleased(mouseX, mouseY, state);
        }
    }

    public static class ListEntries extends GuiListExtended {
        protected GuiStationList owner;
        protected List<TileEntityRouteSignage.Station> stations;
        protected List<ListEntries.IGuiEntryExtended> entries;

        public ListEntries(GuiStationList owner) {
            super(owner.mc, owner.width, owner.height, 33, owner.height - 32, 36);
            this.owner = owner;
            stations = owner.stationList;
            initEntries();
        }

        @Override
        protected void drawBackground() {
            //owner.drawDefaultBackground();
            owner.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        }

        @Override
        public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
            if (this.visible) {
                this.mouseX = mouseXIn;
                this.mouseY = mouseYIn;
                this.drawBackground();
                int i = this.getScrollBarX();
                int j = i + 6;
                int viewHeight = this.bottom - this.top;
                this.bindAmountScrolled();
                GlStateManager.disableLighting();
                GlStateManager.disableFog();
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();
                ScaledResolution res = new ScaledResolution(mc);
                double scaleW = mc.displayWidth / res.getScaledWidth_double();
                double scaleH = mc.displayHeight / res.getScaledHeight_double();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glScissor((int) (left * scaleW), (int) (mc.displayHeight - (bottom * scaleH)),
                        (int) (width * scaleW), (int) (viewHeight * scaleH));
                // Forge: background rendering moved into separate method.
                //this.drawContainerBackground(tessellator);
                int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                int l = this.top + 4 - (int) this.amountScrolled;

                if (this.hasListHeader) {
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

                if (j1 > 0) {
                    int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                    k1 = MathHelper.clamp(k1, 32, this.bottom - this.top - 8);
                    int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

                    if (l1 < this.top) {
                        l1 = this.top;
                    }

                    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    vertexbuffer.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                    vertexbuffer.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                    vertexbuffer.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                    vertexbuffer.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                    tessellator.draw();
                    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    vertexbuffer.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                    vertexbuffer.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                    vertexbuffer.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                    vertexbuffer.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                    tessellator.draw();
                    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    vertexbuffer.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                    vertexbuffer.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                    vertexbuffer.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                    vertexbuffer.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
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
            for (TileEntityRouteSignage.Station station : stations) {
                entries.add(new ListEntries.EntryEntries(this, station));
            }
            entries.add(new ListEntries.BlankEntries(this));
        }

        @Override
        public IGuiListEntry getListEntry(int index) {
            return entries.get(index);
        }

        @Override
        protected int getSize() {
            return this.entries.size();
        }

        protected int getScrollBarX() {
            return width / 4 * 3;
        }

        /**
         * Gets the width of the list
         */
        public int getListWidth() {
            return width / 4 * 3;
        }

        public void addNewEntry(int index) {
            entries.add(index, new ListEntries.EntryEntries(this, new TileEntityRouteSignage.Station()));
            keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
        }

        public void removeEntry(int index) {
            this.entries.remove(index);
            keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
        }

        protected void updateScreen() {
            for (ListEntries.IGuiEntryExtended entry : this.entries)
                entry.updateCursorCounter();
        }

        protected void keyTyped(char eventChar, int eventKey) {
            for (ListEntries.IGuiEntryExtended entry : this.entries)
                entry.keyTyped(eventChar, eventKey);
        }

        protected void mouseClickedPassThru(int x, int y, int mouseEvent) {
            for (ListEntries.IGuiEntryExtended entry : this.entries)
                entry.mouseClicked(x, y, mouseEvent);
        }

        protected static class EntryEntries implements ListEntries.IGuiEntryExtended {
            protected TileEntityRouteSignage.Station station;
            protected ListEntries parent;
            protected Minecraft client;
            protected final GuiButtonExt btnAddNewEntryAbove;
            protected final GuiButtonExt btnRemoveEntry;

            protected EntryEntries(ListEntries parent, TileEntityRouteSignage.Station station) {
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

            public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
            }

            @Override
            public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX,
                                  int mouseY, boolean isSelected) {
                if (isSelected) Gui.drawRect(x, y, listWidth, y + slotHeight, 0x66FFFFFF);
                String s = TextComponentUtil.getPureText(station.stationName[0]);
                if (s.isEmpty()) s = I18n.format("gui.station.nullname");
                this.client.fontRenderer.drawString(s, x + 33 + 2, y + 1, 0xFFFFFF);
                s = TextComponentUtil.getPureText(station.stationName[1]);
                if (s.isEmpty()) s = I18n.format("gui.station.nullname");
                this.client.fontRenderer.drawString(s, x + 33 + 2, y + this.client.fontRenderer.FONT_HEIGHT + 1, 0xFFFFFF);
                if (station.isInterchangeStation()) {
                    if(station.getInterchangeLines().size() > 1){
                        s = I18n.format("gui.station.interchange.multi", station.getInterchangeLines().size());
                    }
                    else {
                        s = TextComponentUtil.getPureText(station.getInterchangeLines().get(0).getLineName()[0]);
                        if (s.isEmpty()) s = I18n.format("gui.station.nullname");
                        String tmp = TextComponentUtil.getPureText(station.getInterchangeLines().get(0).getLineName()[1]);
                        if (tmp.isEmpty()) s = I18n.format("gui.station.nullname");
                        s += " " + tmp;
                    }
                    this.client.fontRenderer.drawString(I18n.format("gui.station.interchange", s), x + 33 + 2, y + client.fontRenderer.FONT_HEIGHT * 2 + 3, 0xFFFFFF);
                }
                Gui.drawRect(x + 2, y + 7, x + 16 + 5, y + 16 + 1, 0xFF000000);
                Gui.drawRect(x + 7, y + 2, x + 16 + 1, y + 16 + 5, 0xFF000000);
                Gui.drawRect(x + 7, y + 7, x + 16 + 1, y + 16 + 1, station.amIHere() ? 0xFFAFFF00 : 0xFFFFFFFF);
                this.btnAddNewEntryAbove.visible = true;
                this.btnAddNewEntryAbove.xPosition = listWidth - 18 - 18 - 2 - 2;
                this.btnAddNewEntryAbove.yPosition = y;
                this.btnAddNewEntryAbove.drawButton(client, mouseX, mouseY);
                //this.btnRemoveEntry.visible = false;
                this.btnRemoveEntry.visible = true;
                this.btnRemoveEntry.xPosition = listWidth - 18 - 2;
                this.btnRemoveEntry.yPosition = y;
                this.btnRemoveEntry.drawButton(client, mouseX, mouseY);
            }

            @Override
            public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX,
                                        int relativeY) {
                if (this.btnAddNewEntryAbove.mousePressed(client, mouseX, mouseY)) {
                    btnAddNewEntryAbove.playPressSound(client.getSoundHandler());
                    parent.stations.add(slotIndex, new TileEntityRouteSignage.Station());
                    parent.addNewEntry(slotIndex);
                    //parent.recalculateState();
                    return true;
                } else if (this.btnRemoveEntry.mousePressed(client, mouseX, mouseY)) {
                    btnRemoveEntry.playPressSound(client.getSoundHandler());
                    parent.stations.remove(slotIndex);
                    parent.removeEntry(slotIndex);
                    //parent.recalculateState();
                    return true;
                } else {
                    client.displayGuiScreen(new GuiStationEditor(parent.owner, station, slotIndex));
                    return true;
                }
            }

            public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            }

            @Override
            public void keyTyped(char eventChar, int eventKey) {
            }

            @Override
            public void updateCursorCounter() {
            }

            @Override
            public void mouseClicked(int x, int y, int mouseEvent) {
            }

        }

        protected static class BlankEntries implements ListEntries.IGuiEntryExtended {
            protected ListEntries parent;
            protected Minecraft client;

            //protected final GuiButtonExt btnAddNewEntryAbove;
            //protected final GuiButtonExt btnRemoveEntry;
            protected BlankEntries(ListEntries parent) {
                this.parent = parent;
                client = parent.mc;
                /*this.btnAddNewEntryAbove = new GuiButtonExt(0, 0, 0, 18, 18, "+");
                this.btnAddNewEntryAbove.packedFGColour = GuiUtils.getColorCode('2', true);
                this.btnAddNewEntryAbove.enabled = parent.getEnabled();
                this.btnRemoveEntry = new GuiButtonExt(0, 0, 0, 18, 18, "x");
                this.btnRemoveEntry.packedFGColour = GuiUtils.getColorCode('c', true);
                this.btnRemoveEntry.enabled = parent.getEnabled();*/
            }

            public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
            }

            @Override
            public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX,
                                  int mouseY, boolean isSelected) {
                if (isSelected) Gui.drawRect(x, y, listWidth, y + slotHeight, 0x66FFFFFF);
                this.client.fontRenderer.drawString(I18n.format("gui.station.add"), x + 33 + 2, y + 1, 0xFFFFFF);
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
                parent.stations.add(slotIndex, new TileEntityRouteSignage.Station());
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

            public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            }

            @Override
            public void keyTyped(char eventChar, int eventKey) {
            }

            @Override
            public void updateCursorCounter() {
            }

            @Override
            public void mouseClicked(int x, int y, int mouseEvent) {
            }

        }

        protected interface IGuiEntryExtended extends IGuiListEntry {
            public void keyTyped(char eventChar, int eventKey);

            public void updateCursorCounter();

            public void mouseClicked(int x, int y, int mouseEvent);
        }
    }
}
