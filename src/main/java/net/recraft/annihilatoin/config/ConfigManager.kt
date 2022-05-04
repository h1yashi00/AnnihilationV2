package net.recraft.annihilatoin.config

import net.recraft.annihilatoin.objects.Game
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.*
import java.util.*

class ConfigManager() : KoinComponent {
    private val configFolder: File by inject()
    init {
        configFolder.mkdirs()
    }
    private class Configuration(private val configFile: File) {
        private val config: YamlConfiguration = YamlConfiguration()
        fun getConfig(): Optional<YamlConfiguration> {
            return Optional.ofNullable(config)
        }

        @Throws(IOException::class, InvalidConfigurationException::class)
        fun load() {
            config.load(configFile)
        }

        @Throws(IOException::class)
        fun save() {
            config.save(configFile)
        }

}
    private val configs = TreeMap<String, Configuration>()
    fun loadConfigFile(filename: String) {
        val configFile = File(configFolder, filename)
        val config: Configuration
        try {
            if (!configFile.exists()) {
                configFile.createNewFile()
                val input: InputStream = Game.plugin.getResource(filename)
                if (input != null) {
                    val out: OutputStream = FileOutputStream(configFile)
                    val buf = ByteArray(1024)
                    var len: Int
                    while (input.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                    out.close()
                    input.close()
                } else {
                    Game.plugin.logger.warning(
                        "default configuration for " + filename + "missing"
                    )
                }
            }
            config = Configuration(configFile)
            config.load()
            configs[filename] = config
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadConfigFiles(vararg filenames: String) {
        for (filename in filenames) {
            loadConfigFiles(filename)
        }
    }

    fun getConfigFolder(configFileName: String): YamlConfiguration {
        val configuration = configs[configFileName]
        return if (configuration == null) {
            YamlConfiguration()
        } else {
            val optionalYamlConfig = configs[configFileName]!!.getConfig()
            val yamlConfig = optionalYamlConfig.orElse(null)
            if (yamlConfig == null) {
                return YamlConfiguration()
            }
            yamlConfig
        }
    }
}
