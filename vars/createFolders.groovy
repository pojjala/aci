def call(body){
    def pipelineParams = [:]
    body.resolveStrategy = Closer.DELEGATE_FIRST
    body.delegate = pipelineParams
    
    pipeline { 
        folder('NextGen') {
            displayName('NextGenPrasad')
            description('Folder for NextGen')
        }

    }
}
