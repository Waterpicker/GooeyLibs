package ca.landonjw.gooeylibs2.api.button.moveable;

import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.moveable.Movable;
import ca.landonjw.gooeylibs2.api.button.moveable.MovableButtonAction;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class MovableButton extends GooeyButton implements Movable {
    private final Consumer<MovableButtonAction> onPickup;
    private final Consumer<MovableButtonAction> onDrop;

    protected MovableButton(@Nonnull ItemStack display, @Nullable Consumer<ButtonAction> onClick, @Nullable Consumer<MovableButtonAction> onPickup, @Nullable Consumer<MovableButtonAction> onDrop) {
        super(display, onClick);
        this.onPickup = onPickup;
        this.onDrop = onDrop;
    }

    @Override
    public void onPickup(MovableButtonAction action) {
        if (this.onPickup != null) {
            this.onPickup.accept(action);
        }
    }

    @Override
    public void onDrop(MovableButtonAction action) {
        if (this.onDrop != null) {
            this.onDrop.accept(action);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends GooeyButton.Builder {
        protected Consumer<MovableButtonAction> onPickup;
        protected Consumer<MovableButtonAction> onDrop;

        @Override
        public Builder display(@Nonnull ItemStack display) {
            super.display(display);
            return this;
        }

        @Override
        public Builder title(@Nullable Component title) {
            super.title(title);
            return this;
        }

        @Override
        public Builder lore(@Nullable Collection<String> lore) {
            super.lore(lore);
            return this;
        }

        @Override
        public <T> Builder lore(Class<T> type, @Nullable Collection<T> lore) {
            super.lore(type, lore);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            super.onClick(behaviour);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Runnable behaviour) {
            super.onClick(behaviour);
            return this;
        }

        public Builder onPickup(@Nullable Consumer<MovableButtonAction> behaviour) {
            this.onPickup = behaviour;
            return this;
        }

        public Builder onPickup(@Nullable Runnable behaviour) {
            if (behaviour != null) {
                this.onPickup = action -> behaviour.run();
            }
            return this;
        }

        public Builder onDrop(@Nullable Consumer<MovableButtonAction> behaviour) {
            this.onDrop = behaviour;
            return this;
        }

        public Builder onDrop(@Nullable Runnable behaviour) {
            if (behaviour != null) {
                this.onDrop = action -> behaviour.run();
            }
            return this;
        }

        @Override
        public MovableButton build() {
            this.validate();
            return new MovableButton(this.buildDisplay(), this.onClick, this.onPickup, this.onDrop);
        }
    }
}

