FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/freestuffly.jar /freestuffly/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/freestuffly/app.jar"]
