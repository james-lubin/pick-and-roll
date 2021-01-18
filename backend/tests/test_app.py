import os
import tempfile
import pytest
from app import create_game

def func(x):
    return x + 1

def test_answer():
    assert func(3) == 5

def test_create_game():
    create_game()