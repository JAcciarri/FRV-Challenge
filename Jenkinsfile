pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'jdk-17'
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

        stage('Run SmokeSuite') {
            steps {
                echo "Ejecutando suite testng/SmokeSuite.xml"
                sh 'mvn clean test -DsuiteXmlFile=testng/SmokeSuite.xml'
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