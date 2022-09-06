def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    pipeline{
     //   folder('NextGen') {
     //       displayName('NextGenPrasad')
     //       description('Folder for NextGen')
            
        jobDsl scriptText: "folder('New Folder')",
        jobDsl scriptText: "displayName('NextGenPrasad')",
        jobDsl scriptText: "description('Folder for NextGen')",
        ignoreExisting: true
        }
    }
}
