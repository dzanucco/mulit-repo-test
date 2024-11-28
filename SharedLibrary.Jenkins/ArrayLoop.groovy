ExternalServices = ["TEMP", "ALAS", "CARDATA", "TSRD"]

ExternalServices.each { x ->

    if (x == "ALAS")
    {
        return
    }
    println(x)
}