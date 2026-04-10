mvn clean package -DskipTests & ^
scp target/miqrobreath-backend.jar root@10.36.160.33:/data/docker/miqrobreath/miqrobreath-backend/miqrobreath-backend.jar & ^
ssh root@10.36.160.33 "docker restart miqrobreath-backend" & ^
ssh root@10.36.160.33 "docker logs -f -n 50 miqrobreath-backend" &