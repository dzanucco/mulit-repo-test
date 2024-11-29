package modules
import org.yaml.snakeyaml.Yaml

def LoadYamlFile(yamlFilePath)
{
    def fileContent = new File(yamlFilePath).text
    def yaml = new Yaml()
    return yaml.load(fileContent)
}

def WriteYamlFile(content, yamlFilePath)
{
    def yaml = new Yaml()

    FileWriter writer = new FileWriter(yamlFilePath)
    yaml.dump(content, writer)
    writer.close()
}