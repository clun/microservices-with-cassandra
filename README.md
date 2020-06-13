# Microservices for Cassandra with Spring

In May 2020, the Spring engineering teams released Spring Boot 2.3.0 and Spring Data 3.0.0 that brought an upgrade to the latest version of the Cassandra Java driver. This driver upgrade provides full support for the newest major version of Cassandra 4.0 in these utilities that we'll showcase throughout the blog. The Spring and Cassandra communities are continuing to work together to provide tighter integrations with better health checks, metrics, and auto-configuration of the driver via application properties files.

#### Contributors: 
- [Cedrick Lunven](https://github.com/clun) - twitter handdle [@clun](https://twitter.com/clunven)
- [Chris Splinter](https://github.com/csplinter) - twitter handle [@]()
- [Frank Moley](https://github.com/fpmoles) - twitter handle [@]()

#### Modules:
- `microservice-spring-boot` : 
   - **Persistence Layer** : repository using explicitly `CqlSession` and java driver Object Mapper. 
   - **Exposition Layer** : Rest Controllers wih spring-mvc  `@Controller`
   - **Documentation** : OpenApi3 and SpringDoc
- `microservice-spring-boot-reactive` : 
   - **Persistence Layer** : repository using explicitly `CqlSession` and java driver Object Mapper. 
   - **Exposition Layer** : Reactive Endpoints wih Spring Web Flux.
- `microservice-spring-data` : *fully embrace the spring approach with Spring Data*
  - **Persistence Layer** : Spring Data `CrudRepository<K,V>`
  - **Exposition Layer** : Spring-Data-Rest
- `microservice-spring-data-reactive` :
  - **Persistence Layer** : Spring Data `ReactiveCrudRepository<K,V>`
  - **Exposition Layer** :Spring Web Flux

## 1. Objectives

This a fully working set of microservices illustrating how to implements microservices on top of Apache Cassandra leveraging Spring modules : `spring-data`,  `spring-boot`,  `spring-data-rest`,  `spring-webmvc`, `spring-security` both synchronous and reactive REST API with DataStax java driver (`CqlSession`)

## 2. How this Works

All external componenets are started using the `docker-compose` or the Kubernetes `yaml` deployment file. This can include a 3 nodes cluster for Cassandra or use ATRA Cassandra as a service by DataStax.

The component expose all the same service, idea is to demonstrate multiple implementations strategies. The business domain is better botz the pilot application in ASTRA Showcases.

Each module is a standalone REST API providing Its own documentation using Swageger/OpenAPI when this is possible. Each module listen on a dedicated port if you want to run all them at once.



## 3. Setup and Running

### 3.a - Prerequisites
The prerequisites required for this application to run
* Docker
* Kubernetes
* JDK 8+
* Maven

### 3.b - Clone repo
```
# Clone the current repository
git clone
```

### Start all components excepts the components with docker-compose

```
docker-compose up -d
```

### Start all components excepts the components with kubernetes
```
kubectl apply -f dist/deploy-kubernetes/grafana-operator.yaml
kubectl apply -f dist/deploy-kubernetes/grafana-instance.yaml
kubectl apply -f dist/deploy-kubernetes/grafana-dashboard-spring.yaml
kubectl apply -f dist/deploy-kubernetes/grafana-dashboard-prometheus.yaml
kubectl apply -f dist/deploy-kubernetes/grafana-dashboard-cassandra.yaml

kubectl apply -f dist/deploy-kubernetes/prometheus-operator.yaml
kubectl apply -f dist/deploy-kubernetes/prometheus-instance.yaml
kubectl apply -f dist/deploy-kubernetes/prometheus-service-monitor.yaml

kubectl apply -f dist/deploy-kubernetes/cass-operator.yaml
kubectl apply -f dist/deploy-kubernetes/cassandra-dc1.yaml
```

# Pick the microservice you like
```
cd microservice-spring-boot
```

# Start the microservice you like
```
mvn spring-boot:run
```

### Check console for proper listening port
```
http://localhost:8080
http://localhost:8081
http://localhost:8082
http://localhost:8083
```
