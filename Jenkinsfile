def registry = 'https://devopsapp.jfrog.io'
def imageName = 'devopsapp.jfrog.io/devopsapp-docker-local/devopsapp'
def version   = '2.1.2'

pipeline {
    agent { label 'build-slave' }

    environment {
        SONAR_TOKEN_SECRET = credentials('SONAR_TOKEN')
        SONAR_PROJECT_KEY  = 'techguy007'
        SONAR_ORG          = 'techguy007'
    }

    stages {
        stage('1. Checkout') {
            steps {
                echo "Checking out code from GitHub..."
                git branch: 'main', url: 'https://github.com/techguy007/devopsApp.git'
            }
        }

        stage('2. Build') {
            steps {
                echo "----------- Build Started ----------"
                sh 'mvn clean package -Dmaven.test.skip=true'
                echo "----------- Build Completed ----------"
            }
        }

        stage('3. Test') {
            steps {
                echo "----------- Unit Test Started ----------"
                sh 'mvn surefire-report:report'
                echo "----------- Unit Test Completed ----------"
            }
        }

        stage('4. SonarCloud Analysis') {
            steps {
                echo "Starting SonarCloud analysis..."
                withSonarQubeEnv('SonarCloud') {
                    sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                          -Dsonar.organization=${SONAR_ORG} \
                          -Dsonar.host.url=https://sonarcloud.io \
                          -Dsonar.login=${SONAR_TOKEN_SECRET}
                    """
                }
            }
        }

        stage('5. Poll SonarCloud Quality Gate') {
            steps {
                script {
                    echo "Polling SonarCloud API for Quality Gate status..."
                    def maxRetries = 20
                    def delaySeconds = 30
                    def status = "PENDING"

                    for (int i = 1; i <= maxRetries; i++) {
                        def response = sh(
                            script: """curl -s -u ${SONAR_TOKEN_SECRET}: \
                            "https://sonarcloud.io/api/qualitygates/project_status?projectKey=${SONAR_PROJECT_KEY}" """,
                            returnStdout: true
                        ).trim()

                        if (response.contains('"status":"OK"')) {
                            echo "✅ SonarCloud Quality Gate PASSED!"
                            status = "OK"
                            break
                        } else if (response.contains('"status":"ERROR"')) {
                            echo "❌ SonarCloud Quality Gate FAILED!"
                            status = "ERROR"
                            break
                        } else {
                            echo "SonarCloud still analyzing... (attempt ${i}/${maxRetries})"
                            sleep time: delaySeconds, unit: 'SECONDS'
                        }
                    }

                    if (status == "PENDING") {
                        error "Timed out waiting for SonarCloud analysis to complete."
                    } else if (status == "ERROR") {
                        error "SonarCloud Quality Gate failed. Check dashboard for details."
                    }
                }
            }
        }

        stage('6. Jar Publish') {
            steps {
                script {
                    echo '<--------------- Jar Publish Started --------------->'
                    def server = Artifactory.newServer(
                        url: registry + '/artifactory',
                        credentialsId: 'artifactory_token'
                    )
                    def properties = "buildid=${env.BUILD_ID},commitid=${env.GIT_COMMIT ?: 'unknown'}"
                    def uploadSpec = """{
                        "files": [
                            {
                                "pattern": "target/*.jar",
                                "target": "devopsapp-libs-release-local/",
                                "props": "${properties}",
                                "flat": "true"
                            }
                        ]
                    }"""
                    def buildInfo = server.upload(uploadSpec)
                    buildInfo.env.collect()
                    server.publishBuildInfo(buildInfo)
                    echo '<--------------- Jar Publish Ended --------------->'
                }
            }
        }

        stage('7. Docker Build') {
            steps {
                script {
                    echo '<--------------- Docker Build Started --------------->'
                    app = docker.build("${imageName}:${version}")
                    echo '<--------------- Docker Build Ended --------------->'
                }
            }
        }

        stage('8. Docker Publish') {
            steps {
                script {
                    echo '<--------------- Docker Publish Started --------------->'
                    docker.withRegistry(registry, 'artifactory_token') {
                        app.push()
                    }
                    echo '<--------------- Docker Publish Ended --------------->'
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline finished. Cleaning up workspace..."
            cleanWs()
        }
    }
}
