package virtuoel.pehkui.mixin.client.compat116.compat1162plus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Inject(method = "method_11117(Lnet/minecraft/class_2724;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_5823()V", remap = false), remap = false)
	private void onOnPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo info, RegistryKey<World> dimension, DimensionType dimensionType, ClientPlayerEntity oldPlayer, int id, String brand, ClientPlayerEntity newPlayer)
	{
		final boolean shouldCopyScale = packet.shouldKeepPlayerAttributes() ||
			Optional.ofNullable(PehkuiConfig.DATA.get("keepScaleOnRespawn"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(false);
		
		if (shouldCopyScale)
		{
			ScaleUtils.loadScale(newPlayer, oldPlayer);
		}
	}
}
