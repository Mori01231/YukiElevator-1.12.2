pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean install'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts 'target/*.jar'
            }
        }
    }
}
