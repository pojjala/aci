List<Path> ancestorFoldersPath(Path path) {
    if (path.parent == null) return []
    ancestorFoldersPath(path.parent) + [path.parent]
}

def generateFolders(List<Path> jenkinsfilePaths, Path rootFolder) {
    echo "+++++++++++++++++++++++++++++++++++++++++++++++++++"
    jenkinsfilePaths
            .collect { ancestorFoldersPath(rootFolder.resolve(it).parent) }
            .flatten()
    // Remove duplicates in case some Jenkinsfiles share ancestors for optimization.
            .unique()
            .each {
                // Effectively provision the folder.
                folder(it.toString())
            }
}

generateFolders(jenkinsfilePaths,rootFolder)
