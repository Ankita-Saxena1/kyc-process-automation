from flask import Flask, request, jsonify
from passporteye import read_mrz

app = Flask(__name__)

@app.route("/extract-mrz", methods=["POST"])
def extract_mrz():
    file = request.files["file"]
    mrz = read_mrz(file)
    return jsonify(mrz.to_dict()) if mrz else {"error": "MRZ not found"}

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
