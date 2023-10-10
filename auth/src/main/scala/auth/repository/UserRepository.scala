package auth.repository

import auth.domain.user.{ User, UserInfo }
import cats.syntax.all.toFunctorOps
import doobie.{ ConnectionIO, Fragment }
import doobie.implicits.toSqlInterpolator

trait UserRepository[F[_]]:
  def insert(userInfo: UserInfo): F[User]
  def select(loginName: String): F[User]
  def selectPasswordHash(loginName: String): F[String]

object UserRepository extends UserRepository[ConnectionIO]:
  private object SQL:
    def insert(userInfo: UserInfo): Fragment =
      sql"""
          |INSERT INTO user_accounts (login_name, first_name, last_name, email, password_hash)
          |VALUES (${userInfo.loginName}, ${userInfo.firstName}, ${userInfo.lastName}, ${userInfo.email}, ${userInfo.passwordHash.trim})
          |""".stripMargin

    def select(loginName: String): Fragment =
      sql"""
          |SELECT id, login_name, first_name, last_name, email FROM user_accounts
          |WHERE login_name = $loginName
          """.stripMargin

    def selectPasswordHash(loginName: String): Fragment =
      sql"SELECT password_hash FROM user_accounts WHERE login_name = $loginName"

  override def insert(userInfo: UserInfo): ConnectionIO[User] =
    SQL.insert(userInfo).update.withUniqueGeneratedKeys[User]("id", "login_name", "first_name", "last_name", "email")

  override def select(loginName: String): ConnectionIO[User] =
    SQL.select(loginName).query[User].unique

  override def selectPasswordHash(loginName: String): ConnectionIO[String] =
    SQL.selectPasswordHash(loginName).query[String].unique
