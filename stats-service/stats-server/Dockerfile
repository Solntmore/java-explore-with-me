FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar statService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/statService-0.0.1-SNAPSHOT.jar"]