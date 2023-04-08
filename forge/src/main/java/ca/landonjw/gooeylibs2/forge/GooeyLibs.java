package ca.landonjw.gooeylibs2.forge;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static ca.landonjw.gooeylibs2.forge.GooeyLibs.MOD_ID;

@Mod(value=MOD_ID)
public class GooeyLibs {
    public static final String MOD_ID = "gooeylibs2";

    public GooeyLibs() {
        ForgeBootstrapper bootstrapper = new ForgeBootstrapper();
        bootstrapper.bootstrap();
        MinecraftForge.EVENT_BUS.register(new RegistryEvents());
    }

    public static class RegistryEvents {
        @SubscribeEvent
        public void onCommandRegistration(RegisterCommandsEvent event) {
            event.getDispatcher().register(Commands.literal("gooeytest").executes(this::createUI));
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
}

