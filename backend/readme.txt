To get the flask app running you'll have to:

1) Set up a configuration file
2) Set up some environment variables

Step 1:
Create a folder named "instance", and inside of it create a file named ".env". The .env file is a simple
list of key-values. Add the following lines:

    PROD_SECRET_KEY='{value goes here}'
    PROD_DATABASE_URI='{value goes here}'

    DEV_SECRET_KEY='{value goes here}'
    DEV_DATABASE_URI='{value goes here}'

Replace the the curly braces with the relevant values. Don't include the curly braces themselves, and
the values must be wrapped in single quotes.


Step 2:
Set the FLASK_APP environment variable with the command (on unix):

    export FLASK_APP=app.py 

Set the environment with the command:

    export APP_ENV=dev