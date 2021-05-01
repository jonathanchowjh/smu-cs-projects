# Text (Joke) Generator Backend
* Backend REST api to serve text generator application
* Text generator uses a Weighted Directed Graph
* Compiles down predictions to a Range Tree (RBT) to improve time complexity of non greedy predictions

<p align="center">
  <img src="https://imgur.com/b0dxWG5.png" width="300" title="Text Gen image">
  <img src="https://imgur.com/gjx3nRe.png" width="300" title="Text Gen image">
</p>

### Running the Application
* Running the Frontend (https://github.com/CS201-G2T10/platform-client.git)
* Running the Backend
```
./mvnw spring-boot:run  // run REST api

cd data
javac ./ApiLoader.java  // Load data into REST API
java ApiLoader
```

### API Documentation 
* Runs on localhost:8080
---
#### POST /api/model
* Returns 201 CREATED
* Request Body
```
{
  "joke": "joke string here"
}
```
---
#### POST /api/model/predict
* Request Body
```
{
  "first_word": "",
  "max_length": 8
}
```
* Response Body (200 OK)
```
{
  "joke": "joke string here",
  "first_word": "",
  "max_length": 8
}
```