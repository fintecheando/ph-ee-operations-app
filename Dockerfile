FROM azul/zulu-openjdk-debian:17 AS builder

RUN apt-get update -qq && apt-get install -y wget

COPY . paymenthub

WORKDIR /paymenthub

RUN ./gradlew --no-daemon -q clean bootJar

WORKDIR /paymenthub/target

RUN jar -xf /paymenthub/build/libs/operations-app*.jar

WORKDIR /paymenthub/target/BOOT-INF/libs

RUN wget -q https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.23/mysql-connector-java-8.0.23.jar

# =========================================
FROM azul/zulu-openjdk-debian:17 as paymenthub

RUN mkdir -p /app/libs

COPY --from=builder /paymenthub/build/libs/operations-app*.jar /app/operations-app.jar

COPY --from=builder /paymenthub/target/BOOT-INF/lib /app/libs

WORKDIR /

EXPOSE 8000

CMD java -jar /app/operations-app.jar

