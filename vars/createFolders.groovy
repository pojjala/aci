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
                            jobDsl scriptText: "displayName('prasadFolder')",
                            jobDsl scriptText: "description('Folder for prasadFolder')",                            
                            ignoreExisting: true
                        }
                    }
                }
            }
        
      }
}
