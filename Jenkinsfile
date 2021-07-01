node {
    stage('Maven Build'){
        sh "mvn install -DskipTests"
    }
    stage('docker Build Image'){
     sh 'docker build -t sandeep/order-picking .'   
    }
    stage('docker run'){
        sh 'docker container run --name order-picking-container_api -p 8080:8080 -d sandeep/order-picking'
    }
}
