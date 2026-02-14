pipeline {
    agent any
    
    options {
        timeout(time: 20, unit: 'MINUTES')
    }

    tools {
        maven 'maven'
        jdk 'java17'
    }

    stages {

        stage('Fetch Code') {
            steps {
                checkout scm
            }
        }

        stage('Build & Package') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Trivy Security Scan') {
            steps {
                sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL .'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t devsecops-app:${BUILD_NUMBER} ."
            }
        }
    }

    post {
        always { echo 'Pipeline finished' }
        success { echo 'Build successful' }
        failure { echo 'Build failed' }
    }
}

