package org.scalax.ssh

import net.schmizz.sshj.{DefaultConfig, SSHClient, Config => SSHJConfig}
import net.schmizz.sshj.sftp.SFTPFileTransfer
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import net.schmizz.sshj.xfer.scp.SCPFileTransfer

import java.io.File
import java.nio.file.Path
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

sealed trait Client[W] {

  protected var backendClient: W

  def init(authMethod: AuthMethod): Unit

  def executeCommand(command: String): String

  def uploadFile(from: String, to: String)(implicit rIO: RemoteIO): Unit

  def uploadFiles(files: Seq[File], dest: Path)(implicit rIO: RemoteIO): Unit

  def uploadFolder(from: Path, to: Path)(implicit rIO: RemoteIO): Unit

  def uploadFolders(folders: Seq[Path])(implicit rIO: RemoteIO): Unit

  def downloadFile(serverLocation: String, localLocation: String)(implicit rIO: RemoteIO): Unit

  def downloadFiles(files: Seq[File])(implicit rIO: RemoteIO): Unit

  def downloadFolder(folder: Path)(implicit rIO: RemoteIO): Unit

  def downloadFolders(folders: Seq[Path])(implicit rIO: RemoteIO): Unit

  def close(): Unit
}


case class SSHJBackend(config: SSHJConfig)
  extends Client[SSHClient]
    with AutoCloseable {

  protected var backendClient: SSHClient = _

  def init(authMethod: AuthMethod): Unit = {
    val _client = new SSHClient(config)
    _client.addHostKeyVerifier(new PromiscuousVerifier)
    _client.connect("10.10.1.101", 22)
    authMethod match {
      case PasswordBased(username, password) => _client.authPassword(username, password)
      case PasswordLess(username, keyProviders) => _client.authPublickey(username, keyProviders: _*)
    }
    backendClient = _client
  }

  def getSFTPTransfer: SFTPFileTransfer = backendClient.newSFTPClient().getFileTransfer

  def getSCPTransfer: SCPFileTransfer = backendClient.newSCPFileTransfer()

  def executeCommand(command: String): String = ???

  def uploadFile(from: String, to: String)(implicit rIO: RemoteIO): Unit = rIO.upload(from, to)

  def uploadFiles(files: Seq[File], dest: Path)(implicit rIO: RemoteIO): Unit = ???

  def uploadFolder(from: Path, to: Path)(implicit rIO: RemoteIO): Unit = ???

  def uploadFolders(folders: Seq[Path])(implicit rIO: RemoteIO): Unit = ???

  def downloadFile(serverLocation: String, localLocation: String)(implicit rIO: RemoteIO): Unit = rIO.download(serverLocation, localLocation)

  def downloadFiles(files: Seq[File])(implicit rIO: RemoteIO): Unit = ???

  def downloadFolder(folder: Path)(implicit rIO: RemoteIO): Unit = ???

  def downloadFolders(folders: Seq[Path])(implicit rIO: RemoteIO): Unit = ???

  def uploadFilesAsync(files: Seq[File], dest: String)(implicit rIO: RemoteIO, exec: ExecutionContext): Future[Unit] = Future.traverse(files) {
    file =>
      Future {
        println("Uploading " + file.getName)
        rIO.upload(file.getAbsolutePath, dest + s"/${file.getName}")
      }
  }.map(_ => ())

  def close(): Unit = backendClient.close()

}

object Driver extends App {
  val ssh = SSHJBackend(new DefaultConfig)
  ssh.init(PasswordBased("solo", "$737@Hack#"))
  implicit val rIO: RemoteIO = SshjSftpRemoteIO(ssh.getSFTPTransfer)
  val tp = Executors.newFixedThreadPool(500)
  implicit val exec: ExecutionContextExecutor = ExecutionContext.fromExecutor(tp)

  val filesToUpload = new File("D:\\Books\\fp-simplified_3").listFiles()

  val start = System.currentTimeMillis()

  ssh
    .uploadFilesAsync(filesToUpload, "/home/solo/tmp")
    .onComplete {
      _ =>
        tp.shutdown()
        println("Time in seconds: " + (System.currentTimeMillis() - start) / 1000.0 + " seconds")
    }

}