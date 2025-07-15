pipeline {
    agent any
    tools {
        maven 'Maven-3'
    }
    environment {
        SONARQUBE_SERVER = 'SonarQubeServer'
    }
    stages {
        stage('Checkout git') {
            steps {
                git branch: 'main', url: 'https://github.com/snuppi19/EVO_IAM_SERVICE1.git'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Run Application') {
                    steps {
                        script {
                            // Chạy file .jar đã build
                            sh 'java -jar target/IAM_Service_1-0.0.1-SNAPSHOT.jar &'
                        }
                    }
                }
    }

    post {
        success {
            echo ' Pipeline completed successfully!'
        }
        failure {
            echo ' Pipeline failed!'
        }
        always {
            sh 'docker rm -f postgres-container || true'
        }
    }
}
