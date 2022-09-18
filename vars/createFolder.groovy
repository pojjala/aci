def call(body) {
    def config = [:]
//     def PROJECT_NAME = "ABC"
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    pipeline{
        agent any
//         environment{
//             PROJECT_NAME = "ACIWorldWide"
//             }
//         environment { 
//             PROJECT_NAME = "ACIWorldWide"
//         } 
        stages{
            stage('Creation of folders'){
                    steps{
                        PROJECT_NAME = "ACIWorldWide"
                        checkout scm
                        script{   
                         echo "Prepare workspace"
                         sh "pwd" 
                         sh "ls -ll" 
                            
                            jobDsl(scriptText: 'folder("${PROJECT_NAME}")')
//                           jobDsl(scriptText: libraryResource('aci/resouces/folderTest.groovy'),removedJobAction: 'IGNORE')   
                           }
                        }
             }
        }  
     }
}
