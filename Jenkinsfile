node {
    stage 'チェックアウト'
    git url: 'https://github.com/AzisabaDev/YukiElevator.git'

    stage 'ビルド'
    sh 'mvn clean install'

    stage '成果物を保存'
    archiveArtifacts 'target/*.jar'
}
