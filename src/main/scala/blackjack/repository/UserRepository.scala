package blackjack.repository

import blackjack.domain.user.UserInfo
import cats.syntax.all.toFunctorOps
import doobie.{ ConnectionIO, Fragment }
import doobie.implicits.toSqlInterpolator

trait UserRepository[F[_]]:
  def insert(userInfo: UserInfo): F[Unit]

object UserRepository extends UserRepository[ConnectionIO]:
  private object SQL:
    def insert(userInfo: UserInfo): Fragment =
      sql"""
          |INSERT INTO user_accounts (login_name, first_name, last_name, email, password_hash)
          |VALUES (${userInfo.loginName}, ${userInfo.firstName}, ${userInfo.lastName}, ${userInfo.email}, ${userInfo.passwordHash.trim})
          |""".stripMargin

  override def insert(userInfo: UserInfo): ConnectionIO[Unit] =
    SQL.insert(userInfo).update.run.void