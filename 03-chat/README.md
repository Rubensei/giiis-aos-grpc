# Compiling & launching

All commands must be run from the root project path.

## Server
### Linux
```bash
./gradlew :server:installDist
./server/build/install/server/bin/server
```
### Windows
```bat
.\gradlew :server:installDist
.\server\build\install\server\bin\server.bat
```

## Client
### Linux
```bash
./gradlew :client:installDist
./client/build/install/client/bin/client
```
### Windows
```bat
.\gradlew :server:installDist
.\client\build\install\client\bin\client.bat
```