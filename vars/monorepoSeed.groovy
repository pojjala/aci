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
    List<String> jenkinsfilePaths = findFiles(glob: '**/*/jenkinsfile').collect { it.path }
    echo "Beginning of provisionItems  Method -------------------------->"       
        jobDsl(
            scriptText: libraryResource('createMultibranchPipelines.groovy'),
            additionalParameters: [
                    jenkinsfilePathsStr: jenkinsfilePaths,
                    rootFolderStr      : rootFolderPath,
                    repositoryURL      : env.GIT_URL
            ],
            // The following may be set to 'DELETE'. Note that branches will compete to delete and recreate items
            // unless you only provision items from the default branch.
            removedJobAction: 'IGNORE'
    )
    
    echo "End of provisionItems  Method -------------------------->"       
    return jenkinsfilePaths
}


/**
 * Get the most relevant baseline revision.
 * @return A revision.
 */
String getBaselineRevision() {
    // Depending on your seed pipeline configuration and preferences, you can set the baseline revision to a target
    // branch, e.g. the repository's default branch or even `env.CHANGE_TARGET` if Jenkins is configured to discover
    // pull requests.
    [env.GIT_PREVIOUS_SUCCESSFUL_COMMIT, env.GIT_PREVIOUS_COMMIT]
    // Look for the first existing existing revision. Commits can be removed (e.g. with a `git push --force`), so a
    // previous build revision may not exist anymore.
            .find { revision ->
                revision != null && sh(script: "git rev-parse --quiet --verify $revision", returnStdout: true) == 0
            } ?: 'HEAD^'
}

/**
 * Get the list of changed directories.
 * @param baselineRevision A revision to compare to the current revision.
 * @return The list of directories which include changes.
 */
List<String> getChangedDirectories(String baselineRevision) {
    // Jenkins native interface to retrieve changes, i.e. `currentBuild.changeSets`, returns an empty list for newly
    // created branches (see https://issues.jenkins.io/browse/JENKINS-14138), so let's use `git` instead.
    sh(
            label: 'List changed directories',
            script: "git diff --name-only $baselineRevision | xargs -L1 dirname | uniq",
            returnStdout: true,
    ).split().toList()
}

/**
 * Find Multibranch Pipelines which Jenkinsfile is located in a directory that includes changes.
 * @param changedFilesPathStr List of changed files paths.
 * @param jenkinsfilePathsStr List of Jenkinsfile paths.
 * @return A list of Pipeline names, relative to the repository root.
 */
// `java.nio.file.Path(s)` instances are not serializable, so we have to add the following annotation.
@NonCPS
static List<String> findRelevantMultibranchPipelines(List<String> changedFilesPathStr, List<String> jenkinsfilePathsStr) {
    List<Path> changedFilesPath = changedFilesPathStr.collect { Paths.get(it) }
    List<Path> jenkinsfilePaths = jenkinsfilePathsStr.collect { Paths.get(it) }

    changedFilesPath.inject([]) { pipelines, changedFilePath ->
        def matchingJenkinsfile = jenkinsfilePaths
                .find { jenkinsfilePath -> changedFilePath.startsWith(jenkinsfilePath.parent) }
        matchingJenkinsfile != null ? pipelines + [matchingJenkinsfile.parent.toString()] : pipelines
    }.unique()
}

/**
 * Get the list of Multibranch Pipelines that should be run according to the changeset.
 * @param jenkinsfilePaths The list of Jenkinsfiles paths.
 * @return The list of Multibranch Pipelines to run relative to the repository root.
 */
List<String> findMultibranchPipelinesToRun(List<String> jenkinsfilePaths) {
    findRelevantMultibranchPipelines(getChangedDirectories(baselineRevision), jenkinsfilePaths)
}

/**
 * Run pipelines.
 * @param rootFolderPath The common root folder of Multibranch Pipelines.
 * @param multibranchPipelinesToRun The list of Multibranch Pipelines for which a Pipeline is run.
 */
def runPipelines(String rootFolderPath, List<String> multibranchPipelinesToRun) {
    parallel(multibranchPipelinesToRun.inject([:]) { stages, multibranchPipelineToRun ->
        stages + [("Build $multibranchPipelinesToRun"): {
            def pipelineName = "$rootFolderPath/$multibranchPipelinesToRun/${URLEncoder.encode(env.CHANGE_BRANCH ?: env.GIT_BRANCH, 'UTF-8')}"
            // For new branches, Jenkins will receive an event from the version control system to provision the
            // corresponding Pipeline under the Multibranch Pipeline item. We have to wait for Jenkins to process the
            // event so a build can be triggered.
            timeout(time: 5, unit: 'MINUTES') {
                waitUntil(initialRecurrencePeriod: 1e3) {
                    def pipeline = Jenkins.instance.getItemByFullName(pipelineName)
                    pipeline && !pipeline.isDisabled()
                }
            }

            // Trigger downstream builds.
            build(job: pipelineName, propagate: true, wait: true)
        }]
    })
}
view rawrunRelevantJenkinsPipelines.medium.groovy hosted with ❤ by GitHub


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
                            List<String> jenkinsfilePaths = provisionItems(rootFolderPath, env.BITBUCKET_PROJECT_URL)
                            List<String> multibranchPipelinesToRun = findMultibranchPipelinesToRun(jenkinsfilePaths)
                            runPipelines(rootFolderPath, multibranchPipelinesToRun)
                            echo "end of stage--------------------------"                           
                        }
                    }
                }
            }
        
      }
}

List<Path> jenkinsfilePaths = jenkinsfilePathsStr.collect { Paths.get(it) }
echo jenkinsfilePaths.toString()
echo 'printenv'
Path rootFoldger = Paths.get(rootFolderStr)
echo "Before    Generated -------------------------->"       
generateFolders(jenkinsfilePaths, rootFolder)
