# Use Python as base image
FROM python:3.9

# Install dependencies
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    libsm6 libxext6 libxrender-dev \
    && rm -rf /var/lib/apt/lists/*

# Install Python packages
RUN pip install flask passporteye

# Copy the Flask app
WORKDIR /app
COPY ./python-project/app.py .

# Expose port 5000
EXPOSE 5000

# Run the Flask app
CMD ["python", "app.py"]
