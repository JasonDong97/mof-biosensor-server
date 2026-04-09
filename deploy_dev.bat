mvn clean package -DskipTests
scp -r target/miqrobreath-backend.jar root@10.36.160.33:/data/docker/miqrobreath/miqrobreath-backend/miqrobreath-backend.jar