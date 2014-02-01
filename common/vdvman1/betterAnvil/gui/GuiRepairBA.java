package vdvman1.betterAnvil.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import vdvman1.betterAnvil.BetterAnvil;
import vdvman1.betterAnvil.inventory.ContainerRepairBA;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRepairBA extends GuiContainer implements ICrafting
{
    private ContainerRepairBA repairContainer;
    private GuiTextField itemNameField;
    private InventoryPlayer playerInventory;

    public GuiRepairBA(InventoryPlayer playerInventory, World world, int x, int y, int z)
    {
        super(new ContainerRepairBA(playerInventory, world, x, y, z, Minecraft.getMinecraft().thePlayer));
        this.playerInventory = playerInventory;
        this.repairContainer = (ContainerRepairBA)this.inventorySlots;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.itemNameField = new GuiTextField(this.fontRenderer, x + 62, y + 24, 103, 12);
        this.itemNameField.setTextColor(-1);
        this.itemNameField.setDisabledTextColour(-1);
        this.itemNameField.setEnableBackgroundDrawing(false);
        this.itemNameField.setMaxStringLength(30);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.addCraftingToCrafters(this);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.repair"), 60, 6, 4210752);

        if (this.repairContainer.maximumCost > 0 || this.repairContainer.isRenamingOnly)
        {
            int colour = 8453920;
            boolean flag = true;
            String s = StatCollector.translateToLocalFormatted("container.repair.cost", new Object[] {Integer.valueOf(this.repairContainer.maximumCost)});

            /*if (!this.repairContainer.getSlot(2).getHasStack())
            {
                flag = false;
            }
            else if (!this.repairContainer.getSlot(2).canTakeStack(this.playerInventory.player))
            {
                colour = 16736352;
            }*/
            if (!this.repairContainer.getSlot(2).canTakeStack(this.playerInventory.player))
            {
                colour = 16736352;
            } 
            else if (!this.repairContainer.getSlot(2).getHasStack())
            {
                flag = false;
            }

            if (flag)
            {
                int finalColour = -16777216 | (colour & 16579836) >> 2 | colour & -16777216;
                int stringX = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
                byte stringY = 67;

                if (this.fontRenderer.getUnicodeFlag())
                {
                    drawRect(stringX - 3, stringY - 2, this.xSize - 7, stringY + 10, -16777216);
                    drawRect(stringX - 2, stringY - 1, this.xSize - 8, stringY + 9, -12895429);
                }
                else
                {
                    this.fontRenderer.drawString(s, stringX, stringY + 1, finalColour);
                    this.fontRenderer.drawString(s, stringX + 1, stringY, finalColour);
                    this.fontRenderer.drawString(s, stringX + 1, stringY + 1, finalColour);
                }

                this.fontRenderer.drawString(s, stringX, stringY, colour);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char character, int key)
    {
        if (this.itemNameField.textboxKeyTyped(character, key))
        {
            this.repairContainer.updateItemName(this.itemNameField.getText());
            this.mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload(BetterAnvil.channel, this.itemNameField.getText().getBytes()));
        }
        else
        {
            super.keyTyped(character, key);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int z)
    {
        super.mouseClicked(x, y, z);
        this.itemNameField.mouseClicked(x, y, z);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int x, int y, float z)
    {
        super.drawScreen(x, y, z);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.itemNameField.drawTextBox();
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float x, int y, int z)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/anvil.png"));
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(centerX, centerY, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(centerX + 59, centerY + 20, 0, this.ySize + (this.repairContainer.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.repairContainer.getSlot(0).getHasStack() || this.repairContainer.getSlot(1).getHasStack()) && !this.repairContainer.getSlot(2).getHasStack())
        {
            this.drawTexturedModalRect(centerX + 99, centerY + 45, this.xSize, 0, 28, 21);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
	public void sendContainerAndContentsToPlayer(Container container, List inventoryList)
    {
        this.sendSlotContents(container, 0, container.getSlot(0).getStack());
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot. Args: Container, slot number, slot contents
     */
    @Override
    public void sendSlotContents(Container container, int updateID, ItemStack itemStack)
    {
        if (updateID == 0)
        {
            this.itemNameField.setText(itemStack == null ? "" : itemStack.getDisplayName());
            this.itemNameField.setEnabled(itemStack != null); //set enabled

            if (itemStack != null)
            {
                this.repairContainer.updateItemName(this.itemNameField.getText());
                this.mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload(BetterAnvil.channel, this.itemNameField.getText().getBytes()));
            }
        }
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    @Override
    public void sendProgressBarUpdate(Container container, int updateID, int updateContent) {}
}
