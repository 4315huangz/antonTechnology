export JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=${PROFILE}"
export JAVA_OPTS="$JAVA_OPTS -Daws.accessKeyId=${ACCESS_KEY}"
export JAVA_OPTS="$JAVA_OPTS -Daws.secretKey=${SECRET_KEY}"
export JAVA_OPTS="$JAVA_OPTS -Ddatabase.driver=org.postgresql.Driver"
export JAVA_OPTS="$JAVA_OPTS -Ddatabase.dialect=org.hibernate.dialect.PostgreSQL9Dialect"
export JAVA_OPTS="$JAVA_OPTS -Dsecret.key=ziwei-anton"
export JAVA_OPTS="$JAVA_OPTS -Dlogging.level.org.springframework=INFO"
export JAVA_OPTS="$JAVA_OPTS -Dlogging.levelt.com.ascending=TRACE"
export JAVA_OPTS="$JAVA_OPTS -Ddatabase.user=ziweiadmin"
export JAVA_OPTS="$JAVA_OPTS -Ddatabase.password=ziwei123!"
export JAVA_OPTS="$JAVA_OPTS -Ddatabase.url=jdbc:postgresql://${DB_URL}:${DB_PORT}/${DB_NAME}"
export JAVA_OPTS="$JAVA_OPTS -Dserver.port=8080"
