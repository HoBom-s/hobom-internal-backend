@Library('hobom-shared-lib') _
hobomPipeline(
  serviceName:    'dev-hobom-internal-backend',
  hostPort:       '8081',
  containerPort:  '8081',
  memory:         '1536m',
  cpus:           '2',
  envPath:        '/etc/hobom-dev/dev-hobom-internal-backend/.env',
  addHost:        true,
  smokeCheckPath: '/actuator/health'
)
