package nl.dantevg.commandscheduler

import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.Serializable

@Serializable
data class Config(
	@YamlComment(
		"List of commands to schedule. Example:",
		"  commands:",
		"  - command: say Good morning!",
		"    schedule: every day 09:00",
	)
	val commands: List<CommandConfig> = emptyList(),
)

@Serializable
data class CommandConfig(
	val command: String,
	val schedule: String,
)
