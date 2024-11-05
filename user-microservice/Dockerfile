FROM maven:3.8.3-openjdk-17 AS builder

COPY ./src/ /root/src
COPY ./pom.xml /root/

WORKDIR /root

RUN mvn package -Dmaven.test.skip=true
RUN java -Djarmode=layertools -jar /root/target/user-microservice-0.0.1-SNAPSHOT.jar list
RUN java -Djarmode=layertools -jar /root/target/user-microservice-0.0.1-SNAPSHOT.jar extract
RUN ls -l /root

FROM openjdk:17-jdk-alpine

ENV TZ=UTC
ENV DB_IP=localhost
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=test1234!
ENV DB_DBNAME=users

COPY --from=builder /root/dependencies/ ./
COPY --from=builder /root/snapshot-dependencies/ ./
RUN sleep 10
COPY --from=builder /root/spring-boot-loader/ ./
COPY --from=builder /root/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher","-XX:+UseContainerSupport -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Xms512m -Xmx512m -XX:+UseG1GC -XX:+UseSerialGC -Xss512k -XX:MaxRAM=72m"]
