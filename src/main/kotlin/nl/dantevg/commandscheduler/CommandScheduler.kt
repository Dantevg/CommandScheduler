package nl.dantevg.commandscheduler

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import com.charleskorn.kaml.encodeToStream
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.NoSuchFileException
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

object CommandScheduler : ModInitializer {
	const val MOD_ID = "commandscheduler"
	val logger: Logger = LoggerFactory.getLogger(MOD_ID)
	
	private var config = loadConfig()
	private val commands = config.commands.map { ScheduledCommand(it.schedule, it.command) }
	
	override fun onInitialize() {
		command // auto-register command with Silk
		
		ServerTickEvents.END_SERVER_TICK.register(::tick)
		
		for (command in commands) {
			logger.info("Scheduled command '${command.command}'. Next run at ${command.nextFormatted()}.")
		}
	}
	
	private fun tick(server: MinecraftServer) {
		for (command in commands) {
			if (command.shouldRun()) command.run(server)
		}
	}
	
	private fun loadConfig(): Config {
		val configPath = FabricLoader.getInstance().configDir.resolve("$MOD_ID.yml")
		try {
			// Try to read the config file if it exists
			return Yaml.default.decodeFromStream(configPath.inputStream())
		} catch (_: NoSuchFileException) {
			// Config file did not exist, write default one
			logger.info("Creating default config")
			Yaml.default.encodeToStream(Config(), configPath.outputStream())
			return Config()
		} catch (e: Exception) {
			logger.error("Syntax error in config file, using default config.")
			logger.error(e.message)
			return Config()
		}
	}
	
	fun reload() {
		logger.info("Reloading config")
		config = loadConfig()
	}
}
