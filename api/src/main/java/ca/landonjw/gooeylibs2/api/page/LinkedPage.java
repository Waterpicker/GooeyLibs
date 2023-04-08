package ca.landonjw.gooeylibs2.api.page;

import ca.landonjw.gooeylibs2.api.template.Template;
import ca.landonjw.gooeylibs2.api.template.types.InventoryTemplate;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LinkedPage
extends GooeyPage {
    public static final String CURRENT_PAGE_PLACEHOLDER = "{current}";
    public static final String TOTAL_PAGES_PLACEHOLDER = "{total}";
    private LinkedPage previous;
    private LinkedPage next;

    public LinkedPage(@Nonnull Template template, @Nullable InventoryTemplate inventoryTemplate, @Nullable Component title, @Nullable Consumer<PageAction> onOpen, @Nullable Consumer<PageAction> onClose, @Nullable LinkedPage previous, @Nullable LinkedPage next) {
        super(template, inventoryTemplate, title, onOpen, onClose);
        this.previous = previous;
        this.next = next;
    }

    public LinkedPage getPrevious() {
        return this.previous;
    }

    public void setPrevious(LinkedPage previous) {
        this.previous = previous;
        this.update();
    }

    public LinkedPage getNext() {
        return this.next;
    }

    public void setNext(LinkedPage next) {
        this.next = next;
        this.update();
    }

    public int getCurrentPage() {
        return this.previous != null ? this.previous.getCurrentPage() + 1 : 1;
    }

    public int getTotalPages() {
        return this.next != null ? this.next.getTotalPages() : this.getCurrentPage();
    }

    @Override
    public Component getTitle() {
        return this.replace(this.replace(super.getTitle(), Pattern.compile(CURRENT_PAGE_PLACEHOLDER, 16), "" + this.getCurrentPage()), Pattern.compile(TOTAL_PAGES_PLACEHOLDER, 16), "" + this.getTotalPages());
    }

    @Override
    public Template getTemplate() {
        return super.getTemplate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private Component replace(Component parent, Pattern pattern, String replacement) {
        MutableComponent result;
        if (parent instanceof MutableComponent stc) {
            String content = stc.getString();
            if (!content.isEmpty()) {
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    content = matcher.replaceAll(replacement);
                }
                result = Component.literal(content);
                result.setStyle(parent.getStyle());
            } else {
                result = Component.literal(stc.getString());
                result.setStyle(parent.getStyle());
            }
        } else {
            result = parent.copy();
            result.setStyle(parent.getStyle());
        }
        List< MutableComponent> children = parent.getSiblings().stream().filter(c -> c instanceof MutableComponent).map(MutableComponent.class::cast).toList();
        for (MutableComponent child : children) {
            result.append(this.replace(child, pattern, replacement));
        }
        return result;
    }

    public static class Builder
    extends GooeyPage.Builder {
        protected LinkedPage previousPage;
        protected LinkedPage nextPage;

        @Override
        public Builder title(@Nullable String title) {
            super.title(title);
            return this;
        }

        @Override
        public Builder title(@Nullable Component title) {
            super.title(title);
            return this;
        }

        @Override
        public Builder template(@Nonnull Template template) {
            super.template(template);
            return this;
        }

        @Override
        public Builder inventory(@Nullable InventoryTemplate template) {
            super.inventory(template);
            return this;
        }

        @Override
        public Builder onOpen(@Nullable Consumer<PageAction> behaviour) {
            super.onOpen(behaviour);
            return this;
        }

        @Override
        public Builder onOpen(@Nullable Runnable behaviour) {
            super.onOpen(behaviour);
            return this;
        }

        @Override
        public Builder onClose(@Nullable Consumer<PageAction> behaviour) {
            super.onClose(behaviour);
            return this;
        }

        @Override
        public Builder onClose(@Nullable Runnable behaviour) {
            super.onClose(behaviour);
            return this;
        }

        public Builder nextPage(@Nullable LinkedPage next) {
            this.nextPage = next;
            return this;
        }

        public Builder previousPage(@Nullable LinkedPage previous) {
            this.previousPage = previous;
            return this;
        }

        @Override
        public LinkedPage build() {
            this.validate();
            return new LinkedPage(this.template, this.inventoryTemplate, this.title, this.onOpen, this.onClose, this.previousPage, this.nextPage);
        }
    }
}

