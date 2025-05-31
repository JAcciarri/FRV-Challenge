pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'jdk-24'
        allure 'allure'
    }

    triggers {
        githubPush()
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Restore Allure History') {
            steps {
                script {
                    try {
                        unstash 'allure-history'
                        sh 'cp -r target/allure-history/. target/allure-results/history || true'
                    } catch (e) {
                        echo 'No hay historial previo disponible'
                    }
                }
            }
        }

        stage('Run SmokeSuite') {
            steps {
                echo "Ejecutando suite testng/SmokeSuite.xml"
                sh 'mvn clean test -DsuiteXmlFile=testng/SmokeSuite.xml'
            }
        }

        stage('Allure Report') {
            steps {
                script {
                    sh 'mkdir -p target/allure-results/history'
                    sh 'cp -r target/allure-results/history target/allure-history || true'
                    stash name: 'allure-history', includes: 'target/allure-history/**'
                }

                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
        failure {
            echo 'La ejecución falló. Revisar los reportes.'
        }
        success {
            echo 'Todos los tests de SmokeSuite pasaron correctamente.'
        }
    }
}
