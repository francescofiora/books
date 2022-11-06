package it.francescofiora.books.itt.ssl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import lombok.Data;

/**
 * Certificate Generator.
 */
@Data
public class CertificateGenerator {

  private static final Set<PosixFilePermission> KEY_PERMISSIONS =
      PosixFilePermissions.fromString("r--r--r--");
  private static final int KEY_LENGHT = 2048;
  private static final int DAYS = 3600;
  public static final String PASSWORD = "mypass";
  private static final String ROOT_CERT_KEY = "ca-key.pem";
  private static final String ROOT_CERT = "ca.pem";
  public static final String TRUSTSTORE = "truststore.ts";
  public static final String KEY_STORE_FILE = "%s-key.pem";
  public static final String REQ_FILE = "%s-req.pem";
  public static final String CERT_FILE = "%s-cert.pem";
  public static final String KEY_STORE_P12_FILE = "%s-keystore.p12";
  public static final String KEY_STORE_JKS_FILE = "%s-keystore.jks";
  public static final String PEM_FILE = "%s.pem";

  private final String path;
  private final String rootCertName;

  /**
   * Generate CA Certificate.
   *
   * @throws Exception if errors occur
   */
  public void generateRoot() throws Exception {
    var rootCertkeyPath = getFullPath(ROOT_CERT_KEY);
    exec("openssl genrsa -out " + rootCertkeyPath + " " + KEY_LENGHT);
    setPosixFilePermissions(rootCertkeyPath);

    var rootCertPath = getFullPath(ROOT_CERT);
    exec("openssl req -new -x509 -nodes -days " + DAYS + " -subj \"/C=IE/ST=Ireland/CN="
        + rootCertName + "\" -key " + rootCertkeyPath + " -out " + rootCertPath);
    setPosixFilePermissions(rootCertPath);

    var trustorePath = getFullPath(TRUSTSTORE);
    exec("keytool -importcert -file " + rootCertPath + " -keystore " + trustorePath + " -storepass "
        + PASSWORD + " -alias \"" + rootCertName + "\" -noprompt");
  }

  /**
   * Generate Signed Certificate for a specific container.
   *
   * @param name the name of container
   * @param serial the serial number
   * @param createJks if true generate the JKS file
   * @throws Exception if errors occur
   */
  public void generateSignedCertificate(String name, String serial, boolean createJks)
      throws Exception {
    var keyStorePath = getFullPathFormatted(KEY_STORE_FILE, name);
    var reqPath = getFullPathFormatted(REQ_FILE, name);
    exec("openssl req -newkey rsa:" + KEY_LENGHT + " -days " + DAYS
        + " -nodes -subj \"/C=IE/ST=Ireland/CN=" + name + "\" -keyout " + keyStorePath
        + " -out " + reqPath);
    setPosixFilePermissions(keyStorePath);
    setPosixFilePermissions(reqPath);

    var rootCertPath = getFullPath(ROOT_CERT);
    var rootCertkeyPath = getFullPath(ROOT_CERT_KEY);
    var certPath = getFullPathFormatted(CERT_FILE, name);
    exec("openssl x509 -req -in " + reqPath + " -days " + DAYS + " -CA " + rootCertPath + " -CAkey "
        + rootCertkeyPath + " -set_serial \"" + serial + "\" -out " + certPath);
    setPosixFilePermissions(certPath);

    if (createJks) {
      var ksP12Path = getFullPathFormatted(KEY_STORE_P12_FILE, name);
      exec("openssl pkcs12 -export -in " + certPath + " -inkey " + keyStorePath + " -passout pass:"
          + PASSWORD + " -out " + ksP12Path);

      var ksJksPath = getFullPathFormatted(KEY_STORE_JKS_FILE, name);
      exec("keytool -importkeystore -srckeystore " + ksP12Path
          + " -srcstoretype pkcs12 -srcstorepass " + PASSWORD + " -destkeystore " + ksJksPath
          + " -deststoretype JKS -deststorepass " + PASSWORD);

      exec("keytool -import -alias ca -keystore " + ksJksPath + " -file " + rootCertPath
          + " -storepass " + PASSWORD + " -noprompt");
    }

    var trustorePath = getFullPath(TRUSTSTORE);
    exec("keytool -importcert -file " + certPath + " -keystore " + trustorePath + " -storepass "
        + PASSWORD + " -alias " + name + " -noprompt");
  }

  public String getFullPathFormatted(String format, Object... args) {
    return getFullPath(String.format(format, args));
  }

  public String getFullPath(String fileName) {
    return path + File.separator + fileName;
  }

  /**
   * Clean all SSL file generated.
   */
  public void clean() {
    File folder = new File(path);
    for (var file : folder.listFiles()) {
      try {
        Files.deleteIfExists(file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void exec(String op) throws Exception {
    String[] cmd = {"/bin/sh", "-c", op};
    var p = Runtime.getRuntime().exec(cmd);
    while (p.isAlive()) {
    }
  }

  private void setPosixFilePermissions(String path) throws Exception {
    Files.setPosixFilePermissions(Path.of(path), KEY_PERMISSIONS);
  }

  /**
   * Append keyStore file and Certificate file to new Certificate File.
   *
   * @param name the container name
   * @throws Exception if errors occur
   */
  public void appendCertificate(String name) throws Exception {
    var keyStorePath = getFullPathFormatted(KEY_STORE_FILE, name);
    var certPath = getFullPathFormatted(CERT_FILE, name);
    var pemPath = getFullPathFormatted(PEM_FILE, name);
    exec("cat " + keyStorePath + " " + certPath + " > " + pemPath);
  }
}
