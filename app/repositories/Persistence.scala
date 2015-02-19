package repositories

import scala.concurrent.Future

trait Persistence[T, ID] {
  def list(): Future[List[T]]
  def create(entity: T): Future[Unit]
  def update(entity: T): Future[Unit]
  def get(id: ID): Future[Option[T]]
  def delete(id: ID): Future[Unit]
}
