#!/bin/bash

# Number of users to generate
TOTAL_USERS=50
ADMIN_USERS=5
API_URL="http://localhost:8081/api/users/register"

# Function to generate random string for name and email
generate_random_string() {
    cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 8 | head -n 1
}

# Loop to create 10000 users
for i in $(seq 1 $TOTAL_USERS); do
    FIRST_NAME=$(generate_random_string)
    LAST_NAME=$(generate_random_string)
    EMAIL="${FIRST_NAME}${LAST_NAME}@gmail.com"
    PASSWORD="demo@123"
    
    if [ $i -le $ADMIN_USERS ]; then
        ROLE="ADMIN"
    else
        ROLE="CUSTOMER"
    fi

    # Create JSON payload for each user
    JSON_PAYLOAD=$(cat <<EOF
    {
        "firstName": "$FIRST_NAME",
        "lastName": "$LAST_NAME",
        "email": "$EMAIL",
        "password": "$PASSWORD",
        "role": "$ROLE"
    }
EOF
)

    # Make POST request to register the user
    curl -X POST "$API_URL" \
        -H "Accept: */*" \
        -H "Content-Type: application/json" \
        -d "$JSON_PAYLOAD"

    # Optional: Print each user registration for debugging
    echo "User $i with role $ROLE created"
done
