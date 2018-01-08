# Recommendations
`Last version: 0.2`
## Docker compose
- Build app: mvn clean package
- Run: docker-compose up --build

## Endpoints
App is accessible on port 8086.

### Recommendation
* `GET: /v1/recommendations/{userId}` Returns recommended accommodations for user with userId

### Config
* `GET: /v1/config` Returns all configurable values

### Health
* `GET: /health` Returns info about service health
* `POST: /v1/health-demo/healthy` Changes config value `healthy` (parameter is boolean) 

### Metrics
* `GET: /metrics` Returns metrics