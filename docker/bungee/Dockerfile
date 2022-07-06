FROM openjdk:8-jdk

WORKDIR /bungeecord
ADD https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar /bungee-files/BungeeCord.jar

RUN mkdir -p /bungee-files/plugin

COPY docker/bungee/serverfiles/ /bungee-files/
COPY bungeeDependencies/ /bungee-files/plugins

CMD bash /bungee-files/start-server.sh