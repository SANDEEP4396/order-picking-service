node {
    stage("clean workspace") {
        deleteDir()
    }
     stage("git checkout") {
        checkout scm

        def GIT_COMMIT = sh(returnStdout: true, script: "git rev-parse HEAD").trim().take(7)
        DOCKER_IMAGE_VERSION = "${BUILD_NUMBER}-${GIT_COMMIT}"
    }
    stage('Maven Build'){
        sh "/usr/bin/mvn clean install"
    }
    stage('docker Build Image'){
     sh 'sudo docker build -t sandeep4396/order-picking-egen .'   
    }
    stage('docker run'){
        sh 'sudo docker container run --network order-picking-service --name order-picking-container$(DOCKER_IMAGE_VERSION) -d -p 8081:8080  sandeep4396/order-picking-egen'
    }
}
