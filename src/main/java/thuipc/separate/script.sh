#!/bin/bash

cd ../..

# Compile the Player and PlayerServer classes
javac thuipc/separate/Player.java thuipc/separate/PlayerServer.java

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful."

     # Run the PlayerServer for Player2
    java thuipc.separate.PlayerServer Player2 localhost 5000 5001 &

     # Wait for a short period to ensure that Player1 is ready to accept connections
     sleep 2

    # Run the PlayerServer for Player1
    java thuipc.separate.PlayerServer Player1 localhost 5001 5000 &

    # Wait for the background processes to finish
    wait
else
    echo "Compilation failed. Please check the source code for errors."
fi