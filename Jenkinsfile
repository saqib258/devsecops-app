pipeline {
    agent any
    
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
                sh 'trivy fs .'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // This 'SonarQube' name must match your Jenkins System Configuration
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t devsecops-app .'
            }
        }
stage('Trivy Image Scan') {
    steps {
        sh 'trivy image devsecops-app'
    }
}

stage('Deploy Container') {
    steps {
        sh '''
        docker rm -f devsecops-running || true
        docker run -d -p 8085:8080 --name devsecops-running devsecops-app
        '''
    }
}
}

}
