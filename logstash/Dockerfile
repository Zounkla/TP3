FROM docker.elastic.co/logstash/logstash:8.17.0

COPY postgresql-42.7.4.jar /usr/share/logstash/logstash-core/lib/jars/

RUN rm -f /usr/share/logstash/conf/logstash.conf

COPY conf/ /usr/share/logstash/pipeline/
