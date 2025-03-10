from flask import Flask, request, jsonify
import cv2
import numpy as np
import mediapipe as mp
from deepface import DeepFace

app = Flask(__name__)
mp_face_mesh = mp.solutions.face_mesh.FaceMesh()

# Liveness Check using Face Mesh
def detect_liveness(image):
    results = mp_face_mesh.process(cv2.cvtColor(image, cv2.COLOR_BGR2RGB))
    return len(results.multi_face_landmarks) > 0  # Returns True if face is detected

# Face Matching using DeepFace
def compare_faces(img1, img2):
    result = DeepFace.verify(img1_path=img1, img2_path=img2, model_name="VGG-Face")
    return result["verified"]

@app.route("/verify-face", methods=["POST"])
def verify_face():
    file1 = request.files["selfie"]
    file2 = request.files["reference"]

    # Read images
    img1 = cv2.imdecode(np.fromstring(file1.read(), np.uint8), cv2.IMREAD_COLOR)
    img2 = cv2.imdecode(np.fromstring(file2.read(), np.uint8), cv2.IMREAD_COLOR)

    # Liveness check
    if not detect_liveness(img1):
        return jsonify({"error": "Liveness check failed"})

    # Face comparison
    match = compare_faces(file1, file2)
    return jsonify({"match": match})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
