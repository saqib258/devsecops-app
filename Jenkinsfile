pipeline {
    agent any
    tools { maven 'maven' }
    stages {
        stage('Fetch Code') { steps { checkout scm } }
        stage('Build & Package') { steps { sh 'mvn clean package -DskipTests' } }
        stage('Trivy Security Scan') { steps { sh 'trivy fs .' } }
        stage('SonarQube Analysis') { steps { withSonarQubeEnv('SonarQube') { sh 'mvn sonar:sonar' } } }
        stage('Docker Build') { steps { sh 'docker build -t devsecops-app .' } }
    }
}
