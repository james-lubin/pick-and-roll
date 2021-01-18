from pick_and_roll.config import *
from flask import Flask, session, render_template, request, flash, redirect, json, jsonify
from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker

def create_app(test_config=None):
    '''Takes optional test configuration, then creates the flask app.'''
    app = Flask(__name__)
    app.config.from_mapping(
        FLASK_ENV = 'development',
        SECRET_KEY='dev',
    )

    if test_config is None:
        # load the instance config, if it exists, when not testing
        app.config.from_object(current_config())
    else:
        # load the test config if passed in
        app.config.from_mapping(test_config)

    # Set up database
    engine = create_engine(app.config["DATABASE_URI"])
    db = scoped_session(sessionmaker(bind=engine))

    @app.route('/games', methods=['GET'])
    def get_all_games():
        q = db.execute("SELECT * FROM court_type").fetchall()
        return str(q)

    @app.route('/games', methods=['POST'])
    def create_game():
        return jsonify({
            "Fort Totten 21": "A",
            "North Hills Run": "Data"
        })

    return app