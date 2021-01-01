package virtuoel.pehkui.server.command.arguments;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

public class ScaleTypeArgumentType implements ArgumentType<ScaleType>
{
	private static final Collection<String> EXAMPLES = ScaleRegistries.SCALE_TYPES.keySet().stream().map(Identifier::toString).collect(Collectors.toList());
	public static final DynamicCommandExceptionType INVALID_ENTRY_EXCEPTION = new DynamicCommandExceptionType(arg -> new LiteralText("Unknown scale type '" + arg + "'"));
	
	@Override
	public ScaleType parse(StringReader stringReader) throws CommandSyntaxException
	{
		final Identifier identifier = Identifier.fromCommandInput(stringReader);
		return Optional.ofNullable(ScaleRegistries.SCALE_TYPES.get(identifier)).orElseThrow(() -> INVALID_ENTRY_EXCEPTION.create(identifier));
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return CommandSource.suggestIdentifiers(ScaleRegistries.SCALE_TYPES.keySet(), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	public static ScaleTypeArgumentType scaleType()
	{
		return new ScaleTypeArgumentType();
	}
	
	public static ScaleType getScaleTypeArgument(CommandContext<ServerCommandSource> context, String name)
	{
		return context.getArgument(name, ScaleType.class);
	}
}
