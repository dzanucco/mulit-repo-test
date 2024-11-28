def ManageMapsValues(collection)
{
    def keyVaultKeys = [:]
    collection.each { key, kvValue ->
        def keyVaultUrl = kvValue.replace("{\"uri\":\"", "").replace("\"}","")
        keyVaultKeys.put(key, keyVaultUrl)
    }

    return keyVaultKeys
}

def FillMap(size)
{
    def map = [:]
    (1..size).collect { 
        map.put("key-${it}", "{\"uri\":\"${it}")
    }
    return map
}

def map = FillMap(10)

println map
def managedMap = ManageMapsValues(map)
println managedMap
