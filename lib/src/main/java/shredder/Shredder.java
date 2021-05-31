package shredder;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

/**
 * 主程式.
 *
 * @author ray
 */
public class Shredder {
  public static void main(String[] args) {
    new Shredder().shreddAllFile(Paths.get(args[0]));
  }

  /**
   * 安全刪除所有檔案.
   *
   * @param root 根目錄
   */
  public void shreddAllFile(Path root) {
    try {

      Files.walkFileTree(
          root,
          new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                throws IOException {
              if (root.equals(dir)) {
                return FileVisitResult.CONTINUE;
              }
              Files.delete(dir);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                throws IOException {
              try {
                overwriteDeleteFile(path);
              } catch (AccessDeniedException e) {
                e.printStackTrace();

                Runtime.getRuntime().exec("chmod 777 " + path);
                overwriteDeleteFile(path);

              } catch (IOException e) {
                e.printStackTrace();
              }
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 用隨機uuid覆蓋檔案並刪除檔案.
   *
   * @param path 檔案路徑
   * @throws IOException 例外
   */
  private void overwriteDeleteFile(Path path) throws IOException {
    Path overridePath = Files.write(path, UUID.randomUUID().toString().getBytes());
    Files.delete(overridePath);
    System.out.println(path);
  }
}
