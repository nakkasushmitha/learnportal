FROM openjdk:17
EXPOSE 8080
ADD target/learningportal.jar learningportal.jar
ENTRYPOINT ["java", "-jar", "/learningportal.jar"]