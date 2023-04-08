/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.network.chat.Component
 */
package ca.landonjw.gooeylibs2.api.page;

import ca.landonjw.gooeylibs2.api.page.PageAction;
import ca.landonjw.gooeylibs2.api.page.PageBase;
import ca.landonjw.gooeylibs2.api.template.Template;
import ca.landonjw.gooeylibs2.api.template.types.InventoryTemplate;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public class GooeyPage
extends PageBase {
    private final Consumer<PageAction> onOpen;
    private final Consumer<PageAction> onClose;

    public GooeyPage(@Nonnull Template template, @Nullable InventoryTemplate inventoryTemplate, @Nullable Component title, @Nullable Consumer<PageAction> onOpen, @Nullable Consumer<PageAction> onClose) {
        super(template, inventoryTemplate, title);
        this.onOpen = onOpen;
        this.onClose = onClose;
    }

    @Override
    public void onOpen(@Nonnull PageAction action) {
        if (this.onOpen != null) {
            this.onOpen.accept(action);
        }
    }

    @Override
    public void onClose(@Nonnull PageAction action) {
        if (this.onClose != null) {
            this.onClose.accept(action);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Component title;
        protected Template template;
        protected InventoryTemplate inventoryTemplate;
        protected Consumer<PageAction> onOpen;
        protected Consumer<PageAction> onClose;

        public Builder title(@Nullable String title) {
            if (title == null) {
                return this;
            }
            return this.title(Component.literal(title));
        }

        public Builder title(@Nullable Component title) {
            this.title = title;
            return this;
        }

        public Builder template(@Nonnull Template template) {
            if (template instanceof InventoryTemplate) {
                throw new IllegalArgumentException("you can not use an inventory template here!");
            }
            this.template = template;
            return this;
        }

        public Builder inventory(@Nullable InventoryTemplate template) {
            this.inventoryTemplate = template;
            return this;
        }

        public Builder onOpen(@Nullable Consumer<PageAction> behaviour) {
            this.onOpen = behaviour;
            return this;
        }

        public Builder onOpen(@Nullable Runnable behaviour) {
            if (behaviour == null) {
                this.onOpen = null;
            } else {
                this.onOpen((PageAction action) -> behaviour.run());
            }
            return this;
        }

        public Builder onClose(@Nullable Consumer<PageAction> behaviour) {
            this.onClose = behaviour;
            return this;
        }

        public Builder onClose(@Nullable Runnable behaviour) {
            if (behaviour == null) {
                this.onClose = null;
            } else {
                this.onClose((PageAction action) -> behaviour.run());
            }
            return this;
        }

        public GooeyPage build() {
            this.validate();
            return new GooeyPage(this.template, this.inventoryTemplate, this.title, this.onOpen, this.onClose);
        }

        protected void validate() {
            if (this.template == null) {
                throw new IllegalStateException("template must be defined");
            }
        }
    }
}

