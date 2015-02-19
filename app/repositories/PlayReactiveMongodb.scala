package repositories

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.api.Play.current

trait PlayReactiveMongodb {
  /** Returns the current instance of the driver. */
  def driver = ReactiveMongoPlugin.driver
  /** Returns the current MongoConnection instance (the connection pool manager). */
  def connection = ReactiveMongoPlugin.connection
  /** Returns the default database (as specified in `application.conf`). */
  def db = ReactiveMongoPlugin.db
}

