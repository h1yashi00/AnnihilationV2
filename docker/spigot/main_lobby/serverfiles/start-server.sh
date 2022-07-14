set -e

mkdir -p /spigot/plugins
cp -Rf /spigot-files/* /spigot/

# ディレクトリを移動しないと､/spigot/plugins/が読み込まれない
cd /spigot/

java -Xmx4g -Xms256m -jar spigot*.jar nogui

echo "eula=true" >> eula.txt


touch input
java -Xmx4g -Xms256m -jar spigot*.jar nogui < input