/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.network.chat.Component
 */
package ca.landonjw.gooeylibs2.api.page;

import ca.landonjw.gooeylibs2.api.data.Subject;
import ca.landonjw.gooeylibs2.api.page.PageAction;
import ca.landonjw.gooeylibs2.api.template.Template;
import ca.landonjw.gooeylibs2.api.template.types.InventoryTemplate;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;

public interface Page
extends Subject<Page> {
    Template getTemplate();

    default Optional<InventoryTemplate> getInventoryTemplate() {
        return Optional.empty();
    }

    Component getTitle();

    default void onOpen(@Nonnull PageAction action) {
    }

    default void onClose(@Nonnull PageAction action) {
    }
}

