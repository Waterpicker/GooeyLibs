package ca.landonjw.gooeylibs2.api.button;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class PlaceholderButton
implements Button {
    private final Button button;

    public PlaceholderButton(@Nonnull Button button) {
        this.button = button;
    }

    public PlaceholderButton() {
        this(GooeyButton.builder().display(ItemStack.EMPTY).build());
    }

    public static PlaceholderButton of(@Nonnull Button button) {
        return new PlaceholderButton(button);
    }

    @Override
    public ItemStack getDisplay() {
        return this.button.getDisplay();
    }

    @Override
    public void onClick(@Nonnull ButtonAction action) {
        this.button.onClick(action);
    }

    @Override
    public void subscribe(@Nonnull Object observer, @Nonnull Consumer<Button> consumer) {
        this.button.subscribe(observer, consumer);
    }

    @Override
    public void unsubscribe(@Nonnull Object observer) {
        this.button.unsubscribe(observer);
    }
}

