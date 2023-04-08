/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.network.chat.Component
 */
package ca.landonjw.gooeylibs2.api.page;

import ca.landonjw.gooeylibs2.api.data.EventEmitter;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.Template;
import ca.landonjw.gooeylibs2.api.template.types.InventoryTemplate;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public abstract class PageBase
implements Page {
    private final EventEmitter<Page> eventEmitter = new EventEmitter();
    private Template template;
    private InventoryTemplate inventoryTemplate;
    private Component title;

    public PageBase(@Nonnull Template template, @Nullable InventoryTemplate inventoryTemplate, @Nullable Component title) {
        this.template = template;
        this.inventoryTemplate = inventoryTemplate;
        this.title = title != null ? title : Component.empty();
    }

    @Override
    public Template getTemplate() {
        if (this.template == null) {
            throw new IllegalStateException("template could not be found on the page!");
        }
        return this.template;
    }

    public void setTemplate(@Nonnull Template template) {
        this.template = template;
        this.update();
    }

    @Override
    public Optional<InventoryTemplate> getInventoryTemplate() {
        return Optional.ofNullable(this.inventoryTemplate);
    }

    public void setPlayerInventoryTemplate(@Nullable InventoryTemplate inventoryTemplate) {
        this.inventoryTemplate = inventoryTemplate;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    public void setTitle(@Nullable String title) {
        this.setTitle(title == null ? null : Component.literal(title));
    }

    public void setTitle(@Nullable Component title) {
        this.title = title == null ? Component.empty() : title;
        this.update();
    }

    @Override
    public void subscribe(@Nonnull Object observer, @Nonnull Consumer<Page> consumer) {
        this.eventEmitter.subscribe(observer, consumer);
    }

    @Override
    public void unsubscribe(@Nonnull Object observer) {
        this.eventEmitter.unsubscribe(observer);
    }

    public void update() {
        this.eventEmitter.emit(this);
    }
}

