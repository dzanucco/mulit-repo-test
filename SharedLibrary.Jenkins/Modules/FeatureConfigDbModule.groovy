package Modules

import javax.sql.DataSource
import java.sql.Connection
import java.sql.Statement
import java.sql.PreparedStatement
import java.sql.ResultSet

def PrintFeatureLog(feature, logMessage)
{
    def log = "${logMessage}\n"
    log += "FeatureName: ${feature.FeatureName}\n"
    log += "RepoName: ${feature.RepoName}\n"
    log += "ServiceName: ${feature.ServiceName}\n"
    log += "BranchName: ${feature.BranchName}\n"
    log += "RepoType: ${feature.RepoType}\n"    
    log += "CommitId: ${feature.CommitId}\n\n"    
    log += "Feature: ${feature}\n"    
    println log
}

boolean AreFeaturesEqual(featureA, featureB) 
{
    if (featureA == null || featureB == null) 
    {
        return false // Handle null cases
    }

    def propertiesToCompare = [
        "Qa", "RepoName", "BranchName", "ServiceName", "FeatureName", 
        "CommitId", "RepoType", "ApiEnabled", "ApiDefBlobFilePath", 
        "PullRequstId", "IsReviewed", "GroupKey", "ReleaseBranch", 
        "ServiceConfig", "DeploymentType", "ApplicationVersion", 
        "ServiceVersion", "Regions", "Stage", "StorageRootDir", 
        "RitaVersion", "ApplicationId", "IsStateless", "ApplicationPackagePath", 
        "Comment", "IsDev", "IsQa", "IsNext", "IsReleased", 
        "ReleaseVersion", "IsHotfix", "HotfixVersions"
    ]

    return propertiesToCompare.every { prop ->
        featureA."${prop}" == featureB."${prop}"
    }
}

def CreateFeaturesTableIfNotExists(DataSource dataSource)
{
    def tableCreationSqlCmd = 'CREATE TABLE IF NOT EXISTS "rita-features" ( ';
    tableCreationSqlCmd += '	"id" INTEGER PRIMARY KEY AUTOINCREMENT, ';
    tableCreationSqlCmd += '	"Qa" INTEGER NOT NULL, ';
    tableCreationSqlCmd += '	"RepoName" TEXT NOT NULL, ';
    tableCreationSqlCmd += '	"BranchName" TEXT NOT NULL, ';
    tableCreationSqlCmd += '	"ServiceName" TEXT NOT NULL, ';
    tableCreationSqlCmd += '	"FeatureName" TEXT NOT NULL, ';
    tableCreationSqlCmd += '	"CommitId" TEXT NOT NULL, ';
    tableCreationSqlCmd += '	"RepoType" TEXT NOT NULL, ';
    tableCreationSqlCmd += '	"ApiEnabled" INTEGER NOT NULL, ';
    tableCreationSqlCmd += '	"ApiDefBlobFilePath" TEXT, ';
    tableCreationSqlCmd += '	"PullRequstId" INTEGER, ';
    tableCreationSqlCmd += '	"IsReviewed" INTEGER, ';
    tableCreationSqlCmd += '	"GroupKey" TEXT, ';
    tableCreationSqlCmd += '	"ReleaseBranch" TEXT, ';
    tableCreationSqlCmd += '	"ServiceConfig" TEXT, ';
    tableCreationSqlCmd += '	"DeploymentType" TEXT, ';
    tableCreationSqlCmd += '	"ApplicationVersion" TEXT, ';
    tableCreationSqlCmd += '	"ServiceVersion" TEXT, ';
    tableCreationSqlCmd += '	"Regions" TEXT, ';
    tableCreationSqlCmd += '	"Stage" TEXT, ';
    tableCreationSqlCmd += '	"StorageRootDir" TEXT, ';
    tableCreationSqlCmd += '	"RitaVersion" TEXT, ';
    tableCreationSqlCmd += '	"ApplicationId" TEXT, ';
    tableCreationSqlCmd += '	"IsStateless" INTEGER, ';
    tableCreationSqlCmd += '	"ApplicationPackagePath" TEXT, ';
    tableCreationSqlCmd += '	"Comment" TEXT, ';
    tableCreationSqlCmd += '	"IsDev" INTEGER, ';
    tableCreationSqlCmd += '	"IsQa" INTEGER, ';
    tableCreationSqlCmd += '	"IsNext" INTEGER, ';
    tableCreationSqlCmd += '	"IsReleased" INTEGER, ';
    tableCreationSqlCmd += '	"ReleaseVersion" TEXT, ';
    tableCreationSqlCmd += '	"IsHotfix" INTEGER, ';
    tableCreationSqlCmd += '	"HotfixVersions" TEXT ';
    tableCreationSqlCmd += '); '

    // Perform database operations
    Connection conn = null
    try {
        conn = dataSource.getConnection()
        Statement stmt = conn.createStatement() 

        println 'Creating table if not exists'
        stmt.executeUpdate(tableCreationSqlCmd)    

        stmt.close()
    } finally {
        if (conn != null) {
            conn.close()
        }
    }
}

def DeleteFeatureTable(DataSource dataSource)
{
    def sqlCmd = "DROP TABLE \"rita-features\""
    Connection conn = null
    try {
        conn = dataSource.getConnection()
        Statement stmt = conn.createStatement() 

        println 'Deleting table "rita-features"'
        stmt.executeUpdate(sqlCmd)    

        stmt.close()
    } finally {
        if (conn != null) {
            conn.close()
        }
    }
}

def InsertRitaFeaturesData(DataSource dataSource, feature)
{
    def sqlCmd = "INSERT INTO \"rita-features\" (Qa, RepoName, BranchName, ServiceName, FeatureName, CommitId, RepoType, ApiEnabled, " +
        "ApiDefBlobFilePath, PullRequstId, IsReviewed, GroupKey, ReleaseBranch, ServiceConfig, DeploymentType, ApplicationVersion, " +
        "ServiceVersion, Regions, Stage, StorageRootDir, RitaVersion, ApplicationId, IsStateless, " +
        "ApplicationPackagePath, Comment, IsDev, IsQa, IsNext, IsReleased, ReleaseVersion, IsHotfix, HotfixVersions)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    
    // Perform database operations
    Connection conn = null
    try {
        conn = dataSource.getConnection()
        Statement stmt = conn.createStatement() 

        def logMessage = "Inserting into the database the following value"
        PrintFeatureLog(feature, logMessage)

        PreparedStatement pstmt = conn.prepareStatement(sqlCmd);
        pstmt.setInt(1, feature.Qa ? 1 : 0)                         // Convert boolean to int
        pstmt.setString(2, feature.RepoName)
        pstmt.setString(3, feature.BranchName)
        pstmt.setString(4, feature.ServiceName)
        pstmt.setString(5, feature.FeatureName)
        pstmt.setString(6, feature.CommitId)
        pstmt.setString(7, feature.RepoType)
        pstmt.setInt(8, feature.ApiEnabled ? 1 : 0)                 // Convert boolean to int
        pstmt.setString(9, feature.ApiDefBlobFilePath)
        pstmt.setInt(10, feature.PullRequstId != null ? feature.PullRequstId as Integer : -1)
        pstmt.setInt(11, feature.IsReviewed ? 1 : 0)                // Convert boolean to int
        pstmt.setString(12, feature.GroupKey)
        pstmt.setString(13, feature.ReleaseBranch)
        pstmt.setString(14, feature.ServiceConfig)
        pstmt.setString(15, feature.DeploymentType)
        pstmt.setString(16, feature.ApplicationVersion)
        pstmt.setString(17, feature.ServiceVersion)
        pstmt.setString(18, feature.Regions?.join(', '))            // Convert list to comma-separated string
        pstmt.setString(19, feature.Stage)
        pstmt.setString(20, feature.StorageRootDir)
        pstmt.setString(21, feature.RitaVersion)
        pstmt.setString(22, feature.ApplicationId)
        pstmt.setInt(23, feature.IsStateless ? 1 : 0)               // Convert boolean to int
        pstmt.setString(24, feature.ApplicationPackagePath)
        pstmt.setString(25, feature.Comment)
        pstmt.setInt(26, feature.IsDev ? 1 : 0)                     // 1 if on DEV stage, otherwise 0
        pstmt.setInt(27, feature.IsQa ? 1 : 0)                      // 1 if on QA stage, otherwise 0
        pstmt.setInt(28, feature.IsNext ? 1 : 0)                    // 1 if on NEXT stage, otherwise 0
        pstmt.setInt(29, feature.IsReleased ? 1 : 0)                // 1 if Released to a specific version, otherwise 0
        pstmt.setString(30, feature.ReleaseVersion)                 // The version x.xx.x where a package have been released, otherwise null
        pstmt.setInt(31, feature.IsHotfix ? 1 : 0)                  // 1 if is an hotfix, otherwise 0
        pstmt.setString(32, feature.HotfixVersions?.join(', '))     // Convert list to comma-separated string

        pstmt.executeUpdate();
        pstmt.close()
    } finally {
        if (conn != null) {
            conn.close()
        }
    }
}

def UpdateRitaFeatureData(DataSource dataSource, feature)
{
    def sqlCmd = "UPDATE \"rita-features\" SET Qa = ?, RepoName = ?, BranchName = ?, ServiceName = ?, FeatureName = ?, CommitId = ?, " +
        "RepoType = ?, ApiEnabled = ?, ApiDefBlobFilePath = ?, PullRequstId = ?, IsReviewed = ?, GroupKey = ?, ReleaseBranch = ?, " +
        "ServiceConfig = ?, DeploymentType = ?, ApplicationVersion = ?, ServiceVersion = ?, Regions = ?, Stage = ?, StorageRootDir = ?, " +
        "RitaVersion = ?, ApplicationId = ?, IsStateless = ?, ApplicationPackagePath = ?, Comment = ?, IsDev = ?, IsQa = ?, IsNext = ?, " +
        "IsReleased = ?, ReleaseVersion = ?, IsHotfix = ?, HotfixVersions = ? WHERE id = ?"
    
    // Perform database operations
    Connection conn = null
    try {
        conn = dataSource.getConnection()
        Statement stmt = conn.createStatement() 

        def logMessage = "Updating the feature with id ${feature.Id}"
        PrintFeatureLog(feature, logMessage)

        PreparedStatement pstmt = conn.prepareStatement(sqlCmd);
        pstmt.setInt(1, feature.Qa ? 1 : 0)                         // Convert boolean to int
        pstmt.setString(2, feature.RepoName)
        pstmt.setString(3, feature.BranchName)
        pstmt.setString(4, feature.ServiceName)
        pstmt.setString(5, feature.FeatureName)
        pstmt.setString(6, feature.CommitId)
        pstmt.setString(7, feature.RepoType)
        pstmt.setInt(8, feature.ApiEnabled ? 1 : 0)                 // Convert boolean to int
        pstmt.setString(9, feature.ApiDefBlobFilePath)
        pstmt.setInt(10, feature.PullRequstId != null ? feature.PullRequstId as Integer : -1)
        pstmt.setInt(11, feature.IsReviewed ? 1 : 0)                // Convert boolean to int
        pstmt.setString(12, feature.GroupKey)
        pstmt.setString(13, feature.ReleaseBranch)
        pstmt.setString(14, feature.ServiceConfig)
        pstmt.setString(15, feature.DeploymentType)
        pstmt.setString(16, feature.ApplicationVersion)
        pstmt.setString(17, feature.ServiceVersion)
        pstmt.setString(18, feature.Regions?.join(', '))            // Convert list to comma-separated string
        pstmt.setString(19, feature.Stage)
        pstmt.setString(20, feature.StorageRootDir)
        pstmt.setString(21, feature.RitaVersion)
        pstmt.setString(22, feature.ApplicationId)
        pstmt.setInt(23, feature.IsStateless ? 1 : 0)               // Convert boolean to int
        pstmt.setString(24, feature.ApplicationPackagePath)
        pstmt.setString(25, feature.Comment)
        pstmt.setInt(26, feature.IsDev ? 1 : 0)                     // 1 if on DEV stage, otherwise 0
        pstmt.setInt(27, feature.IsQa ? 1 : 0)                      // 1 if on QA stage, otherwise 0
        pstmt.setInt(28, feature.IsNext ? 1 : 0)                    // 1 if on NEXT stage, otherwise 0
        pstmt.setInt(29, feature.IsReleased ? 1 : 0)                // 1 if Released to a specific version, otherwise 0
        pstmt.setString(30, feature.ReleaseVersion)                 // The version x.xx.x where a package have been released, otherwise null
        pstmt.setInt(31, feature.IsHotfix ? 1 : 0)                  // 1 if is an hotfix, otherwise 0
        pstmt.setString(32, feature.HotfixVersions?.join(', '))     // Convert list to comma-separated string
        pstmt.setInt(33, feature.Id)        

        pstmt.executeUpdate();
        pstmt.close()
    } finally {
        if (conn != null) {
            conn.close()
        }
    }
}

def DeleteRitaFeatureData(DataSource dataSource, feature)
{
    def sqlCmd = "DELETE FROM \"rita-features\" WHERE id = ?"
    
    Connection conn = null
    try {
        conn = dataSource.getConnection()
        Statement stmt = conn.createStatement() 

        def logMessage = "Delete the feature with id ${feature.Id}"
        PrintFeatureLog(feature, logMessage)

        PreparedStatement pstmt = conn.prepareStatement(sqlCmd);
        pstmt.setInt(1, feature.Id)        

        pstmt.executeUpdate();
        pstmt.close()
    } finally {
        if (conn != null) {
            conn.close()
        }
    }
}

def ReadFeatureEntryFromResultSet(ResultSet rs)
{
    def feature = [:]
    feature.Id = rs.getInt('id')
    feature.Qa = rs.getInt('Qa') == 1
    feature.RepoName = rs.getString('RepoName')
    feature.BranchName = rs.getString('BranchName')
    feature.ServiceName = rs.getString('ServiceName')
    feature.FeatureName = rs.getString('FeatureName')
    feature.CommitId = rs.getString('CommitId')
    feature.RepoType = rs.getString('RepoType')
    feature.ApiEnabled = rs.getInt('ApiEnabled') == 1
    feature.ApiDefBlobFilePath = rs.getString('ApiDefBlobFilePath')
    feature.PullRequestId = rs.getInt('PullRequstId')
    feature.IsReviewed = rs.getInt('IsReviewed') == 1
    feature.GroupKey = rs.getString('GroupKey')
    feature.ReleaseBranch = rs.getString('ReleaseBranch')
    feature.ServiceConfig = rs.getString('ServiceConfig')
    feature.DeploymentType = rs.getString('DeploymentType')
    feature.ApplicationVersion = rs.getString('ApplicationVersion')
    feature.ServiceVersion = rs.getString('ServiceVersion')
    feature.Regions = rs.getString('Regions')?.split(',')?.collect { it.trim() }
    feature.Stage = rs.getString('Stage')
    feature.StorageRootDir = rs.getString('StorageRootDir')
    feature.RitaVersion = rs.getString('RitaVersion')
    feature.ApplicationId = rs.getString('ApplicationId')
    feature.IsStateless = rs.getInt('IsStateless') == 1
    feature.ApplicationPackagePath = rs.getString('ApplicationPackagePath')
    feature.Comment = rs.getString('Comment')
    feature.IsDev = rs.getInt('IsDev') == 1
    feature.IsQa = rs.getInt('IsQa') == 1
    feature.IsNext = rs.getInt('IsNext') == 1
    feature.IsReleased = rs.getInt('IsReleased') == 1
    feature.ReleaseVersion = rs.getString('ReleaseVersion')
    feature.IsHotfix = rs.getInt('IsHotfix') == 1
    feature.HotfixVersions = rs.getString('HotfixVersions')?.split(',')?.collect { it.trim() }

    return feature
}

def ReadRitaFeaturesFilteredData(DataSource dataSource, sqlReadCmd)
{
    def items = []
    
    Connection conn = null
    try {
        conn = dataSource.getConnection()
        Statement stmt = conn.createStatement() 
        ResultSet rs = stmt.executeQuery(sqlReadCmd)
        while (rs.next()) {

            def feature = ReadFeatureEntryFromResultSet(rs)
            items.add(feature)
        }
        rs.close()    
        stmt.close()
    } finally {
        if (conn != null) {
            conn.close()
        }
    }
    return items
}

def GetAllRitaFeaturesData(DataSource dataSource)
{
    def readCmd = "SELECT * FROM \"rita-features\""
    println "Reading all rita-features data: ${readCmd}"
    return ReadRitaFeaturesFilteredData(dataSource, readCmd)
}

def GetReadyForQA(DataSource dataSource)
{
    def readCmd = "SELECT * FROM \"rita-features\" WHERE IsDev = 1 AND IsQa = 0 AND IsNext = 0 AND IsHotfix = 0 AND IsReleased = 0"
    println "Reading ReadyForQA from rita-features data: ${readCmd}"
    return ReadRitaFeaturesFilteredData(dataSource, readCmd)
    }

def GetReadyForStage(DataSource dataSource)
{
    def readCmd = "SELECT * FROM \"rita-features\" WHERE IsDev = 1 AND IsReviewed = 1 AND IsQa = 1 AND IsNext = 0 AND IsHotfix = 0 AND IsReleased = 0"
    println "Reading ReadyForStage from rita-features data: ${readCmd}"
    return ReadRitaFeaturesFilteredData(dataSource, readCmd)
}

def GetReadyForRelease(DataSource dataSource)
{
    def readCmd = "SELECT * FROM \"rita-features\" " +
    "WHERE IsDev = 1 " +
    "AND IsReviewed = 1 " +
    "AND IsQa = 1 " +
    "AND IsNext = 1 " +
    "AND IsHotfix = 0 " +
    "AND IsReleased = 0"
    println "Reading ReadyForRelease from rita-features data: ${readCmd}"
    return ReadRitaFeaturesFilteredData(dataSource, readCmd)
}

def GetHotfixes(DataSource dataSource)
{
    def readCmd = "SELECT * FROM \"rita-features\" WHERE IsHotfix = 1"

    println "Reading Hotfixes from rita-features data: ${readCmd}"
    return ReadRitaFeaturesFilteredData(dataSource, readCmd)
}

def GetReleased(DataSource dataSource)
{
    def readCmd = "SELECT * FROM \"rita-features\" WHERE IsDev = 1 AND IsReviewed = 1 AND IsQa = 1 AND IsNext = 1 AND IsHotfix = 0 AND IsReleased = 1"
    
    println "Reading Released from rita-features data: ${readCmd}"
    return ReadRitaFeaturesFilteredData(dataSource, readCmd)
}

def IsIstaNumberAlreadyExists(DataSource dataSource, serviceName, featureName)
{
    def isIstaNumberAlreadyExists = false
    def sqlCmd = "SELECT id FROM \"rita-features\" WHERE ServiceName = ? AND FeatureName = ? AND IsHotfix = 0 AND IsReleased = 0 " // ReadyForQA or ReadyForStage or ReadyForRelease

    // Perform database operations
    Connection conn = null
    try 
    {
        conn = dataSource.getConnection()

        def items = []
        def log = "Is ISTA number already exists for \n"
        log += "ServiceName: ${serviceName}\n"
        log += "FeatureName: ${featureName}\n"
        println log

        PreparedStatement pstmt = conn.prepareStatement(sqlCmd);
        pstmt.setString(1, serviceName)
        pstmt.setString(2, featureName)

        ResultSet rs =  pstmt.executeQuery();
        if (rs.next()) 
        {
            isIstaNumberAlreadyExists = true
        }
        rs.close()    
        pstmt.close()
    } finally {
        if (conn != null) 
        {
            conn.close()
        }
    }
    return isIstaNumberAlreadyExists
}

def GetReadyForQAFromServiceAndFeatureNames(DataSource dataSource, serviceName, featureName)
{
    def feature = [:]
    def sqlReadCmd = "SELECT * FROM \"rita-features\" WHERE IsDev = 1 AND ServiceName = ? AND FeatureName = ?"
    Connection conn = null
    try 
    {
        conn = dataSource.getConnection()

        PreparedStatement pstmt = conn.prepareStatement(sqlReadCmd);
        pstmt.setString(1, serviceName)
        pstmt.setString(2, featureName)

        println "Reading ReadyForQA with ServiceName <${serviceName}> and FeatureName <${featureName}> from the database..."
        ResultSet rs = pstmt.executeQuery()
        while (rs.next()) 
        {
            feature = ReadFeatureEntryFromResultSet(rs)
        }
        rs.close()    
        pstmt.close()
    } finally {
        if (conn != null) 
        {
            conn.close()
        }
    }
    return feature
}

def GetHotfixFromServiceAndFeatureNames(DataSource dataSource, serviceName, featureName)
{
    def feature = [:]
    def sqlReadCmd = "SELECT * FROM \"rita-features\" WHERE IsHotfix = 1 AND ServiceName = ? AND FeatureName = ?"
    Connection conn = null
    try {
        conn = dataSource.getConnection()
        
        PreparedStatement pstmt = conn.prepareStatement(sqlReadCmd);
        pstmt.setString(1, serviceName)
        pstmt.setString(2, featureName)
        
        println "Reading hotfix with ServiceName <${serviceName}> and FeatureName <${featureName}> from the database..."
        ResultSet rs = pstmt.executeQuery()
        while (rs.next()) 
        {
            feature = ReadFeatureEntryFromResultSet(rs)
        }
        rs.close()
        pstmt.close()
    } finally {
        if (conn != null) 
        {
            conn.close()
        }
    }
    return feature
}

def ImportFeatureConfigFileToDb(DataSource dataSource, featureConfig)
{
    println "Importing the rita-feature-config data into the database"
    featureConfig.each { section ->
        println "Importing the ${section.key} ${section.value.size()} values"
        section.value.each { feature ->
            
            switch(section.key) {
                case "ReadyForQA":
                    feature.IsDev = true
                    break;
                case "ReadyForStage":
                    feature.IsDev = true
                    feature.IsReviewed = true
                    feature.IsQa = true
                    break;
                case "ReadyForRelease":
                    feature.IsDev = true
                    feature.IsReviewed = true
                    feature.IsQa = true
                    feature.IsNext = true
                    break;
                case "Released":
                    feature.IsDev = true
                    feature.IsReviewed = true
                    feature.IsQa = true
                    feature.IsNext = true
                    feature.IsReleased = true
                    break;
                case "Hotfixes":
                    feature.IsHotfix = true
                    feature.HotfixVersions = feature.Versions
                    break;
            }
            InsertRitaFeaturesData(dataSource, feature)
        }
    }
}