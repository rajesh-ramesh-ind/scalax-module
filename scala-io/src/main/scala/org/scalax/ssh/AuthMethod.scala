package org.scalax.ssh

import net.schmizz.sshj.userauth.keyprovider.KeyProvider

sealed trait AuthMethod {
  val username: String
}

case class PasswordBased(username: String, password: String) extends AuthMethod

case class PasswordLess(username: String, keyProviders: Seq[KeyProvider]) extends AuthMethod