rootProject.name = "Insight"

include("app")
project(":app").projectDir = file("modules/app")

include("controller")
project(":controller").projectDir = file("modules/controller")

include("model")
project(":model").projectDir = file("modules/model")

include("repository")
project(":repository").projectDir = file("modules/repository")

include("service")
project(":service").projectDir = file("modules/service")

include("security")
project(":security").projectDir = file("modules/security")

include("dto")
project(":dto").projectDir = file("modules/dto")

include("utils")
project(":utils").projectDir = file("modules/utils")