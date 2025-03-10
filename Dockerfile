# Use Python as base image
FROM python:3.9

# Install dependencies
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    libsm6 libxext6 libxrender-dev \
    libgl1-mesa-glx \
    supervisor \
    && rm -rf /var/lib/apt/lists/*

# Install Python packages
RUN pip install flask passporteye deepface opencv-python mediapipe

# Copy the Flask app
WORKDIR /app
COPY ./python-project/app.py .
COPY ./python-project/face_service.py .

# Copy the supervisor configuration file
COPY ./supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Expose port 5000
EXPOSE 5000

# Run supervisord
CMD ["supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]