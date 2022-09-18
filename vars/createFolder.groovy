def call(body) {
    def config = [:]
    def env.PROJECT_NAME = "ABC"
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    pipeline{
        agent any
        environment{
            env.PROJECT_NAME = "ACIWorldWide"
            }
        stages{
            stage('Creation of folders'){
                    steps{
                        checkout scm
                        script{   
                         echo "Prepare workspace"
                         sh "pwd" 
                         sh "ls -ll" 
                            
                            jobDsl(scriptText: 'folder("${env.PROJECT_NAME}")')
//                           jobDsl(scriptText: libraryResource('aci/resouces/folderTest.groovy'),removedJobAction: 'IGNORE')   
                           }
                        }
             }
        }  
     }
}
