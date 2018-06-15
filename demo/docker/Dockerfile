FROM openjdk:8

WORKDIR /usr/share/java/demo

ADD app/de172.29.104.124mo.jar /usr/share/java/demo

EXPOSE 8080

CMD java -XX:+PrintFlagsFinal -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar demo.jar