import os
from flask import Flask, jsonify, send_from_directory, abort
from flask_cors import CORS
import pg8000
from urllib.parse import quote
import logging

# Set up logging for better error handling and debugging
logging.basicConfig(level=logging.DEBUG)

app = Flask(__name__)
CORS(app)  # Enable CORS for React

# Config for images and database
BASE_IMAGE_URL = os.getenv('BASE_IMAGE_URL', "http://127.0.0.1:5000/images/")
PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))  # Get the absolute path of the current file's directory
IMAGE_DIRECTORY = os.path.join(PROJECT_ROOT, "images")  # Correct directory structure

# Ensure the images directory exists
if not os.path.exists(IMAGE_DIRECTORY):
    os.makedirs(IMAGE_DIRECTORY)

DATABASE_URL = os.getenv('DATABASE_URL', 'postgres://postgres:Sanika@123@localhost:5432/hotel_booking')

def connect_db():
    return pg8000.connect(
        database="hotel_booking",
        user="postgres",
        password=os.getenv('DB_PASSWORD', 'Sanika@123'),  # Use environment variable for password
        host="localhost",
        port=5432
    )

@app.route('/')
def home():
    return "Welcome to the Hotel Booking App!"

@app.route('/favicon.ico')
def favicon():
    return '', 204  # Handle favicon requests gracefully

@app.route('/images/<int:image_id>')
def get_image(image_id):
    """ Serve images from the correct directory based on image ID. """
    image_filename = f"{image_id}.jpg"  # Assuming images are named 1.jpg, 2.jpg, etc.
    image_path = os.path.join(IMAGE_DIRECTORY, image_filename)
    logging.debug(f"Looking for image: {image_path}")
    
    if os.path.exists(image_path):
        try:
            return send_from_directory(IMAGE_DIRECTORY, image_filename)
        except Exception as e:
            logging.error(f"Error serving image {image_filename}: {e}")
            abort(500, description="Internal server error while serving the image.")
    else:
        logging.warning(f"Image not found: {image_path}")
        abort(404, description="Image not found")

@app.route('/hotels', methods=['GET'])
def get_hotels():
    try:
        conn = connect_db()
        cursor = conn.cursor()

        cursor.execute("SELECT * FROM hotels;")
        hotels = cursor.fetchall()

        cursor.close()
        conn.close()

        # Construct hotel data with image URLs
        hotels_list = [
            {
                "id": row[0],
                "name": row[1],
                "location": row[2],
                "rating": row[3],
                "contact_number": row[4],
                "available_rooms": row[5],
                "price": row[6],
                "photos": BASE_IMAGE_URL + f"{int(row[7])}.jpg" if row[7] and row[7].isdigit() else None  # Image ID to filename
            }
            for row in hotels
        ]
        
        return jsonify(hotels_list)
    
    except Exception as e:
        logging.error(f"Error retrieving hotels: {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/book_room/<int:hotel_id>', methods=['POST'])
def book_room(hotel_id):
    try:
        conn = connect_db()
        cursor = conn.cursor()

        cursor.execute("SELECT available_rooms FROM hotels WHERE id = %s", (hotel_id,))
        available_rooms = cursor.fetchone()

        if available_rooms and available_rooms[0] > 0:
            cursor.execute("""
                UPDATE hotels
                SET available_rooms = available_rooms - 1
                WHERE id = %s AND available_rooms > 0
                RETURNING available_rooms
            """, (hotel_id,))
            updated_rooms = cursor.fetchone()[0]

            # Insert into booking_history table
            cursor.execute("""
                INSERT INTO booking_history (hotel_name, action, timestamp)
                SELECT name, 'Booked', CURRENT_TIMESTAMP
                FROM hotels WHERE id = %s
            """, (hotel_id,))

            conn.commit()
            cursor.close()
            conn.close()
            return jsonify({"message": "Room booked successfully!", "available_rooms": updated_rooms})
        else:
            cursor.close()
            conn.close()
            return jsonify({"error": "No rooms available!"}), 400
    except Exception as e:
        logging.error(f"Error booking room: {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/cancel_booking/<int:hotel_id>', methods=['POST'])
def cancel_booking(hotel_id):
    try:
        conn = connect_db()
        cursor = conn.cursor()

        cursor.execute("SELECT available_rooms FROM hotels WHERE id = %s", (hotel_id,))
        available_rooms = cursor.fetchone()

        if available_rooms and available_rooms[0] < 5:
            cursor.execute("""
                UPDATE hotels
                SET available_rooms = available_rooms + 1
                WHERE id = %s AND available_rooms < 5
                RETURNING available_rooms
            """, (hotel_id,))
            updated_rooms = cursor.fetchone()[0]

            # Insert into booking_history table
            cursor.execute("""
                INSERT INTO booking_history (hotel_name, action, timestamp)
                SELECT name, 'Cancelled', CURRENT_TIMESTAMP
                FROM hotels WHERE id = %s
            """, (hotel_id,))

            conn.commit()
            cursor.close()
            conn.close()
            return jsonify({"message": "Booking canceled successfully!", "available_rooms": updated_rooms})
        else:
            cursor.close()
            conn.close()
            return jsonify({"error": "Max room limit reached (5)!"}), 400
    except Exception as e:
        logging.error(f"Error canceling booking: {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/booking_history', methods=['GET'])
def get_booking_history():
    try:
        # Connect to the database
        conn = connect_db()
        cursor = conn.cursor()

        # Query booking history from the database
        cursor.execute("SELECT id, hotel_name, action, timestamp FROM booking_history;")
        history = cursor.fetchall()

        cursor.close()
        conn.close()

        # Convert the result into a list of dictionaries
        history_list = [{"id": row[0], "hotel_name": row[1], "action": row[2], "timestamp": row[3]} for row in history]

        return jsonify(history_list), 200

    except Exception as e:
        logging.error(f"Error fetching booking history: {e}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)
