pipeline {
  agent any

  tools {
    jdk 'jdk-21'   // Jenkins 글로벌 툴에서 세팅해둔 JDK 21
  }

  options {
    timestamps()
    ansiColor('xterm')
    disableConcurrentBuilds()
  }

  environment {
    APP_NAME    = 'hobom-internal-backend'
    REGISTRY    = 'docker.io'
    IMAGE_REPO  = '<hub-username>/hobom-internal-backend'
    IMAGE_TAG   = "${REGISTRY}/${IMAGE_REPO}:${env.BUILD_NUMBER}"

    REGISTRY_CRED = 'dockerhub-cred'     // Jenkins에 저장한 DockerHub ID/PW
    SSH_CRED      = 'deploy-ssh-key-id'  // 배포 서버 SSH key
    DEPLOY_HOST   = 'deploy.example.com' // k3s 마스터 노드
    DEPLOY_PATH   = '/srv/hobom-internal-backend'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'git --no-pager log -1 --pretty=oneline'
      }
    }

    stage('Build JAR') {
      steps {
        sh './gradlew clean bootJar -x test'
        sh 'ls -lh build/libs/'
      }
      post {
        success {
          archiveArtifacts artifacts: 'build/libs/*.jar', onlyIfSuccessful: true
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh """
          docker build -t ${IMAGE_TAG} .
        """
      }
    }

    stage('Push Image') {
      steps {
        withCredentials([usernamePassword(credentialsId: env.REGISTRY_CRED, usernameVariable: 'REG_USER', passwordVariable: 'REG_PASS')]) {
          sh """
            echo "$REG_PASS" | docker login ${REGISTRY} -u "$REG_USER" --password-stdin
            docker push ${IMAGE_TAG}
            docker logout ${REGISTRY}
          """
        }
      }
    }

    stage('Deploy (only develop)') {
      when { expression { env.BRANCH_NAME == 'develop' } }
      steps {
        sshagent (credentials: [env.SSH_CRED]) {
          sh """
            ssh -o StrictHostKeyChecking=no ${DEPLOY_HOST} '
              set -e
              cd ${DEPLOY_PATH}
              export APP_IMAGE=${IMAGE_TAG}
              # k3s/Helm 배포 (예: helm upgrade --install)
              # 또는 docker compose
              docker compose pull
              docker compose up -d --remove-orphans
              docker image prune -f
            '
          """
        }
      }
    }
  }

  post {
    success {
      echo "✅ Build #${env.BUILD_NUMBER} OK (${env.BRANCH_NAME})"
      script {
        if (env.BRANCH_NAME == 'develop') {
          echo "🚀 Deployed ${IMAGE_TAG} to ${DEPLOY_HOST}"
        }
      }
    }
    failure {
      echo "❌ Build failed (${env.BRANCH_NAME})"
    }
  }
}
