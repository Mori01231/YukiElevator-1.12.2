pipeline {
    agent any

    options {
        skipDefaultCheckout()
    }

    stages {
        stage('チェックアウト') {
            steps {
                checkout scm
            }
        }

        stage('ビルド') {
            steps {
                sh 'mvn -B clean install'
            }
        }

        stage('成果物を保存') {
            steps {
                archiveArtifacts 'target/*.jar'
            }
        }
    }
}
