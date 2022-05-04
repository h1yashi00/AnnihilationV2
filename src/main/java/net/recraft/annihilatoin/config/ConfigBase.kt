package net.recraft.annihilatoin.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class ConfigBase(_file: File, configFileName: String) {
    protected var fileConfig: FileConfiguration
    protected val file: File = File(_file, configFileName)
    init {
        if (!_file.exists()) {
            _file.mkdirs()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        fileConfig = YamlConfiguration.loadConfiguration(file)
    }
}