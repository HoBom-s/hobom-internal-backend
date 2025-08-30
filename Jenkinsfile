pipeline {
  agent none

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    // Docker Hub
    REGISTRY      = 'docker.io'
    IMAGE_REPO    = 'jjockrod/hobom-system'
    SERVICE_NAME  = 'hobom-internal-backend'
    IMAGE_TAG     = "${REGISTRY}/${IMAGE_REPO}:${SERVICE_NAME}-${env.BUILD_NUMBER}"
    IMAGE_LATEST  = "${REGISTRY}/${IMAGE_REPO}:${SERVICE_NAME}-latest"
    REGISTRY_CRED = 'dockerhub-cred'
    READ_CRED_ID  = 'dockerhub-readonly'

    // Remote server
    APP_NAME      = 'hobom-internal-backend'
    DEPLOY_HOST   = 'ishisha.iptime.org'
    DEPLOY_PORT   = '22223'
    DEPLOY_USER   = 'infra-admin'
    SSH_CRED_ID   = 'deploy-ssh-key'

    // Remote .env 경로 (서버에만 존재)
    ENV_PATH      = '/etc/hobom-internal-backend/.env'

    HOST_PORT     = '8081'
    CONTAINER_PORT= '8081'
  }

  stages {
    stage('Build & Push (Kaniko)') {
      agent {
        kubernetes {
          yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      command: ["/busybox/sh","-c","sleep 9999999"]
      volumeMounts:
        - name: kaniko-docker-config
          mountPath: /kaniko/.docker
  volumes:
    - name: kaniko-docker-config
      emptyDir: {}
          """
        }
      }
      steps {
        checkout scm
        container('kaniko') {
          withCredentials([usernamePassword(credentialsId: env.REGISTRY_CRED, usernameVariable: 'REG_USER', passwordVariable: 'REG_PASS')]) {
            sh '''
              set -eu
              # mask credentials
              set +x
              AUTH="$(printf '%s' "$REG_USER:$REG_PASS" | base64 -w0 2>/dev/null || printf '%s' "$REG_USER:$REG_PASS" | base64)"
              cat > /kaniko/.docker/config.json <<CFG
{ "auths": { "https://index.docker.io/v1/": { "auth": "$AUTH" } } }
CFG
              set -x

              /kaniko/executor \
                --context "$WORKSPACE" \
                --dockerfile "$WORKSPACE/Dockerfile" \
                --destination "${IMAGE_TAG}" \
                --destination "${IMAGE_LATEST}" \
                --cache=false \
                --verbosity=info
            '''
          }
        }
      }
    }

    stage('Deploy container to server') {
      when { anyOf { branch 'develop'; branch 'main' } }
      agent any
      steps {
        sshagent (credentials: [env.SSH_CRED_ID]) {
          withCredentials([usernamePassword(credentialsId: env.READ_CRED_ID, usernameVariable: 'PULL_USER', passwordVariable: 'PULL_PASS')]) {
            sh '''
set -eux
ssh -o StrictHostKeyChecking=no -p "$DEPLOY_PORT" "$DEPLOY_USER@$DEPLOY_HOST" \
  APP_NAME="$APP_NAME" \
  IMAGE="$IMAGE_TAG" \
  CONTAINER="$APP_NAME" \
  ENV_PATH="$ENV_PATH" \
  HOST_PORT="$HOST_PORT" \
  CONTAINER_PORT="$CONTAINER_PORT" \
  PULL_USER="$PULL_USER" \
  PULL_PASS="$PULL_PASS" \
  bash -s <<'EOS'
set -euo pipefail
echo "[REMOTE] Deploying $APP_NAME with image $IMAGE"

if ! command -v docker >/dev/null 2>&1; then
  echo "[REMOTE][ERROR] docker not found. Install docker and add $USER to docker group."
  exit 1
fi

echo "$PULL_PASS" | docker login docker.io -u "$PULL_USER" --password-stdin

if [ ! -f "$ENV_PATH" ]; then
  echo "[REMOTE][ERROR] $ENV_PATH not found. Create it first."
  exit 1
fi

docker pull "$IMAGE" || (echo "[REMOTE][ERROR] docker pull failed" && exit 1)

if docker ps -a --format '{{.Names}}' | grep -w "$CONTAINER" >/dev/null 2>&1; then
  docker stop "$CONTAINER" || true
  docker rm "$CONTAINER" || true
fi

docker run -d --name "$CONTAINER" \
  --restart unless-stopped \
  --env-file "$ENV_PATH" \
  -p "${HOST_PORT}:${CONTAINER_PORT}" \
  "$IMAGE"

docker ps --filter "name=$CONTAINER" --format "table {{.Names}}\\t{{.Image}}\\t{{.Status}}\\t{{.Ports}}"
EOS
'''
          }
        }
      }
    }

    stage('Smoke check (optional)') {
      when { anyOf { branch 'develop'; branch 'main' } }
      agent any
      steps {
        sshagent (credentials: [env.SSH_CRED_ID]) {
          sh """
            ssh -o StrictHostKeyChecking=no -p ${env.DEPLOY_PORT} ${env.DEPLOY_USER}@${env.DEPLOY_HOST} '
              curl -fsS http://localhost:${env.HOST_PORT}/actuator/health || true
            '
          """
        }
      }
    }
  }

  post {
    success { echo "✅ Build #${env.BUILD_NUMBER} → pushed ${env.IMAGE_TAG} & deployed on ${env.DEPLOY_HOST}" }
    failure { echo "❌ Build failed (${env.BRANCH_NAME})" }
  }
}
