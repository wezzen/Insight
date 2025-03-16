rootProject.name = "Insight"

include("controller")
project(":controller").projectDir = file("modules/controller")

include("model")
project(":model").projectDir = file("modules/model")

include("repository")
project(":repository").projectDir = file("modules/repository")

include("service")
project(":service").projectDir = file("modules/service")