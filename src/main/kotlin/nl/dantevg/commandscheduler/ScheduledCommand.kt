package nl.dantevg.commandscheduler

import com.github.shyiko.skedule.Schedule
import net.minecraft.server.MinecraftServer
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ScheduledCommand(val schedule: Schedule, val command: String) {
	constructor(schedule: String, command: String) : this(Schedule.parse(schedule), command)
	
	private var lastRun = ZonedDateTime.now()
	
	fun nextRun(): ZonedDateTime = schedule.next(lastRun)
	fun nextFormatted(): String = nextRun().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
	
	fun shouldRun() = nextRun().isBefore(ZonedDateTime.now())
	
	fun run(server: MinecraftServer) {
		lastRun = ZonedDateTime.now()
		CommandScheduler.logger.info("Running command $command. Next run at ${nextFormatted()}.")
		server.commandManager.executeWithPrefix(server.commandSource, command)
	}
}
