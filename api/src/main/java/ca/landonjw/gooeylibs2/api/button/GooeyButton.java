package ca.landonjw.gooeylibs2.api.button;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GooeyButton extends ButtonBase {
    private final Consumer<ButtonAction> onClick;

    protected GooeyButton(@Nonnull ItemStack display, @Nullable Consumer<ButtonAction> onClick) {
        super(display);
        this.onClick = onClick;
    }

    @Override
    public void onClick(@Nonnull ButtonAction action) {
        if (this.onClick != null) {
            this.onClick.accept(action);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static GooeyButton of(ItemStack stack) {
        return GooeyButton.builder().display(stack).build();
    }

    public static class Builder {
        protected ItemStack display;
        protected Component title;
        protected Collection<Component> lore = Lists.newArrayList();
        protected Consumer<ButtonAction> onClick;
        protected Set<FlagType> hideFlags = new LinkedHashSet<>();

        public Builder display(@Nonnull ItemStack display) {
            this.display = display;
            return this;
        }

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

        public Builder lore(@Nullable Collection<String> lore) {
            if (lore == null) {
                return this;
            }
            this.lore = lore.stream().map(Component::literal).collect(Collectors.toList());
            return this;
        }

        public <T> Builder lore(Class<T> type, @Nullable Collection<T> lore) {
            if (lore == null) {
                return this;
            }
            if (Component.class.isAssignableFrom(type)) {
                this.lore = (Collection<Component>) lore;
                return this;
            }
            if (String.class.isAssignableFrom(type)) {
                return this.lore((Collection<String>) lore);
            }
            throw new UnsupportedOperationException("Invalid Type: " + type.getName());
        }

        public Builder hideFlags(FlagType... flags) {
            this.hideFlags.addAll(Arrays.asList(flags));
            return this;
        }

        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            this.onClick = behaviour;
            return this;
        }

        public Builder onClick(@Nullable Runnable behaviour) {
            this.onClick = behaviour != null ? action -> behaviour.run() : null;
            return this;
        }

        public GooeyButton build() {
            this.validate();
            return new GooeyButton(this.buildDisplay(), this.onClick);
        }

        protected void validate() {
            if (this.display == null) {
                throw new IllegalStateException("button display must be defined");
            }
        }

        protected ItemStack buildDisplay() {
            if (this.title != null) {
                MutableComponent result = Component.literal("").setStyle(Style.EMPTY.withItalic(Boolean.valueOf(false))).append(this.title);
                this.display.setHoverName(result);
            }
            if (!this.lore.isEmpty()) {
                ListTag nbtLore = new ListTag();
                for (Component line : this.lore) {
                    MutableComponent result = Component.literal("").setStyle(Style.EMPTY.withItalic(Boolean.valueOf(false))).append(line);
                    nbtLore.add(StringTag.valueOf(Component.Serializer.toJson(result)));
                }
                this.display.getOrCreateTagElement("display").put("Lore", nbtLore);
            }
            if (!this.hideFlags.isEmpty() && this.display.hasTag()) {
                if (this.hideFlags.contains(FlagType.Reforged) || this.hideFlags.contains(FlagType.All)) {
                    this.display.getOrCreateTag().putString("tooltip", "");
                }
                if (this.hideFlags.contains(FlagType.Generations) || this.hideFlags.contains(FlagType.All)) {
                    this.display.getOrCreateTag().putBoolean("HideTooltip", true);
                }
                int value = 0;
                for (FlagType flag : this.hideFlags) {
                    value += flag.getValue();
                }
                this.display.getOrCreateTag().putInt("HideFlags", value);
            }
            return this.display;
        }
    }
}

