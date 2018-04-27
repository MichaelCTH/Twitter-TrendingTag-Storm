# Twitter Trending Tag using Storm
[![Build Status](https://travis-ci.org/MichaelCTH/Twitter-TrendingTag-Storm.svg?branch=master)](https://travis-ci.org/MichaelCTH/Twitter-TrendingTag-Storm)
## Summary

## Prerequisites
- Maven
- Docker
- Node.js *(optional for web app)*
 
## Quick Start
### 1. API keys
Fill out your Twittier secure and key in ```resources/twitter4j.properties```

### 2. Pack the source code using Maven
Redirect to the project folder and type
```
	mvn package
```
### 3. Start servers in Docker 

```
	docker-compose -f stack.yml up
```
*\*make sure your docker service is running*

### 4. Connect to Nimbus and submit

```
	docker exec -it nimbus bash
	cd /code/
	storm jar Twitter-TrendingTag-Storm-1.0-SNAPSHOT.jar Twitter_TrendingTag_Topology
```

### 5. Start viewer app (optional)

```
	cd ./ViewerApp
	npm install
	node App.js
```
Then open the Viewer.html in your favorite browser.
