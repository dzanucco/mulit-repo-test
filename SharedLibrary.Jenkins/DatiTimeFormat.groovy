import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

def GetKeyVaultExpirationDate()
{
    def LocalDate twoYearsFromNow = LocalDate.now().plusYears(2)
    return twoYearsFromNow.atStartOfDay().toInstant(ZoneOffset.UTC).toString()
}

def GetKeyVaultExpirationDateFormatted()
{
    def LocalDate twoYearsFromNow = LocalDate.now().plusYears(2)
    def ZonedDateTime utcDateTime = twoYearsFromNow.atStartOfDay(ZoneOffset.UTC)
    def DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return utcDateTime.format(formatter)
}

def GetLocalDate()
{
    def LocalDate twoYearsFromNow = LocalDate.now().plusYears(2)
    def ZonedDateTime utcDateTime = twoYearsFromNow.atStartOfDay(ZoneOffset.UTC)
    def DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return utcDateTime.format(formatter)
}

println "Not Formatted: ${GetKeyVaultExpirationDate()}"
println "Formatted: ${GetKeyVaultExpirationDateFormatted()}"
println "Local Data: ${GetLocalDate()}"