FROM gradle
WORKDIR  /workspace
USER root
COPY . /workspace
RUN gradle bootJar

# build image
FROM openjdk:8-jre
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
ENV JAVA_OPTS ''
WORKDIR /app
COPY --from=0 /workspace/build/libs/test-web-app-0.0.1-SNAPSHOT.jar .
EXPOSE 10083

ENTRYPOINT java $JAVA_OPTS -jar test-web-app-0.0.1-SNAPSHOT.jar
