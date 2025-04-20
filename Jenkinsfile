pipeline {
    agent any

    tools {
        jdk 'JDK17'        // Optional if set in Jenkins
        maven 'Maven3'     // Optional if using Maven tool config
    }

    environment {
        BUILD_ENV = 'prod'
    }

    stages {
        stage('Clone Repo') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'   // Or just 'mvn clean package' if no wrapper
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build succeeded!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
