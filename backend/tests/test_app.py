import os
import tempfile
import pytest
from pick_and_roll.app import create_app

@pytest.fixture
def app_test():
    '''Returns a flask app with a testing configuration'''
    return create_app({
        'FLASK_ENV': 'development',
        'DEBUG': True,
        'TESTING': True,
        'SECRET_KEY': 'test-key',
        'DATABASE_URI': 'postgres://postgres:password@localhost:0000/test-db'}
    )    

def test_create_app(app_test):
    assert app_test.config['SECRET_KEY'] == 'test-key'
    assert app_test.config['DATABASE_URI'] == 'postgres://postgres:password@localhost:0000/test-db'