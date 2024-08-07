FROM openjdk:8
VOLUME /tmp
ADD xh-service-0.0.1.jar xh.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/xh.jar"]