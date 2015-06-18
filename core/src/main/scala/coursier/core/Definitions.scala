package coursier.core

case class Module(organization: String,
                  name: String) {

  def trim: Module = copy(
    organization = organization.trim,
    name = name.trim
  )
  override def toString = s"$organization:$name"
}

sealed abstract class Scope(val name: String)

case class Dependency(module: Module,
                      version: String,
                      scope: Scope,
                      `type`: String,
                      classifier: String,
                      exclusions: Set[(String, String)],
                      optional: Boolean) {
  def moduleVersion = (module, version)
}

case class Project(module: Module,
                   version: String,
                   dependencies: Seq[Dependency],
                   parent: Option[(Module, String)],
                   dependencyManagement: Seq[Dependency],
                   properties: Map[String, String],
                   profiles: Seq[Profile]) {
  def moduleVersion = (module, version)
}

object Scope {
  case object Compile extends Scope("compile")
  case object Runtime extends Scope("runtime")
  case object Test extends Scope("test")
  case object Provided extends Scope("provided")
  case object Import extends Scope("import")
  case class Other(override val name: String) extends Scope(name)
}

case class Activation(properties: Seq[(String, Option[String])])

case class Profile(id: String,
                   activeByDefault: Option[Boolean],
                   activation: Activation,
                   dependencies: Seq[Dependency],
                   dependencyManagement: Seq[Dependency],
                   properties: Map[String, String])
