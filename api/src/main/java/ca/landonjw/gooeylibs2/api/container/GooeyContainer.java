package ca.landonjw.gooeylibs2.api.container;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.ButtonClick;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.moveable.Movable;
import ca.landonjw.gooeylibs2.api.button.moveable.MovableButtonAction;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.page.PageAction;
import ca.landonjw.gooeylibs2.api.tasks.Task;
import ca.landonjw.gooeylibs2.api.template.Template;
import ca.landonjw.gooeylibs2.api.template.slot.TemplateSlot;
import ca.landonjw.gooeylibs2.api.template.slot.TemplateSlotDelegate;
import ca.landonjw.gooeylibs2.api.template.types.InventoryTemplate;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class GooeyContainer extends AbstractContainerMenu {
    private final MinecraftServer server;
    private final ServerPlayer player;
    private final Container container;
    private final Page page;
    public InventoryTemplate inventoryTemplate;
    private long lastClickTick;
    private boolean closing;
    private Button cursorButton;

    public GooeyContainer(@Nonnull ServerPlayer player, @Nonnull Page page) {
        super(page.getTemplate().getTemplateType().getContainerType(page.getTemplate()), 1);
        this.server = player.level.getServer();
        this.player = player;
        this.page = page;
        this.inventoryTemplate = page.getInventoryTemplate().orElse(null);
        this.container = new SimpleContainer(page.getTemplate().getSize() + 36);
        this.bindSlots();
        this.bindPage();
    }

    private void bindPage() {
        this.page.subscribe(this, this::refresh);
    }

    public void refresh() {
        this.unbindSlots();
        this.inventoryTemplate = this.page.getInventoryTemplate().orElse(null);
        this.bindSlots();
        this.openWindow();
    }

    private void bindSlots() {

        List<TemplateSlotDelegate> delegates = this.page.getTemplate().getSlots();
        int slotIndex = 0;
        for (int i = 0; i < delegates.size(); ++i) {
            int index = i;
            TemplateSlotDelegate delegate = delegates.get(i);
            TemplateSlot slot = new TemplateSlot(this.container, delegate, 0, 0);
            delegate.subscribe(this, () -> this.updateSlotStack(index, this.getItemAtSlot(index), false));
            this.addSlot(slot);
            this.container.setItem(slotIndex++, slot.getItem());
        }
        if (this.inventoryTemplate == null) {
            for (int i = 9; i < 36; ++i) {
                GooeyButton button = GooeyButton.of(this.player.getInventory().items.get(i));
                TemplateSlotDelegate delegate = new TemplateSlotDelegate(button, i - 9);
                this.addSlot(new TemplateSlot(this.container, delegate, 0, 0));
                this.container.setItem(slotIndex++, button.getDisplay());
            }
            for (int i = 0; i < 9; ++i) {
                GooeyButton button = GooeyButton.of(this.player.getInventory().items.get(i));
                TemplateSlotDelegate delegate = new TemplateSlotDelegate(button, i + 27);
                this.addSlot(new TemplateSlot(this.container, delegate, 0, 0));
                this.container.setItem(slotIndex++, button.getDisplay());
            }
        } else {
            for (int i = 0; i < this.inventoryTemplate.getSize(); ++i) {
                int index = i;
                int itemSlot = i + this.page.getTemplate().getSize();
                TemplateSlotDelegate delegate = this.inventoryTemplate.getSlot(i);
                TemplateSlot slot = new TemplateSlot(this.container, delegate, 0, 0);
                delegate.subscribe(this, () -> this.updateSlotStack(index, this.getItemAtSlot(itemSlot), true));
                this.addSlot(slot);
                this.container.setItem(itemSlot, slot.getItem());
            }
        }
    }

    private void unbindSlots() {
        this.slots.forEach(slot -> ((TemplateSlot) slot).getDelegate().unsubscribe(this));
        this.inventoryTemplate.getSlots().forEach(delegate -> delegate.unsubscribe(this));
    }

    private void updateSlotStack(int index, ItemStack stack, boolean playerInventory) {
        this.player.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.player.containerMenu.getStateId(), playerInventory ? this.page.getTemplate().getSize() + index : index, stack));
    }

    private int getTemplateIndex(int slotIndex) {
        if (this.isSlotInPlayerInventory(slotIndex)) {
            return slotIndex - this.page.getTemplate().getSize();
        }
        return slotIndex;
    }

    private Template getTemplateFromIndex(int slotIndex) {
        if (this.isSlotInPlayerInventory(slotIndex)) {
            return this.inventoryTemplate;
        }
        return this.page.getTemplate();
    }

    private boolean isSlotInPlayerInventory(int slot) {
        int templateSize = this.page.getTemplate().getSize();
        return slot >= templateSize && slot - templateSize < this.player.containerMenu.slots.size();
    }

    private ItemStack getItemAtSlot(int slot) {
        if (slot == -999 || slot >= this.slots.size()) {
            return ItemStack.EMPTY;
        }
        return this.slots.get(slot).getItem();
    }

    private TemplateSlotDelegate getReference(int slot) {
        if (slot < 0) {
            return null;
        }
        if (slot >= this.page.getTemplate().getSize()) {
            int targetedPlayerSlotIndex = slot - this.page.getTemplate().getSize();
            if (this.inventoryTemplate != null) {
                return this.inventoryTemplate.getSlot(targetedPlayerSlotIndex);
            }
            return null;
        }
        return this.page.getTemplate().getSlot(slot);
    }

    public void open() {
        this.player.closeContainer();
        this.player.containerMenu = this;
        this.player.containerCounter = this.player.containerMenu.containerId;
        this.openWindow();
        this.page.onOpen(new PageAction(this.player, this.page));
    }

    private void openWindow() {
        ClientboundOpenScreenPacket openWindow = new ClientboundOpenScreenPacket(this.player.containerCounter, this.page.getTemplate().getTemplateType().getContainerType(this.page.getTemplate()), this.page.getTitle());
        this.player.connection.send(openWindow);
        this.updateAllContainerContents();
        this.setPlayersCursor(ItemStack.EMPTY);
    }

    private void patchDesyncs(int slot, ClickType clickType) {
        if (clickType == ClickType.PICKUP || clickType == ClickType.CLONE || clickType == ClickType.THROW) {
            this.updateSlotStack(this.getTemplateIndex(slot), this.getItemAtSlot(slot), this.isSlotInPlayerInventory(slot));
        } else if (clickType == ClickType.QUICK_MOVE || clickType == ClickType.PICKUP_ALL) {
            this.updateAllContainerContents();
        }
    }

    public ItemStack quickMoveStack(@NotNull Player player, int i) {
        return ItemStack.EMPTY;
    }

    public void clicked(int slot, int dragType, @NotNull ClickType type, @NotNull Player player) {
        if (slot == -1 || slot == -999) {
            return;
        }
        Slot target = this.slots.get(slot);
        if (this.lastClickTick == (long)this.server.getTickCount()) {
            if (type == ClickType.PICKUP && this.cursorButton != null) {
                ItemStack clickedItem = this.getItemAtSlot(slot);
                ItemStack cursorItem = this.cursorButton.getDisplay();
                if (clickedItem.getItem() == cursorItem.getItem() && ItemStack.isSame(clickedItem, cursorItem)) {
                    ItemStack copy = this.getItemAtSlot(slot).copy();
                    copy.setCount(copy.getCount() + this.cursorButton.getDisplay().getCount());
                    target.onTake(this.player, copy);
                }
                return;
            }
            return;
        }
        this.lastClickTick = this.server.getTickCount();
        if (type == ClickType.QUICK_CRAFT && dragType == 8) {
            Task.builder().execute(() -> {
                this.updateAllContainerContents();
                this.setPlayersCursor(this.cursorButton != null ? this.cursorButton.getDisplay() : ItemStack.EMPTY);
            }).build();
            return;
        }
        this.patchDesyncs(slot, type);
        Button button = this.getButton(slot);
        if (button instanceof Movable || this.cursorButton != null) {
            this.handleMovableButton(slot, dragType, type);
            return;
        }
        this.setPlayersCursor(ItemStack.EMPTY);
        if (type == ClickType.SWAP) {
            ItemStack inventory = this.player.getInventory().getItem(dragType);
            this.updateSlotStack(27 + dragType, inventory, true);
            this.updateSlotStack(slot, this.getItemAtSlot(slot), false);
        }
        if (type == ClickType.QUICK_CRAFT) {
            this.updateSlotStack(this.getTemplateIndex(slot), ItemStack.EMPTY, this.isSlotInPlayerInventory(slot));
            return;
        }
        ButtonClick buttonClickType = this.getButtonClickType(type, dragType);
        if (button != null) {
            ButtonAction action = new ButtonAction(this.player, buttonClickType, button, this.page.getTemplate(), this.page, slot);
            button.onClick(action);
        }
    }

    private ButtonClick getButtonClickType(ClickType type, int dragType) {
        return switch (type) {
            case PICKUP -> {
                if (dragType == 0) {
                    yield ButtonClick.LEFT_CLICK;
                }
                yield ButtonClick.RIGHT_CLICK;
            }
            case CLONE -> ButtonClick.MIDDLE_CLICK;
            case QUICK_MOVE -> {
                if (dragType == 0) {
                    yield ButtonClick.SHIFT_LEFT_CLICK;
                }
                yield ButtonClick.SHIFT_RIGHT_CLICK;
            }
            case THROW -> ButtonClick.THROW;
            default -> ButtonClick.OTHER;
        };
    }

    private void handleMovableButton(int slot, int dragType, ClickType clickType) {
        if (clickType == ClickType.QUICK_CRAFT && slot == -999) {
            return;
        }
        Template template = this.getTemplateFromIndex(slot);
        int targetTemplateSlot = this.getTemplateIndex(slot);
        if (template == null) {
            if (clickType == ClickType.PICKUP && this.isSlotOccupied(slot)) {
                this.setPlayersCursor(this.cursorButton != null ? this.cursorButton.getDisplay() : ItemStack.EMPTY);
                return;
            }
            if (clickType == ClickType.QUICK_CRAFT) {
                this.updateSlotStack(this.getTemplateIndex(slot), this.getItemAtSlot(slot), true);
            }
            if (this.cursorButton != null) {
                this.setPlayersCursor(this.cursorButton.getDisplay());
            }
        } else {
            Button clickedButton = this.getButton(slot);
            if (this.cursorButton == null) {
                if (slot == -999) {
                    return;
                }
                this.setPlayersCursor(this.getItemAtSlot(slot));
                if (clickedButton == null) {
                    return;
                }
                if (clickType == ClickType.QUICK_CRAFT && dragType == 9) {
                    this.setPlayersCursor(ItemStack.EMPTY);
                    return;
                }
                ButtonClick click = this.getButtonClickType(clickType, dragType);
                MovableButtonAction action = new MovableButtonAction(this.player, click, clickedButton, template, this.page, targetTemplateSlot);
                clickedButton.onClick(action);
                ((Movable)clickedButton).onPickup(action);
                if (action.isCancelled()) {
                    this.setPlayersCursor(ItemStack.EMPTY);
                    this.updateSlotStack(targetTemplateSlot, clickedButton.getDisplay(), template instanceof InventoryTemplate);
                } else {
                    this.cursorButton = clickedButton;
                    this.setButton(slot, null);
                    if (clickType == ClickType.CLONE || clickType == ClickType.QUICK_MOVE || clickType == ClickType.THROW) {
                        this.setPlayersCursor(this.cursorButton.getDisplay());
                    }
                }
            } else {
                if (clickType == ClickType.PICKUP_ALL || slot == -999) {
                    this.setPlayersCursor(this.cursorButton.getDisplay());
                    return;
                }
                if (this.isSlotOccupied(slot)) {
                    this.setPlayersCursor(this.cursorButton.getDisplay());
                } else {
                    ButtonClick click = this.getButtonClickType(clickType, dragType);
                    MovableButtonAction action = new MovableButtonAction(this.player, click, this.cursorButton, template, this.page, targetTemplateSlot);
                    this.cursorButton.onClick(action);
                    ((Movable)this.cursorButton).onDrop(action);
                    if (action.isCancelled()) {
                        if (clickType == ClickType.CLONE) {
                            return;
                        }
                        this.setPlayersCursor(this.cursorButton.getDisplay());
                        this.updateSlotStack(targetTemplateSlot, ItemStack.EMPTY, template instanceof InventoryTemplate);
                    } else {
                        this.setButton(slot, this.cursorButton);
                        this.cursorButton = null;
                        this.setPlayersCursor(ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    private boolean isSlotOccupied(int slot) {
        if (this.isSlotInPlayerInventory(slot) && this.inventoryTemplate == null) {
            return this.player.containerMenu.slots.get(this.getTemplateIndex(slot) + 9).hasItem();
        }
        return this.getButton(slot) != null;
    }

    public Page getPage() {
        return this.page;
    }

    private void updateAllContainerContents() {
        this.refresh(this.player, this.getItems());
        this.player.containerMenu.broadcastChanges();
        if (this.inventoryTemplate != null) {
            this.refresh(this.player, this.inventoryTemplate.getFullDisplay(this.player));
        } else {
            this.refresh(this.player, this.player.containerMenu.getItems());
        }
    }

    private void refresh(ServerPlayer player, NonNullList<ItemStack> contents) {
        player.connection.send(new ClientboundContainerSetContentPacket(player.containerMenu.containerId, player.containerMenu.getStateId(), contents, player.getItemInHand(InteractionHand.MAIN_HAND)));
    }

    private void setPlayersCursor(ItemStack stack) {
        ClientboundContainerSetSlotPacket setCursorSlot = new ClientboundContainerSetSlotPacket(-1, this.player.containerMenu.getStateId(), 0, stack);
        this.player.connection.send(setCursorSlot);
    }

    private void setButton(int slot, Button button) {
        if (slot < 0) {
            return;
        }
        ((TemplateSlot)this.getSlot(slot)).setButton(button);
    }

    public void removed(@NotNull Player player) {
        if (this.closing) {
            return;
        }
        this.closing = true;
        this.page.onClose(new PageAction(this.player, this.page));
        this.page.unsubscribe(this);
        this.slots.forEach(slot -> ((TemplateSlot) slot).getDelegate().unsubscribe(this));
        super.removed(player);
        player.containerMenu.broadcastChanges();
        this.refresh(this.player, this.player.containerMenu.getItems());
    }

    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    private Button getButton(int slot) {
        if (slot < 0) {
            return null;
        }
        if (slot >= this.page.getTemplate().getSize()) {
            int targetedPlayerSlotIndex = slot - this.page.getTemplate().getSize();
            if (this.inventoryTemplate != null) {
                return this.inventoryTemplate.getSlot(targetedPlayerSlotIndex).getButton().orElse(null);
            }
            return null;
        }
        return this.page.getTemplate().getSlot(slot).getButton().orElse(null);
    }
}

