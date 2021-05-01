# Jiak.PNG project documentation
Jiak.PNG is a full-stack web application that is built on a MERN (MongoDB, Express.js, React.js, Node.js) stack.
<p align="center">
  <img src="./pictures/IMG_1651.PNG" width="200" title="Razer Hackathon">
  <img src="./pictures/IMG_1652.PNG" width="200" title="Razer Hackathon">
</p>

## Jiak.club Website
- Hawker side https://jiak-hawker.vitaverify.me 
  - Username: 202033100A
  - Password: test123
- Patron side https://jiak-patron.vitaverify.me 
  - You can register as a user by inputting any 8-digit phone number and password

## Back-end
Running the server on localhost
```
git clone https://github.com/Project-Jiak/api_main.git
cd api_main
npm install
npm run dev
```


- Inspecting database with MongoDB compass
  - Download MongoDB Compass GUI
  - Use the connection string below to connect to the database
  ```mongodb+srv://jiakadmin:foodislife@cluster0.mvy2t.mongodb.net```
  - All data for the application is stored under the jiak-dev database
- Testing API calls with postman
  - Download Postman
  - Download postman_collection.json
  - Import postman_collection.json to the Postman
  - Click on “Environments” on the left toolbar, and add environment variables
  - Select environment by clicking on the top right icon
  - Start calling the API sequentially. To test either patron or hawker API calls, the first step is to log in with a set of user credentials.

| Variable | Value |
| :---: | :---: |
| server | https://jiak-api.vitaverify.me |
| base | /api/v1 |

## Front-end
- Downloading Prerequisites
- Download nodejs - https://nodejs.org/en/download/ 
- Download git - https://git-scm.com/downloads 
Running the application

### PATRON’s frontend type these code into your terminal
```
git clone https://github.com/Project-Jiak/platform_patron.git
cd platform_patron
npm install
npm run start
```

### HAWKER’s frontend type these code into your terminal
```
git clone https://github.com/Project-Jiak/platform_hawker.git 
cd platform_hawker
npm install
npm run start
```
- Open Chrome
  - http://localhost:3005 for Patron’s frontend
  - http://localhost:3000 for Hawker’s frontend
- Seeing in iphone resolution
  - Right click, anywhere in your chrome browser
  - Click Inspect
  - Toggle device toolbar (top left of inspect screen - phone icon)

## Inspecting the application
```
# starting in the root of the repository ~/platform_patron
# STARTING CODE FILE ~/platform_patron/src/App.js
cat src/App.js

# PAGES of the application
cd src/pages
cd ../..                                                        # back to root

# DUMMY DATA
cat src/redux/reducers/initState.js             # main file
cat src/redux/reducers/states/hawker.js    # hawker data
cat src/redux/reducers/states/menus.js     # menus data
cat src/redux/reducers/states/stalls.js       # stalls data

# CONNECTION TO BACKEND
cat src/redux/reducers/main.js
```


