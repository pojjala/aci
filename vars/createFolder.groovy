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
                          $class: 'ExecuteDslScripts',
                          targets: ['aci/vars/myFoldertest.groovy']
                        
                        
//                         checkout scm
//                         script{   
// //                           jobDsl(scriptText: "folder('folderName')")  
//                              jobDsl(scriptText: "folder('folderName')")  
//                           jobDsl(scriptText: libraryResource('aci/vars/myFoldertest.groovy'),removedJobAction: 'IGNORE')   
//                         }
                    }
                }
      }
    }
}
