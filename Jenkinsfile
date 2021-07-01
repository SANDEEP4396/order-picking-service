node {
    stage("clean workspace") {
        deleteDir()
    }
    stage('Maven Build'){
        sh "/usr/bin/mvn clean install"
    }
    stage('docker Build Image'){
     sh 'docker build -t sandeep/order-picking .'   
    }
    stage('docker run'){
        sh 'docker container run --name order-picking-container_api -p 8080:8080 -d sandeep/order-picking'
    }
}
