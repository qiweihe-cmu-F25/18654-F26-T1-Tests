# Travel Aggregator

There are 11 microservices in this repository. Each microservice has its own Java project under its name. They are:
1. `carhire_search`
2. `carhire_tracking`
3. `expense_management`
4. `experience_search`
5. `experience_tracking`
6. `flight_tracking`
7. `flightsearch`
8. `hotel_tracking`
9. `hotelsearch`
10. `trip_management`
11. `userprofile`


After running `mvn install`, each Java project can be run as a java jar using `mvn spring-boot:run` in its folder.


# Local machine set up
1. Download and install JDK 17 from https://www.oracle.com/java/technologies/downloads/?er=221886#java17
2. Install Homebrew by executing the following command in the Terminal: /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
3. Install maven 3.9.8 by executing the following command in Terminal: brew install maven

## Local Service Development Setup

To set up the local development environment for each service, follow these steps:

### Step 1: Configure Environment Variables

0. You can run following commands to skip step 1 ~ 3:
   1. `cd` into the root directory of the project
   2. `chmod +x copy_env_files.sh`
   3. `./copy_env_files.sh`
   

1. Create a `.env` file in the root directory of your project.
2. Refer to the `.env.example` file for the required environment variables. Copy the contents of `.env.example` into your newly created `.env` file and set the appropriate values.

    ```sh
    cp .env.example .env
    ```
3. Each service operates with its own database. To ensure proper configuration, follow these steps:
   1. Create a `.env` file for each service (except for services with names containing 'search').
   2. Copy the contents of the `.env.example` file located in each service's folder into the corresponding `.env` file.
   3. Ensure the `.env` files contain the following variables:

       ```
       DB_URL=your_database_name
       POSTGRES_USER=your_username
       POSTGRES_PASSWORD=your_password
       ```

### Step 2: Run the Application

To start a service, `cd` to its folder and execute the following command:

```sh
mvn spring-boot:run
```
You may need to open several terminals to run multiple services simultaneously.


## Installation Steps

## Mac

### Java

Using Homebrew:

```shell
brew tap homebrew/cask-versions
brew install --cask temurin@17
```

Add the following line to your zsh profile (usually at `~/.zshrc`):
```shell
export PATH="/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin/:$PATH"
```


### Maven

Follow this guide carefully: https://maven.apache.org/install.html

### Docker

Follow this guide carefully: https://docs.docker.com/desktop/install/mac-install/

## Windows

### Java

Follow the instructions in this guide carefully: https://adoptium.net/installation/windows/

### Maven

Follow this guide carefully: https://maven.apache.org/guides/getting-started/windows-prerequisites.html


### Docker

Follow this official guide carefully:  https://docs.docker.com/desktop/install/windows-install/


