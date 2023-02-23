# Installation
## Install Spring Cloud Data Flow
### Docker
export HOST_MOUNT_PATH=~/.m2
export DOCKER_MOUNT_PATH=/root/.m2/
export HOST_MOUNT_PATH=/tmp/myapps
export DATAFLOW_VERSION=2.10.0
export SKIPPER_VERSION=2.9.0

wget -O docker-compose.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose.yml;
wget -O docker-compose-kafka.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-kafka.yml;
wget -O docker-compose-postgres.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-postgres.yml;

docker-compose -f docker-compose.yml -f docker-compose-<broker>.yml -f docker-compose-<database>.yml up

### Manual
```groovy
mkdir jars
cd jars
wget https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dataflow-server/2.10.0/spring-cloud-dataflow-server-2.10.0.jar
wget https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dataflow-shell/2.10.0/spring-cloud-dataflow-shell-2.10.0.jar
wget https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-skipper-server/2.9.0/spring-cloud-skipper-server-2.9.0.jar
```

## JFROG
Source: https://www.jfrog.com/confluence/display/JFROG/Installing+Artifactory#InstallingArtifactory-DockerInstallation

```groovy
mkdir -p $JFROG_HOME/artifactory/var/etc/
cd $JFROG_HOME/artifactory/var/etc/
touch ./system.yaml
sudo chown -R 1030:1030 $JFROG_HOME/artifactory/var
sudo chmod -R 777 $JFROG_HOME/artifactory/var
docker run --name artifactory -v $JFROG_HOME/artifactory/var/:/var/opt/jfrog/artifactory -d -p 8081:8081 -p 8082:8082 releases-docker.jfrog.io/jfrog/artifactory-oss:latest
```