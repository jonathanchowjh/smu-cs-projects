# Text (Joke) Generator
* Backend REST api to serve text generator application
* Text generator uses a Weighted Directed Graph
* Compiles down predictions to a Range Tree (RBT) to improve time complexity of non greedy predictions

<p align="center">
  <img src="https://imgur.com/fOw9VDh.png" width="600" title="">
</p>
<p align="center">
  <img src="https://imgur.com/b0dxWG5.png" width="300" title="">
  <img src="https://imgur.com/gjx3nRe.png" width="300" title="">
</p>

### Running the BACKEND Application
* Running the Frontend (https://github.com/CS201-G2T10/platform-client.git)
* Running the Backend (https://github.com/CS201-G2T10/rest-api.git)
```
./mvnw spring-boot:run  // run REST api

cd data
javac ./ApiLoader.java  // Load data into REST API
java ApiLoader
```

### Running the FRONTEND Application
* Running the Backend (https://github.com/CS201-G2T10/rest-api.git)
* Running the Frontend (https://github.com/CS201-G2T10/platform-client.git)
```
brew install node           // Download node / npm => for macs
npm i                       // instal node_modules
npm start                   // running dev server on localhost:3000

npm i -g serve              // install build server
npm run build               // build react application
serve -s build -p 3000      // running build server on localhost:3000
```

### API Documentation Backend
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