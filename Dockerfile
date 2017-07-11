FROM alpine:3.5

RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home
	
ENV LANG=C.UTF-8 \
	JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk \
	PATH=$PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin \
	JAVA_VERSION=8u121 \
	JAVA_ALPINE_VERSION=8.121.13-r0 \
	JAVA_OPTS=""

RUN set -x \
	&& apk add --no-cache \
		openjdk8="$JAVA_ALPINE_VERSION" \
	&& [ "$JAVA_HOME" = "$(docker-java-home)" ]

VOLUME /tmp

ADD build/libs/order-*.jar order.jar

RUN sh -c 'touch /order.jar'

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /order.jar" ]
