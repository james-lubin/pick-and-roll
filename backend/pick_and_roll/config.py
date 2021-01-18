"""Flask configuration."""
from os import environ, path
from dotenv import load_dotenv

basedir = path.abspath(path.join(__file__ ,"../../instance"))
load_dotenv(path.join(basedir, '.env'))

class Config:
    """Base config."""
    #Add shared config here

class ProdConfig(Config):
    FLASK_ENV = 'production'
    DEBUG = False
    TESTING = False
    SECRET_KEY = environ.get('PROD_SECRET_KEY')
    DATABASE_URI = environ.get('PROD_DATABASE_URI')

class DevConfig(Config):
    FLASK_ENV = 'development'
    DEBUG = True
    TESTING = True
    SECRET_KEY = environ.get('DEV_SECRET_KEY')
    DATABASE_URI = environ.get('DEV_DATABASE_URI')

def current_config():
    environment = environ.get("APP_ENV")
    if not environment:
        raise RuntimeError("APP_ENV environment variable is not set")
    
    switch = {
        'prod' : ProdConfig,
        'dev' : DevConfig,
    }

    config = switch.get(environment)
    if config == None:
        raise RuntimeError("Invalid config environment variable") 

    return config