FROM java:17
EXPOSE 8080
ADD target/learningportal-0.0.1-SNAPSHOT.jar learningportal-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/learningportal-0.0.1-SNAPSHOT.jar"]