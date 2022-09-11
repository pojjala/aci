def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    pipeline{
        agent any
        environment{
//             BITBUCKET_PROJECT_URL = "https://github.com/pojjala/payments.git"
//             REPOSITORY_ROOT = "payments"
//             INCLUDE_PATTERN = "develope feature/app1"
            PROJECT_NAME = "ACIWorldWide"
            }
        stages{
            stage('Creation of folders'){
                    steps{
//                         checkout scm
                        script{   
//                             jobDsl(scriptText: folder('PrasadOVLN') )
                            jobDsl(scriptText: "folder('myFolder')")
//                           jobDsl(scriptText: libraryResource('aci/vars/myFoldertest.groovy'),removedJobAction: 'IGNORE')   
                        }
                    }
                }
      }
    }
}
