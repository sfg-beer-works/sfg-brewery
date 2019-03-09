[![CircleCI](https://circleci.com/gh/sfg-beer-works/sfg-beer-works.svg?style=svg)](https://circleci.com/gh/sfg-beer-works/sfg-beer-works)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sfg-beer-works_sfg-beer-works&metric=alert_status)](https://sonarcloud.io/dashboard?id=sfg-beer-works_sfg-beer-works)
# SFG Beer Works
SFG Beer works is a Spring Framework application used to demonstrate the interaction of microservices.

This application mimics a beer distribution pipeline. Beer consumers order beers from a pub. The pub, 
when needed will order more beer from a beer distributor. The distributor orders and receives beer from
a brewery.

Primary author of SFG Beer Works lives in St Petersburg, Florida, US. St Petersburg enjoys a thriving
craft beer scene. Breweries and beers in this application are modeled after real breweries and beers 
in the Tampa Bay area.

## Brewery
The Brewery application is for a brewery brewing beer. This intended to be modeled after a traditional 
monolith application being migrated to microservices. 
#### Features
* Uses Spring MVC, Spring Security, Spring Data JPA, Hibernate
* Shared shared relational database
* Scheduled tasks to brew beer, mimic tasting room (creates demand, to consume inventory and trigger brewing)
* Thymeleaf based UI w/Bootstrap 4

## Distributor
The distributor is a beer distributor - designed to be between many Pub's and many Brewerys. The distributor accepts 
orders from pubs, places orders with Brewers, picks up orders from Breweries, delivers to Pubs.
#### Features
* Uses Spring WebFlux (controllers) 
* Spring Data MongoDB
* Spring Security

## Pub
The Pub is a bar or tavern serving beer to Beer Consumers. Beer Consumers order beer from Pub, Pub places reorders 
from Distributors
#### Features
* Uses Spring WebFlux (Functional)
* Spring Data MongoDB
* Spring Security

## Beer Consumer
The Beer Consumer orders and consumes beers from Pubs. This service creates demand on the supply chain.
#### Features
* Primary language is Kotlin
* Spring WebClient

## Common Features
Features common to all components.
#### Features
* Java 11
* Docker
* JUnit 5 / Mockito
* Spring Framework 5 / Spring Boot 2.1+
* Project Lombok
* Mapstruct

## API Docs
* [SFG Beer Works Brewery](https://sfg-beer-works.github.io/brewery-api/)
* [SFG Beer Works Distributor](https://sfg-beer-works.github.io/distributor-api/)
* [SFG Beer Works Pub](https://sfg-beer-works.github.io/pub-api/)

# Default Port Mappings - For Single Host

| Service Name | Port | 
| --------| -----|
| Brewery Beer Service | 8080 |
| Brewery Order Service | 8081 |
| Brewery UI | 8082 |
| Distributor | 8090 | 
| Pub | 8091 |
| Beer Consumer | 8092