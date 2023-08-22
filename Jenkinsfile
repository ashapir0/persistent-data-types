pipeline {
    agent any

    environment {
        // Initialize version to a default
        VERSION = '0.0.0'
    }

    tools {
        gradle 'Gradle 8.2'
    }

    options {
        quietPeriod(0)
        disableConcurrentBuilds()
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    userRemoteConfigs: [[
                        url: 'https://github.com/ashapir0/persistent-data-types.git',
                        credentialsId: 'GitHub'
                    ]],
                    branches: [[name: '*/main']]
                ])
            }
        }
        stage('Read Version') {
            steps {
                script {
                    // Read version from build.gradle.kts (assumes version is set like: version = "1.0.5")
                    VERSION = sh(script: "grep 'version =' build.gradle.kts | awk '{print \$3}' | tr -d '\"'", returnStdout: true).trim()
                    echo "Found version: ${VERSION}"
                }
            }
        }
        stage('Build with Gradle') {
            steps {
                sh './gradlew jar'
            }
        }
        stage('Publish to Nexus') {
            steps {
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "build/libs/persistent-data-types-${VERSION}.jar"]], mavenCoordinate: [artifactId: 'persistent-data-types', groupId: 'com.manya', packaging: 'jar', version: "${VERSION}"]]]
            }
        }
        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: "**/persistent-data-types-${VERSION}*.jar"
            }
        }
    }

    post {
        always {
            discordSend description: "**Build:** ${BUILD_NUMBER}\n**Version:** ${VERSION}\n**Status:** ${currentBuild.currentResult}", enableArtifactsList: true, footer: "Jenkins Discord Webhook", image: '', link: '', result: currentBuild.currentResult, scmWebUrl: 'https://github.com/ashapir0/persistent-data-types', showChangeset: true, thumbnail: '', title: "persistent-data-types #${BUILD_NUMBER}", webhookURL: 'https://discord.com/api/webhooks/1133051070558507079/oKA8k4hEyGW01Wo33LfVZ7nPCwApwxtlujEl1sQBGBHeKhtwQLYpnV001v4KQU-JOGBG'
        }
    }
}