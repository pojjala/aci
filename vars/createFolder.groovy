def call(body) {
    def project = 'payments'
//     https://github.com/pojjala/payments.git
    def branchApi = new URL("https://github.com/pojjala/${project}/branches")
    def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())
    branches.each {
        def branchName = it.name
        def jobName = "${project}-${branchName}".replaceAll('/','-')
        job(jobName) {
            scm {
                git("git://github.com/${project}.git", branchName)
            }
            steps {
                maven("test -Dproject.name=${project}/${branchName}")
            }
        }
    }
    
    
//     def config = [:]
//     body.resolveStrategy = Closure.DELEGATE_FIRST
//     body.delegate = config
//     body()
    
//     pipeline{
//         agent any
//         environment{
//             PROJECT_NAME = "ACIWorldWide"
//             }
//         stages{
//             stage('Creation of folders'){
//                     steps{
//                         checkout scm
//                         script{   
// //                              jobDsl(scriptText: "folder('project-ovln')")
//                           jobDsl(scriptText: libraryResource('aci/vars/myFoldertest.groovy'),removedJobAction: 'IGNORE')   
//                            }
//                         }
//              }
//         }  
//      }
}
