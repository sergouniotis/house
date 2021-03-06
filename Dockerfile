FROM java:8

VOLUME /tmp

ADD target/house-swarm.jar app.jar

ADD dockerfiles/run.sh /run.sh

ADD src/main/resources/images /images

RUN bash -c 'touch /app.jar'

EXPOSE 8080

CMD ["bash","./run.sh"]