The Video Record Management System For Presentation Assessment
=================

The Technology Stack
------------
Operating System: Ubuntu Server 14.04LTS or 16.04LTS

Virtualization Technology For Easy Deployment: Docker

Server Technology: Spring Boot (Based on Java Servlet)

HTTP Server & Servlet Container: Tomcat (Embeded in Spring Boot)

JDK 1.8 or above is required

Relational Database: MySQL v5.7 or above

Key Value Database: Redis 3 or above


Install Docker
-----------

Docker make it easy to build up the runtime environment for the server

To install Docker easily, Ubuntu Server 14.04LTS or above is chosen

### Install docker on Ubuntu
#### Alter the APT mirror
To install software fast, change the URL of the mirrors in /etc/apt/sources.list
For example, use the mirrors provided by Aliyun: http://cn.archive.ubuntu.com/ubuntu

#### Install apt-fast
apt-fast accelerates the download process of apt-get with the help of aria2
```
sudo add-apt-repository ppa:saiarcot895/myppa
sudo apt-get update
sudo apt-get -y install apt-fast
```

#### Install Docker
Install Docker with apt-fast and the script provided by DaoCloud
```
curl -sSL https://get.daocloud.io/docker | sed "s/apt-get install/apt-fast install/g" | sudo sh
```
The script also installs the distributed version control system GIT


#### Run Docker Containers
Three Docker containers are required to run the website: MySQL 5.7, Redis 3.2 and jdk 1.8

To run the Docker container needed to deploy the website, run the BASH script in the project
```
sudo bash docker/setup.sh
```


Introduction of some directories
-----------

### src
The source codes and the resources to run and test the website

### src/test
The code to test the website

### src/main
The source codes and the resources of the website

#### src/main/java
The source code in Java of the website

#### src/main/resources
The static resource of the website, such as Javascript, Cascading Style Sheet (CSS) and images

### src/main/mysql
The initialization script of the tables in MySQL
