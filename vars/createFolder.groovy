def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    pipeline{
        agent any
        environment{
            PROJECT_NAME = "ACIWorldWide"
            }
        stages{
            stage('Creation of folders'){
                    steps{
                        checkout scm
                        dsl{ 
                            folder('project-ovln')
                           }
//                         script{   
//                              jobDsl(scriptText: "folder('project-ovln')")
// //                           jobDsl(scriptText: libraryResource('aci/vars/myFoldertest.groovy'),removedJobAction: 'IGNORE')   
//                            }
                        }
             }
        }  
     }
}
