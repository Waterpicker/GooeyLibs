/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 */
package ca.landonjw.gooeylibs2.api.helpers;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.Template;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PaginationHelper {
    public static void linkPagesTogether(@Nonnull List<LinkedPage> pages) {
        for (int i = 0; i < pages.size(); ++i) {
            if (i != 0) {
                pages.get(i).setPrevious(pages.get(i - 1));
            }
            if (i == pages.size() - 1) continue;
            pages.get(i).setNext(pages.get(i + 1));
        }
    }

    public static LinkedPage createPagesFromPlaceholders(@Nonnull Template template, @Nonnull List<Button> toReplace, @Nullable LinkedPage.Builder pageBuilder) {
        ArrayList<Integer> placeholderIndexes = new ArrayList<Integer>();
        for (int i = 0; i < template.getSize(); ++i) {
            if (!(template.getSlot(i).getButton().orElse(null) instanceof PlaceholderButton)) continue;
            placeholderIndexes.add(i);
        }
        if (placeholderIndexes.isEmpty()) {
            throw new IllegalStateException("no placeholders defined in supplied template");
        }
        LinkedPage.Builder builder = pageBuilder != null ? pageBuilder : LinkedPage.builder();
        ArrayList<LinkedPage> pages = new ArrayList<LinkedPage>();
        int currentIndex = 0;
        if (toReplace.isEmpty()) {
            return builder.template(template.clone()).build();
        }
        while (currentIndex < toReplace.size()) {
            Template replacement = template.clone();
            for (int i = 0; i < placeholderIndexes.size(); ++i) {
                int targetIndex = placeholderIndexes.get(i);
                if (currentIndex >= toReplace.size()) break;
                replacement.getSlot(targetIndex).setButton(toReplace.get(currentIndex));
                ++currentIndex;
            }
            pages.add(builder.template(replacement).build());
        }
        PaginationHelper.linkPagesTogether(pages);
        return pages.get(0);
    }
}

