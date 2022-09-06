def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    pipeline{
        agent any
            stages{
                stage('folder'){
                    steps{
                        script{
                            jobDsl scriptText: "folder('prasadFolder')",    
                            ignoreExisting: true
                        }
                    }
                }
            }
        
      }
}
