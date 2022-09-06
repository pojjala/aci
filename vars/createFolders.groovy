def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    pipeline{
        folder('NextGen') {
            displayName('NextGenPrasad')
            description('Folder for NextGen')
        }
    }
}
