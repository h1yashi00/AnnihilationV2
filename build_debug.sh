gradle jar

# spigot_spigot_1はコンテナ名
docker cp ./build/libs/* spigot_spigot_1:/spigot/plugins

# -w はワーキングディレクトリ リダイレクトする際は bash -c を指定しないとうまく動作しない｡
docker exec -it -w /spigot spigot_spigot_1 bash -c "echo 'reload' >> input"