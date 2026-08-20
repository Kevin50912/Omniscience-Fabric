package com.thatdudo.omniscience;

import com.thatdudo.omniscience.config.ConfigManager;
import com.thatdudo.omniscience.gui.ScreenBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;


public class Omniscience implements ClientModInitializer {
	public static final String MOD_ID = "omniscience";
	public static final String MOD_NAME = "Omniscience";
	public static final String CONFIG_FILE_NAME = Omniscience.MOD_ID+".json";

	private KeyBinding keyBindingOpenSettings;
	private KeyBinding keyToggleEnabled;
	public static boolean isCreative = true;

	private static final KeyBinding.Category OMNISCIENCE_CATEGORY =
			KeyBinding.Category.create(
					Identifier.of(MOD_ID, "main")
			);


	@Override
	public void onInitializeClient() {

		ConfigManager.init();

		keyBindingOpenSettings = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(
						"key.omniscience.settings",
						InputUtil.Type.KEYSYM,
						GLFW.GLFW_KEY_UNKNOWN,
						OMNISCIENCE_CATEGORY
				)
		);

		keyToggleEnabled = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(
						"key.omniscience.enable",
						InputUtil.Type.KEYSYM,
						GLFW.GLFW_KEY_UNKNOWN,
						OMNISCIENCE_CATEGORY
				)
		);
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(MinecraftClient client) {
		if (keyBindingOpenSettings.wasPressed()) {
			ScreenBuilder.openConfigScreen(client);
		}
		else if (keyToggleEnabled.wasPressed()) {
			if (client.player != null) {
				if (ConfigManager.getConfig().enabled) {
					ConfigManager.getConfig().enabled = false;
					client.player.sendMessage(Text.translatable("message.disabled", MOD_NAME).formatted(Formatting.RED), true);
				}
				else {
					ConfigManager.getConfig().enabled = true;
					client.player.sendMessage(Text.translatable("message.enabled", MOD_NAME).formatted(Formatting.GREEN), true);
				}
			}
		}
	}
}
