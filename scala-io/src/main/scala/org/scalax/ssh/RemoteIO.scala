package org.scalax.ssh

import net.schmizz.sshj.sftp.SFTPFileTransfer
import net.schmizz.sshj.xfer.scp.SCPFileTransfer

sealed trait RemoteIO {
  def upload(src: String, dest: String): Unit
  def download(src: String, dest: String): Unit
}

case class SshjSftpRemoteIO(sftpFileTransfer: SFTPFileTransfer) extends RemoteIO {
  def upload(src: String, dest: String): Unit = sftpFileTransfer.upload(src, dest)

  def download(src: String, dest: String): Unit = sftpFileTransfer.download(src, dest)
}

case class SshjScpRemoteIO(scpClient: SCPFileTransfer) extends RemoteIO {
  def upload(src: String, dest: String): Unit = scpClient.upload(src, dest)

  def download(src: String, dest: String): Unit = scpClient.download(src, dest)
}