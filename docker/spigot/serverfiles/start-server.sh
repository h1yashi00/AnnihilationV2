set -e

cp -Rf /spigot-files/* /spigot/
mkdir -p /spigot/plugins


java -Xmx4g -Xms256m -jar /spigot/spigot*.jar nogui

echo "eula=true" >> eula.txt

# ディレクトリを移動しないと､/spigot/plugins/が読み込まれない
cd /spigot

java -Xmx4g -Xms256m -jar spigot*.jar nogui
