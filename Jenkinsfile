pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-token') // Stored in Jenkins credentials
        SONAR_HOST_URL = 'http://sonarqube:9000' // 🧠 Important: NOT localhost
        DOCKER_IMAGE = 'loind0911/sonarqube-springboot'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarQube') {
                    sh """
                      mvn sonar:sonar \
                        -Dsonar.projectKey=sonarqube-springboot \
                        -Dsonar.host.url=$SONAR_HOST_URL \
                        -Dsonar.token=$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: '5ceaf9c5-6d8f-4273-899f-deed4c2fccd8', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                      echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                      docker push $DOCKER_IMAGE
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ CI pipeline completed successfully!'
        }
        failure {
            echo '❌ CI pipeline failed. Check logs.'
        }
    }
}
