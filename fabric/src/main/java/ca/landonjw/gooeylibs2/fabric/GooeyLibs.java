/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
 *  net.minecraft.ItemStack
 *  net.minecraft.class_1802
 *  net.minecraft.class_1935
 *  net.minecraft.CommandSourceStack
 *  net.minecraft.class_2170
 *  net.minecraft.class_2561
 */
package ca.landonjw.gooeylibs2.fabric;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import ca.landonjw.gooeylibs2.fabric.FabricBootstrapper;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GooeyLibs
implements ModInitializer {
    private final FabricBootstrapper bootstrapper = new FabricBootstrapper();

    public void onInitialize() {
        this.bootstrapper.bootstrap();
        CommandRegistrationCallback.EVENT.register((dispatcher, accessor, environment) -> dispatcher.register(Commands.literal("gooeytest").executes(this::createUI)));
    }

    private int createUI(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        GooeyButton button = GooeyButton.builder().display(new ItemStack(Items.BLACK_STAINED_GLASS_PANE)).title(Component.literal("")).build();
        GooeyButton diamond = GooeyButton.builder().display(new ItemStack(Items.DIAMOND)).title(Component.literal("Test Item")).onClick(() -> System.out.println("Clicked diamond")).build();
        ChestTemplate template = ChestTemplate.builder(5).border(0, 0, 6, 9, button).set(22, diamond).build();
        GooeyPage page = GooeyPage.builder().title(Component.literal("1.19.2 Test UI")).template(template).build();
        UIManager.openUIForcefully(source.getPlayerOrException(), page);
        return 1;
    }
}

