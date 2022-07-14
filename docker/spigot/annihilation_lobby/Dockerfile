FROM openjdk:8-jdk

WORKDIR /work
ADD https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar BuildTools.jar

RUN java -jar BuildTools.jar -o /artifact --rev 1.8.8

RUN cp -rf /artifact/ /spigot-files/

COPY docker/spigot/annihilation_lobby/serverfiles/ /spigot-files/
COPY localDependencies/annihilation_lobby /spigot-files/plugins/


CMD bash /spigot-files/start-server.sh