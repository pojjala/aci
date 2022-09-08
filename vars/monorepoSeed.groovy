import java.nio.file.Path
import java.nio.file.Paths
/**
 * Provision items on Jenkins.
 * @param rootFolderPath A root folder path.
 * @param repositoryURL The repository URL.
 * @return The list of Jenkinsfile paths for which corresponding items have been provisioned.
 */
List<String> provisionItems(String rootFolderPath, String repositoryURL) {
    // Find all Jenkinsfiles.
    echo "provisionItems------------------------->-"
    List<String> jenkinsfilePaths = findFiles(glob: '**/*/jenkinsfile').collect { it.path }
    echo "after jenkinsfilePaths------------------------->-"
    // Provision folder and Multibranch Pipelines.
    jobDsl(
            scriptText: libraryResource('createMultibranchPipelines.groovy'),
            additionalParameters: [
                    jenkinsfilePathsStr: jenkinsfilePaths,
                    rootFolderStr      : rootFolderPath,
       //             repositoryURL      : env.GIT_URL
            ],
            // The following may be set to 'DELETE'. Note that branches will compete to delete and recreate items
            // unless you only provision items from the default branch.
            removedJobAction: 'IGNORE'
    )

    return jenkinsfilePaths
}


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
                            List<String> jenkinsfilePaths = provisionItems(rootFolderPath, env.BITBUCKET_PROJECT_URL)
                            echo "--------------------------"
                            echo jenkinsfilePaths.toString()
                            echo "--------------------------"
                            jobDsl scriptText: "folder('prasadFolder')",
                            ignoreExisting: true
                        }
                    }
                }
            }
        
      }
}
