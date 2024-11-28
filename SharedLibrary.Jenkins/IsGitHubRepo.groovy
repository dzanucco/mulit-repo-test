def IsGitHubRepository(repoName)
{
    def whitelistedRepos = [
        "rita-dev-user-default-function",
        "rita-dev-vehicle-default-vehicleidentservice",
        "rita-dev-ista-default-container",
        "rita-dev-multilanguage-devopstool-backend"
    ]
 
    if (repoName == null || repoName == "")
        return false
    else if (repoName.contains("rita-ui-"))
        return true
    else if (repoName.contains("rita-dev-") && repoName.contains("-service"))
        return true
    else if (repoName.contains("rita-dev-") && repoName.contains("-publicservice"))
        return true
    else if (whitelistedRepos.contains(repoName))
        return true
    return false
}


def IsBitBucket(repoName)
{
    return !IsGitHubRepository(repoName)
}

def isRepo1 = IsGitHubRepository("rita-ui-webapp-default-frontend")
def isRepo2 = IsGitHubRepository("rita-dev-case-default-service")
def isNotRepo1 = IsGitHubRepository("case-default-service")
def isNotRepo2 = IsGitHubRepository("vehicle-default-publicservice")
def isNotRepo3 = IsGitHubRepository("")
def isNotRepo4 = IsGitHubRepository(null)
def isNotRepo5 = IsGitHubRepository("rita-dev-vehicle-default-publicservice")
def isNotRepo6 = IsGitHubRepository("rita-dev-multilanguage-devopstool-backend")
def isNotRepo7 = IsGitHubRepository("rita-dev-user-default-function")
def isNotRepo8 = IsGitHubRepository("rita-dev-ista-default-container")
def isNotRepo9 = IsGitHubRepository("rita-dev-ediabas-container-service")
def isNotRepo10 = IsGitHubRepository("rita-dev-vehicle-default-vehicleidentservice")

println "IsGitHub rita-ui-webapp-default-frontend ${isRepo1}"
println "IsRepo rita-dev-case-default-service ${isRepo2}"
println "IsRepo case-default-service ${isNotRepo1}"
println "IsRepo vehicle-default-publicservice ${isNotRepo2}"
println "IsRepo \"\" ${isNotRepo3}"
println "IsRepo null ${isNotRepo4}"
println "IsRepo rita-dev-vehicle-default-publicservice ${isNotRepo5}"
println "IsRepo rita-dev-multilanguage-devopstool-backend ${isNotRepo6}"
println "IsRepo rita-dev-user-default-function ${isNotRepo7}"
println "IsRepo rita-dev-ista-default-container ${isNotRepo8}"
println "IsRepo rita-dev-ediabas-container-service ${isNotRepo9}"
println "IsRepo rita-dev-vehicle-default-vehicleidentservice ${isNotRepo10}"
println ""
isRepo1 = IsBitBucket("rita-ui-webapp-default-frontend")
isRepo2 = IsBitBucket("rita-dev-case-default-service")
isNotRepo1 = IsBitBucket("case-default-service")
isNotRepo2 = IsBitBucket("vehicle-default-publicservice")
isNotRepo3 = IsBitBucket("")
isNotRepo4 = IsBitBucket(null)
isNotRepo5 = IsBitBucket("rita-dev-vehicle-default-publicservice")