def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    String repositoryName = env.JOB_NAME.split('/')[1]
    String rootFolderPath = "Generated/$repositoryName"

    pipeline{
        agent any
            stages{
                stage('folder'){
                    steps{
                        echo "--------------------------"
                        echo repositoryName.toString()
                        echo rootFolderPath.toString()
                        echo "--------------------------"
                        script{                       
                            jobDsl scriptText: "folder('prasadFolder')",
                            ignoreExisting: true
                        }
                    }
                }
            }
        
      }
}
