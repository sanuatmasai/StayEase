#!/bin/bash

# Total number of hotels to create
TOTAL_HOTELS=200
API_URL="http://localhost:8081/api/hotels"
AUTH_HEADER="Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW51YXRtYXNhaUBnbWFpbC5jb20iLCJleHAiOjE3NDQ5NzI4MzYsImlhdCI6MTc0NDk2OTIzNn0.XJe8OU0ezNJAC1LIGL6aYKtBWpkmI5nxrwRenv-PwgY"

# Function to generate random strings for hotel names and descriptions
generate_random_string() {
    cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 8 | head -n 1
}

# Loop to create 200 hotels
for i in $(seq 1 $TOTAL_HOTELS); do
    HOTEL_NAME="Hotel_$(generate_random_string)"
    LOCATION="Location_$(generate_random_string)"
    DESCRIPTION="Description for $HOTEL_NAME"
    TOTAL_ROOMS=$((RANDOM % 500 + 50))  # Random total rooms between 50 and 500
    AVAILABLE_ROOMS=$((RANDOM % TOTAL_ROOMS))  # Ensure available rooms <= total rooms

    # Create JSON payload for each hotel
    JSON_PAYLOAD=$(cat <<EOF
    {
        "name": "$HOTEL_NAME",
        "location": "$LOCATION",
        "description": "$DESCRIPTION",
        "totalRooms": $TOTAL_ROOMS,
        "availableRooms": $AVAILABLE_ROOMS
    }
EOF
)

    # Make POST request to create the hotel
    curl --location "$API_URL" \
        --header "accept: */*" \
        --header "Content-Type: application/json" \
        --header "$AUTH_HEADER" \
        --data "$JSON_PAYLOAD"

    # Optional: Print each hotel registration for debugging
    echo "Hotel $i ($HOTEL_NAME) created with $AVAILABLE_ROOMS out of $TOTAL_ROOMS rooms available."
done
