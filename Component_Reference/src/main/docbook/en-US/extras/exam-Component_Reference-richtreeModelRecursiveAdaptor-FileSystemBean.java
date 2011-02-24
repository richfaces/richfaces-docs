@ManagedBean
@RequestScoped
public class FileSystemBean {
   private static final String SRC_PATH = "/WEB-INF";

   private List<FileSystemNode> srcRoots;

   public synchronized List<FileSystemNode> getSourceRoots() {
      if (srcRoots == null) {
         srcRoots = new FileSystemNode(SRC_PATH).getDirectories();
      }
      return srcRoots;
   }
}
