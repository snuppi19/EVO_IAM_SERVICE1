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
                            // Chạy file .jar đã build (giả sử nằm trong target/)
                            sh 'java -jar target/IAM_Service_1-0.0.1-SNAPSHOT.jar &'
                            // Lưu PID để tắt sau này (tùy chọn)
                            sh 'echo $! > app.pid'
                        }
                    }
                }

        stage('Shutdown and Clean') {
                    steps {
                        script {
                            // Tắt ứng dụng nếu đang chạy
                            sh 'if [ -f app.pid ]; then kill $(cat app.pid) && rm app.pid; fi'
                            // Dọn dẹp file build (tùy chọn)
                            sh 'rm -rf target/*'
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
