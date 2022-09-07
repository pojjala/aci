def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    pipeline{
        agent any
        environment{
            BITBUCKET_PROJECT_URL = "https://github.com/pojjala/payments.git"
            REPOSITORY_ROOT = "payments"
            INCLUDE_PATTERN = "develope feature/app1"
            }
        stages{
            stage('Provision and execute any new Multibranch Pipeline'){
                    steps{
                        checkout scm
                        script{
                            String rootFolderPath = env.REPOSITORY_ROOT
                            echo "--------------------------"
                            echo rootFolderPath.toString()
                            echo "--------------------------"
                            jobDsl scriptText: "folder('prasadFolder')",
                            ignoreExisting: true
                        }
                    }
                }
            }
        
      }
}
