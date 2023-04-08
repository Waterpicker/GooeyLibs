package ca.landonjw.gooeylibs2.api.button;

import ca.landonjw.gooeylibs2.api.data.UpdateEmitter;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;

public abstract class ButtonBase extends UpdateEmitter<Button> implements Button {
    private ItemStack display;

    protected ButtonBase(@Nonnull ItemStack display) {
        this.display = Objects.requireNonNull(display);
    }

    @Override
    public final ItemStack getDisplay() {
        return this.display;
    }

    public void setDisplay(@Nonnull ItemStack display) {
        this.display = display;
        this.update();
    }
}

