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
    private InventoryPlayer field_82325_q;

    public GuiRepairBA(InventoryPlayer par1, World par2World, int par3, int par4, int par5)
    {
        super(new ContainerRepairBA(par1, par2World, par3, par4, par5, Minecraft.getMinecraft().thePlayer));
        this.field_82325_q = par1;
        this.repairContainer = (ContainerRepairBA)this.inventorySlots;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.itemNameField = new GuiTextField(this.fontRenderer, i + 62, j + 24, 103, 12);
        this.itemNameField.setTextColor(-1);
        this.itemNameField.func_82266_h(-1);
        this.itemNameField.setEnableBackgroundDrawing(false);
        this.itemNameField.setMaxStringLength(30);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.addCraftingToCrafters(this);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.repair"), 60, 6, 4210752);

        if (this.repairContainer.maximumCost > 0)
        {
            int k = 8453920;
            boolean flag = true;
            String s = StatCollector.translateToLocalFormatted("container.repair.cost", new Object[] {Integer.valueOf(this.repairContainer.maximumCost)});

            if (!this.repairContainer.getSlot(2).getHasStack())
            {
                flag = false;
            }
            else if (!this.repairContainer.getSlot(2).canTakeStack(this.field_82325_q.player))
            {
                k = 16736352;
            }

            if (flag)
            {
                int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
                int i1 = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
                byte b0 = 67;

                if (this.fontRenderer.getUnicodeFlag())
                {
                    drawRect(i1 - 3, b0 - 2, this.xSize - 7, b0 + 10, -16777216);
                    drawRect(i1 - 2, b0 - 1, this.xSize - 8, b0 + 9, -12895429);
                }
                else
                {
                    this.fontRenderer.drawString(s, i1, b0 + 1, l);
                    this.fontRenderer.drawString(s, i1 + 1, b0, l);
                    this.fontRenderer.drawString(s, i1 + 1, b0 + 1, l);
                }

                this.fontRenderer.drawString(s, i1, b0, k);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (this.itemNameField.textboxKeyTyped(par1, par2))
        {
            this.repairContainer.updateItemName(this.itemNameField.getText());
            this.mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload(BetterAnvil.channel, this.itemNameField.getText().getBytes()));
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.itemNameField.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.itemNameField.drawTextBox();
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/gui/repair.png");
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k + 59, l + 20, 0, this.ySize + (this.repairContainer.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.repairContainer.getSlot(0).getHasStack() || this.repairContainer.getSlot(1).getHasStack()) && !this.repairContainer.getSlot(2).getHasStack())
        {
            this.drawTexturedModalRect(k + 99, l + 45, this.xSize, 0, 28, 21);
        }
    }

    @SuppressWarnings("rawtypes")
	public void sendContainerAndContentsToPlayer(Container par1Container, List par2List)
    {
        this.sendSlotContents(par1Container, 0, par1Container.getSlot(0).getStack());
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot. Args: Container, slot number, slot contents
     */
    public void sendSlotContents(Container par1Container, int par2, ItemStack par3ItemStack)
    {
        if (par2 == 0)
        {
            this.itemNameField.setText(par3ItemStack == null ? "" : par3ItemStack.getDisplayName());
            this.itemNameField.func_82265_c(par3ItemStack != null);

            if (par3ItemStack != null)
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
    public void sendProgressBarUpdate(Container par1Container, int par2, int par3) {}
}
