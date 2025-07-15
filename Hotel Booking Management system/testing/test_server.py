import pytest
import requests

# Base URL of the Flask application
BASE_URL = "http://127.0.0.1:5000"  # Assuming the Flask app is running locally on port 5000

def test_home():
    """Test the home page ("/") route."""
    response = requests.get(f"{BASE_URL}/")
    assert response.status_code == 200
    assert response.text == "Welcome to the Hotel Booking App!"

def test_get_hotels():
    """Test the /hotels route to get the list of hotels."""
    response = requests.get(f"{BASE_URL}/hotels")
    assert response.status_code == 200
    hotels = response.json()
    assert isinstance(hotels, list)  # Ensure the response is a list
    if hotels:
        assert "name" in hotels[0]  # Check if 'name' exists in the first hotel entry
        assert "location" in hotels[0]  # Check if 'location' exists in the first hotel entry

def test_get_image():
    """Test the /images route to fetch an image by ID."""
    image_id = 1  # Assuming image ID 1 exists in your server
    response = requests.get(f"{BASE_URL}/images/{image_id}")
    assert response.status_code == 200
    assert response.headers["Content-Type"].startswith("image/")  # Ensure it returns an image

def test_book_room():
    """Test the /book_room route to book a room."""
    hotel_id = 1  # Assuming hotel ID 1 exists and has rooms available
    response = requests.post(f"{BASE_URL}/book_room/{hotel_id}")
    assert response.status_code == 200
    json_response = response.json()
    assert "message" in json_response  # Ensure the booking message is in the response
    assert json_response["message"] == "Room booked successfully!"

def test_cancel_booking():
    """Test the /cancel_booking route to cancel a booking."""
    hotel_id = 1  # Assuming hotel ID 1 exists
    response = requests.post(f"{BASE_URL}/cancel_booking/{hotel_id}")
    assert response.status_code == 200
    json_response = response.json()
    assert "message" in json_response  # Ensure the cancellation message is in the response
    assert json_response["message"] == "Booking canceled successfully!"

def test_get_booking_history():
    """Test the /booking_history route to fetch the booking history."""
    response = requests.get(f"{BASE_URL}/booking_history")
    assert response.status_code == 200
    history = response.json()
    assert isinstance(history, list)  # Ensure the response is a list
    if history:
        assert "hotel_name" in history[0]  # Check if 'hotel_name' exists in the first history entry
        assert "action" in history[0]  # Check if 'action' exists in the first history entry
