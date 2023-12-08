# Compiling & launching

All commands must be run from the root project path.

## Server
### Linux y Windows powershell
```bash
./gradlew :server:installDist
./server/build/install/server/bin/server
```
### Windows cmd
```bat
.\gradlew.bat :server:run
.\server\build\install\server\bin\server.bat
```

## Client
### Linux y Windows powershell
```bash
./gradlew :client:installDist
./client/build/install/client/bin/client
```
### Windows cmd
```bat
.\gradlew.bat :client:installDist
.\client\build\install\client\bin\client.bat
```