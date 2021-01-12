from flask import Flask, session, render_template, request, flash, redirect, json, jsonify
app = Flask(__name__)

@app.route('/games', methods=['GET'])
def games():
    return jsonify({
        "Fort Totten 21": "A",
        "North Hills Run": "Data"
    })
