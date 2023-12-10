# Requisitos e instalación

# Java 17

## Windows

Recomendable [Temurin](https://adoptium.net/es/temurin/releases/?version=17) 

Hay que configurar la variable de entorno `JAVA_HOME` con la ruta del jdk instalado para que todos los comandos de la sesión funcionen correctamente. Esto es gestionado automáticamente por el instalador si se utilizan las opciones por defecto.

## Linux

Recomendable instalar utilizando [skdman](https://sdkman.io/)

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.9-tem
# En caso de no querer que se utilice por defecto la nueva instalación, será necesario el siguiente comando
sdk use java 17.0.9-tem # Esto utilizará la nueva versión de java sólo en la terminal en la que se ejecuta
```

#  Python 3.12

Realiza esta instalación sólo si vas a seguir la demo en tu máquina.

## Instalación

### Windows

Desde la [página oficial](https://www.python.org/downloads/) 

Marcar checkbox para añadir a la variable de entorno Path o añadir {usuario}/AppData/Local/Programs/Python/PythonXXX y /PythonXXX/Scripts a mano.

### Linux

```bash
sudo apt install python3 python3-pip
```

## Configuración

En el guión se hará uso de un entorno virtual para aislar las dependencias del proyecto, por lo que será necesario instalar `virtualenv`

```bash
python -m pip install virtualenv
```

