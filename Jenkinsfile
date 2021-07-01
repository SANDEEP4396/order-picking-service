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
     sh 'docker build -t sandeep4396/order-picking-api .'   
    }
    stage('docker run'){
        sh 'docker container run --name order-picking-container_api -p 8080:8080 -d sandeep/order-picking'
    }
}
