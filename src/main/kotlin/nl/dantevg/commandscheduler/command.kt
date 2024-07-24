package nl.dantevg.commandscheduler

import net.minecraft.text.Text
import net.silkmc.silk.commands.command

private val usage = """
	Usage:
	  /${CommandScheduler.MOD_ID} reload
""".trimIndent()

val command = command(CommandScheduler.MOD_ID) {
	runs {
		source.sendFeedback({ Text.literal(usage) }, false)
	}
	
	literal("reload") runs {
		CommandScheduler.reload()
		if (source.isExecutedByPlayer) source.sendFeedback({ Text.literal("Reload done") }, false)
	}
}
