FROM openjdk:17-jdk-alpine
MAINTAINER ws.probal
COPY build/libs/url-shortener-0.0.1-SNAPSHOT.jar url_shortener.jar
ENTRYPOINT ["java","-jar","/url_shortener.jar"]