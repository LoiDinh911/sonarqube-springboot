pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-token')
        SONAR_HOST_URL = 'http://<sonar-server-ip>:9000'
        DOCKER_IMAGE = 'yourdockerhubusername/your-image-name'
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
                        -Dsonar.projectKey=myproject \
                        -Dsonar.host.url=$SONAR_HOST_URL \
                        -Dsonar.login=$SONAR_TOKEN
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
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
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
