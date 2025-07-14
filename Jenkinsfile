pipeline {
    agent any
    tools {
        maven 'Maven-3'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/snuppi19/EVO_IAM_SERVICE1.git'
            }
        }
        stage('Start PostgreSQL') {
            steps {
                script {
                    sh 'docker run -d --name postgres-container -e POSTGRES_DB=IAMservice -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=01092004 -p 5434:5432 postgres:latest'
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t Keycloak_mini:latest .'
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {

            sh 'docker rm -f postgres-container || true'
        }
    }
}