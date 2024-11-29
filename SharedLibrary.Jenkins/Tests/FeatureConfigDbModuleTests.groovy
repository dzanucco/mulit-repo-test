import spock.lang.*
import javax.sql.DataSource
import java.sql.Statement
import java.sql.ResultSet
import java.sql.Connection

class FeatureConfigDbModuleTests extends Specification 
{
    def databaseModule = new modules.DatabaseModule()
    def featureConfigDbModule = new modules.FeatureConfigDbModule()
    def tempDbFile = ""
    DataSource dataSource

    // called before each test
    def setup() 
    {
        tempDbFile = File.createTempFile("testdb", ".sqlite")
        dataSource = databaseModule.GetDataSource(tempDbFile)
    }
    
    // called after each test
    def cleanup() 
    {
        // Delete the database file
        tempDbFile?.delete()
    }

    def GetFakeFeaturesData()
    {
        def items = []
        def IsDev = [
            Qa: false,
            RepoName: "IsDev",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME_Dev",
            FeatureName: "FEATURE_NAME_Dev",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsDev: true
        ]
        def IsDevReviewed = [
            Qa: false,
            RepoName: "IsDevReviewed",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME_DevReviewd",
            FeatureName: "FEATURE_NAME_DevReviewd",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsDev: true,
            IsReviewed : true
        ]
        def IsQa = [
            Qa: false,
            RepoName: "IsQa",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME_Qa",
            FeatureName: "FEATURE_NAME_Qa",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsDev: true,
            IsReviewed : true,
            IsQa : true
        ]
        def IsNext = [
            Qa: false,
            RepoName: "IsNext",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME_Next",
            FeatureName: "FEATURE_NAME_Next",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsDev: true,
            IsReviewed : true,
            IsQa : true,
            IsNext : true
        ]
        def IsReleased = [
            Qa: false,
            RepoName: "IsReleased",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME_Released",
            FeatureName: "FEATURE_NAME_Released",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsDev: true,
            IsReviewed : true,
            IsQa : true,
            IsNext : true,
            IsReleased : true,
            ReleaseVersion : "1.15.0"
        ]
        def IsHotfix = [
            Qa: false,
            RepoName: "IsHotfix",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME_Hotfix",
            FeatureName: "FEATURE_NAME_Hotfix",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsHotfix: true,
            HotfixVersions : "1.15.0,1.16.0"
        ]
        items.add(IsDev)
        items.add(IsDevReviewed)
        items.add(IsQa)
        items.add(IsNext)
        items.add(IsReleased)
        items.add(IsHotfix)

        return items
    }

    def "AreFeaturesEqual_True"() 
    {
        given: "Given two equal feature objects"
        def featureA = [
            Qa: 1, RepoName: "Repo1", BranchName: "main", ServiceName: "Service1", 
            FeatureName: "Feature1", CommitId: "abc123", RepoType: "Git", 
            ApiEnabled: 1, ApiDefBlobFilePath: "/path/to/blob", PullRequstId: 123, 
            IsReviewed: 1, GroupKey: "group1", ReleaseBranch: "release/main", 
            ServiceConfig: "config", DeploymentType: "Kubernetes", 
            ApplicationVersion: "1.0.0", ServiceVersion: "1.0.0", 
            Regions: "US,EU", Stage: "prod", StorageRootDir: "/storage/root", 
            RitaVersion: "1.0.0", ApplicationId: "app-123", IsStateless: 0, 
            ApplicationPackagePath: "/packages/app", Comment: "Production release", 
            IsDev: 1, IsQa: 1, IsNext: 1, IsReleased: 1, 
            ReleaseVersion: "1.0.0", IsHotfix: 0, HotfixVersions: ["1.15.0","1.16.0"]
        ]

        def featureB = [
            Qa: 1, RepoName: "Repo1", BranchName: "main", ServiceName: "Service1", 
            FeatureName: "Feature1", CommitId: "abc123", RepoType: "Git", 
            ApiEnabled: 1, ApiDefBlobFilePath: "/path/to/blob", PullRequstId: 123, 
            IsReviewed: 1, GroupKey: "group1", ReleaseBranch: "release/main", 
            ServiceConfig: "config", DeploymentType: "Kubernetes", 
            ApplicationVersion: "1.0.0", ServiceVersion: "1.0.0", 
            Regions: "US,EU", Stage: "prod", StorageRootDir: "/storage/root", 
            RitaVersion: "1.0.0", ApplicationId: "app-123", IsStateless: 0, 
            ApplicationPackagePath: "/packages/app", Comment: "Production release", 
            IsDev: 1, IsQa: 1, IsNext: 1, IsReleased: 1, 
            ReleaseVersion: "1.0.0", IsHotfix: 0, HotfixVersions: ["1.15.0","1.16.0"]
        ]

        when: "Checking if the objects have the same values"
        def areEquals = featureConfigDbModule.AreFeaturesEqual(featureA, featureB)

        then: "The result should be true"
        areEquals == true
    }

    def "AreFeaturesEqual_False"() 
    {
        given: "Given two equal feature objects"
        def featureA = [
            Qa: 1, RepoName: "Repo1", BranchName: "main", ServiceName: "Service1", 
            FeatureName: "Feature1", CommitId: "abc123", RepoType: "Git", 
            ApiEnabled: 1, ApiDefBlobFilePath: "/path/to/blob", PullRequstId: 123, 
            IsReviewed: 0, GroupKey: "group1", ReleaseBranch: "release/main", 
            ServiceConfig: "config", DeploymentType: "Kubernetes", 
            ApplicationVersion: "1.0.0", ServiceVersion: "1.0.0", 
            Regions: "US,EU", Stage: "prod", StorageRootDir: "/storage/root", 
            RitaVersion: "1.0.0", ApplicationId: "app-123", IsStateless: 0, 
            ApplicationPackagePath: "/packages/app", Comment: "Production release", 
            IsDev: 1, IsQa: 0, IsNext: 0, IsReleased: '1', 
            ReleaseVersion: "1.0.0", IsHotfix: 0, HotfixVersions: []
        ]

        def featureB = [
            Qa: 1, RepoName: "Repo1", BranchName: "main", ServiceName: "Service1", 
            FeatureName: "Feature1", CommitId: "abc123", RepoType: "Git", 
            ApiEnabled: 1, ApiDefBlobFilePath: "/path/to/blob", PullRequstId: 123, 
            IsReviewed: 0, GroupKey: "group1", ReleaseBranch: "release/main", 
            ServiceConfig: "config", DeploymentType: "Kubernetes", 
            ApplicationVersion: "1.0.0", ServiceVersion: "1.0.0", 
            Regions: "US,EU", Stage: "prod", StorageRootDir: "/storage/root", 
            RitaVersion: "1.0.0", ApplicationId: "app-123", IsStateless: 0, 
            ApplicationPackagePath: "/packages/app", Comment: "Production release", 
            IsDev: 1, IsQa: 1, IsNext: 0, IsReleased: 1, 
            ReleaseVersion: "1.0.0", IsHotfix: 0, HotfixVersions: []
        ]
        when: "Checking if the objects have the same values"
        def areEquals = featureConfigDbModule.AreFeaturesEqual(featureA, featureB)

        then: "The result should be true"
        areEquals == false
    }

    def "CreateFeaturesTableIfNotExists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)

        when: "Checking if the table exists"
        Statement stmt = null
        Connection connection = null
        def tableExists = false
        try {
            connection = dataSource.getConnection()
            stmt = connection.createStatement()
            def resultSet = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='rita-features'")
            tableExists = resultSet.next() // True if the table exists
        } finally {
            if (stmt != null) stmt.close()
        }

        then: "The table should exist"
        tableExists == true
    }

    def "DeleteFeatureTable"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)

        when: "Deleting the rita-features table"
        featureConfigDbModule.DeleteFeatureTable(dataSource)

        then: "The table should exist"
        Statement stmt = null
        Connection connection = null
        def tableExists = false
        try {
            connection = dataSource.getConnection()
            stmt = connection.createStatement()
            def resultSet = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='rita-features'")
            tableExists = resultSet.next() // True if the table exists
        } finally {
            if (stmt != null) stmt.close()
        }
    }

    def "InsertRitaFeaturesData_ReadyForQA"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)

        when: "Inserting a new element into the rita-features table"
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_InsertRitaFeaturesData",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME",
            FeatureName: "FEATURE_NAME",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsDev: true
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)

        then: "The element should exists"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 1

        and:
        items[0].Qa == feature.Qa
        items[0].RepoName == feature.RepoName
        items[0].BranchName == feature.BranchName
        items[0].ServiceName == feature.ServiceName
        items[0].FeatureName == feature.FeatureName
        items[0].CommitId == feature.CommitId
        items[0].RepoType == feature.RepoType
        items[0].ApiEnabled == feature.ApiEnabled
        items[0].ApiDefBlobFilePath == feature.ApiDefBlobFilePath
        items[0].IsDev == feature.IsDev
    }

    def "UpdateRitaFeaturesData_ReadyForQA"() 
    {
        given: "A new table with one element in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def randomName = UUID.randomUUID().toString()
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_${randomName}",
            BranchName: "FULL_BRANCH_NAME_${randomName}",
            ServiceName : "SERVICE_NAME_${randomName}",
            FeatureName: "FEATURE_NAME_${randomName}",
            CommitId: "COMMIT_ID_${randomName}",
            RepoType: "REPO_TYPE_${randomName}",
            ApiEnabled: false,
            ApiDefBlobFilePath: "ApiDefBlobFilePath_${randomName}",
            IsDev: true
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)
        // get feature id
        def features = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        feature.Id = features[0].Id

        when: "Updating the element into the rita-features table"
        feature.IsReviewed = true
        feature.CommitId = "asdasfsadgfa98sdf68a76gdfa"
        featureConfigDbModule.UpdateRitaFeatureData(dataSource, feature)

        then: "The element should be updated"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 1

        and:
        items[0].Qa == feature.Qa
        items[0].RepoName == feature.RepoName
        items[0].BranchName == feature.BranchName
        items[0].ServiceName == feature.ServiceName
        items[0].FeatureName == feature.FeatureName
        items[0].CommitId == feature.CommitId
        items[0].RepoType == feature.RepoType
        items[0].ApiEnabled == feature.ApiEnabled
        items[0].ApiDefBlobFilePath == feature.ApiDefBlobFilePath
        items[0].IsDev == feature.IsDev
        items[0].IsReviewed == feature.IsReviewed
        items[0].CommitId == feature.CommitId
    }

    def "InsertRitaFeaturesData_Hotfix"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)

        when: "Inserting a new element into the rita-features table"
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_InsertRitaFeaturesData",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME",
            FeatureName: "FEATURE_NAME",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsHotfix: true,
            HotfixVersions: ["1.15.0","1.16.0"]
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)

        then: "The element should exists"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 1

        and:
        items[0].RepoName == "REPO_NAME_InsertRitaFeaturesData"
    }

    def "UpdateRitaFeaturesData_Hotfix"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_InsertRitaFeaturesData",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME",
            FeatureName: "FEATURE_NAME",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsHotfix: true
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)
        // get feature id
        def features = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        feature.Id = features[0].Id

        when: "Updating the hotfix into the rita-features table"
        feature.CommitId = "asdasfsadgfa98sdf68a76gdfa"
        feature.HotfixVersions = ["1.15.0","1.16.0","1.17.0"]
        featureConfigDbModule.UpdateRitaFeatureData(dataSource, feature)

        then: "The element should be updated"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 1

        and:
        items[0].Qa == feature.Qa
        items[0].RepoName == feature.RepoName
        items[0].BranchName == feature.BranchName
        items[0].ServiceName == feature.ServiceName
        items[0].FeatureName == feature.FeatureName
        items[0].CommitId == feature.CommitId
        items[0].RepoType == feature.RepoType
        items[0].ApiEnabled == feature.ApiEnabled
        items[0].ApiDefBlobFilePath == feature.ApiDefBlobFilePath
        items[0].IsHotfix == feature.IsHotfix
        items[0].CommitId == feature.CommitId
    }

    def "InsertRitaFeaturesData_ReadyForStage"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)

        when: "Inserting a new element into the rita-features table"
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_InsertRitaFeaturesData",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME",
            FeatureName: "FEATURE_NAME",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsHotfix: true
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)

        then: "The element should exists"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 1

        and:
        items[0].RepoName == "REPO_NAME_InsertRitaFeaturesData"
    }

    def "InsertRitaFeaturesData_ReadyForRelease"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)

        when: "Inserting a new element into the rita-features table"
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_InsertRitaFeaturesData",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME",
            FeatureName: "FEATURE_NAME",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsHotfix: true
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)

        then: "The element should exists"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 1

        and:
        items[0].RepoName == "REPO_NAME_InsertRitaFeaturesData"
    }

    def "InsertRitaFeaturesData_Released"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)

        when: "Inserting a new element into the rita-features table"
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_InsertRitaFeaturesData",
            BranchName: "FULL_BRANCH_NAME",
            ServiceName : "SERVICE_NAME",
            FeatureName: "FEATURE_NAME",
            CommitId: "COMMIT_ID",
            RepoType: "REPO_TYPE",
            ApiEnabled: false,
            ApiDefBlobFilePath: "true",
            IsHotfix: true
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)

        then: "The element should exists"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 1

        and:
        items[0].RepoName == "REPO_NAME_InsertRitaFeaturesData"
    }

    def "DeleteRitaFeatureData_element_exists_should_delete_it"() 
    {
        given: "A new table with one element in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def randomName = UUID.randomUUID().toString()
        def feature = [
            Qa: false,
            RepoName: "REPO_NAME_${randomName}",
            BranchName: "FULL_BRANCH_NAME_${randomName}",
            ServiceName : "SERVICE_NAME_${randomName}",
            FeatureName: "FEATURE_NAME_${randomName}",
            CommitId: "COMMIT_ID_${randomName}",
            RepoType: "REPO_TYPE_${randomName}",
            ApiEnabled: false,
            ApiDefBlobFilePath: "ApiDefBlobFilePath_${randomName}",
            IsDev: true
        ]
        featureConfigDbModule.InsertRitaFeaturesData(dataSource, feature)
        // get the inserted feature
        def features = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")

        when: "Deleting the element from the rita-features table"
        featureConfigDbModule.DeleteRitaFeatureData(dataSource, features[0])

        then: "The element should be deleted"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == 0
    }

    def "DeleteRitaFeatureData_element_not_exists_nothing_to_delete"() 
    {
        given: "A new table with one element in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def itemsToInsert = GetFakeFeaturesData()
        itemsToInsert.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "Deleting the element from the rita-features table"
        def featureToDelete = [
            Id : 99
        ]
        featureConfigDbModule.DeleteRitaFeatureData(dataSource, featureToDelete)

        then: "The element should be deleted"
        def items = featureConfigDbModule.ReadRitaFeaturesFilteredData(dataSource, "SELECT * FROM \"rita-features\"")
        items.size() == itemsToInsert.size()
    }

    def "GetReadyForQA"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "GetReadyForQA"
        def readyForQA = featureConfigDbModule.GetReadyForQA(dataSource)

        then: "Should be two elements"
        readyForQA.size() == 2
        and:
        readyForQA[0].RepoName == "IsDev"
        readyForQA[1].RepoName == "IsDevReviewed"
    }

    def "GetReadyForStage"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "GetReadyForQA"
        def readyForStage = featureConfigDbModule.GetReadyForStage(dataSource)

        then: "Should be one element"
        readyForStage.size() == 1
        and:
        readyForStage[0].RepoName == "IsQa"
    }

    def "GetReadyForRelease"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "GetReadyForQA"
        def readyForRelease = featureConfigDbModule.GetReadyForRelease(dataSource)

        then: "Should be one element"
        readyForRelease.size() == 1
        and:
        readyForRelease[0].RepoName == "IsNext"
    }

    def "GetHotfixes"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "GetReadyForQA"
        def hotfixes = featureConfigDbModule.GetHotfixes(dataSource)

        then: "Should be one element"
        hotfixes.size() == 1
        and:
        hotfixes[0].RepoName == "IsHotfix"
    }

    def "GetReleased"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "GetReadyForQA"
        def released = featureConfigDbModule.GetReleased(dataSource)

        then: "Should be one element"
        released.size() == 1
        and:
        released[0].RepoName == "IsReleased"
    }

    def "IsIstaNumberAlreadyExists_should_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "IsIstaNumberAlreadyExists"
        def exists = featureConfigDbModule.IsIstaNumberAlreadyExists(dataSource, "SERVICE_NAME_Dev", "FEATURE_NAME_Dev")

        then: "Should exists"
        exists == true
    }

    def "IsIstaNumberAlreadyExists_wrong_service_name_should_not_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "IsIstaNumberAlreadyExists"
        def exists = featureConfigDbModule.IsIstaNumberAlreadyExists(dataSource, "SERVICE_NAME_Test", "FEATURE_NAME_Dev")

        then: "Should not exists"
        exists == false
    }

    def "IsIstaNumberAlreadyExists_wrong_feature_name_should_not_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "IsIstaNumberAlreadyExists"
        def exists = featureConfigDbModule.IsIstaNumberAlreadyExists(dataSource, "SERVICE_NAME_Dev", "FEATURE_NAME_Test")

        then: "Should not exists"
        exists == false
    }

    def "IsIstaNumberAlreadyExists_wrong_service_and_feature_name_should_not_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "IsIstaNumberAlreadyExists"
        def exists = featureConfigDbModule.IsIstaNumberAlreadyExists(dataSource, "SERVICE_NAME_Test", "FEATURE_NAME_Test")

        then: "Should not exists"
        exists == false
    }

    def "IsIstaNumberAlreadyExists_should_not_exists_hotfix"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "IsIstaNumberAlreadyExists"
        def exists = featureConfigDbModule.IsIstaNumberAlreadyExists(dataSource, "SERVICE_NAME_Hotfix", "FEATURE_NAME_Hotfix")

        then: "Should not exists"
        exists == false
    }

    def "IsIstaNumberAlreadyExists_should_not_exists_released"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
        }

        when: "IsIstaNumberAlreadyExists"
        def exists = featureConfigDbModule.IsIstaNumberAlreadyExists(dataSource, "SERVICE_NAME_Released", "FEATURE_NAME_Released")

        then: "Should not exists"
        exists == false
    }

    def "GetReadyForQAFromServiceAndFeatureNames_should_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            if (i.RepoName.contains("Dev"))
            {
                featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
            }
        }

        when: "GetReadyForQA"
        def exists = featureConfigDbModule.GetReadyForQAFromServiceAndFeatureNames(dataSource, "SERVICE_NAME_Dev", "FEATURE_NAME_Dev")

        then: "Should exists"
        exists.RepoName == "IsDev"
    }

    def "GetReadyForQAFromServiceAndFeatureNames_should_not_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            if (!i.RepoName.contains("Dev"))
            {
                featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
            }
        }

        when: "GetReadyForQA"
        def exists = featureConfigDbModule.GetReadyForQAFromServiceAndFeatureNames(dataSource, "SERVICE_NAME_HotFix", "FEATURE_NAME_HotFix")

        then: "Should exists"
        exists == [:]
    }

    def "GetHotfixFromServiceAndFeatureNames_should_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            if (i.RepoName.contains("Hotfix"))  // insert only the hotfix feature
            {
                featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
            }
        }

        when: "GetReadyForQA"
        def exists = featureConfigDbModule.GetHotfixFromServiceAndFeatureNames(dataSource, "SERVICE_NAME_Hotfix", "FEATURE_NAME_Hotfix")

        then: "Should exists"
        exists.RepoName == "IsHotfix"
    }

    def "GetHotfixFromServiceAndFeatureNames_should_not_exists"() 
    {
        given: "A new table in the database"
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        def items = GetFakeFeaturesData()
        items.each{ i ->
            if (!i.RepoName.contains("Hotfix")) // insert all features except the hotfix one
            {
                featureConfigDbModule.InsertRitaFeaturesData(dataSource, i)
            }
        }

        when: "GetReadyForQA"
        def exists = featureConfigDbModule.GetHotfixFromServiceAndFeatureNames(dataSource, "SERVICE_NAME_Dev", "FEATURE_NAME_Dev")

        then: "Should exists"
        exists == [:]
    }

    def "ImportFeatureConfigFileToDb"() 
    {
        given: "Read yml rita-features config file"
        def yamlFileModule = new modules.YamlFileModule()
        def featuresConfig = yamlFileModule.LoadYamlFile("./ConfigFiles/rita-feature-config.yaml")
        featureConfigDbModule.CreateFeaturesTableIfNotExists(dataSource)
        
        when: "Import config data"
        featureConfigDbModule.ImportFeatureConfigFileToDb(dataSource, featuresConfig)

        then: "Ready for QA elements should be the right number"
        def readyForQA = featureConfigDbModule.GetReadyForQA(dataSource)
        readyForQA.size() == featuresConfig.ReadyForQA.size()
        
        and: "Ready for Stage elements should be the right number"
        def readyForStage = featureConfigDbModule.GetReadyForStage(dataSource)
        readyForStage.size() == featuresConfig.ReadyForStage.size()

        and: "Ready for Release elements should be the right number"
        def readyForRelease = featureConfigDbModule.GetReadyForRelease(dataSource)        
        readyForRelease.size() == featuresConfig.ReadyForRelease.size()

        and: "Hotfixes elements should be the right number"
        def hotfixes = featureConfigDbModule.GetHotfixes(dataSource)
        hotfixes.size() == featuresConfig.Hotfixes.size()

        and: "Release elements should be the right number"
        def released = featureConfigDbModule.GetReleased(dataSource)
        released.size() == featuresConfig.Released.size()

        and: "Total elements should be the right number"
        def items = featureConfigDbModule.GetAllRitaFeaturesData(dataSource)
        items.size() == (featuresConfig.ReadyForQA.size()+featuresConfig.ReadyForStage.size()+featuresConfig.ReadyForRelease.size()+featuresConfig.Hotfixes.size()+featuresConfig.Released.size())
    }
}