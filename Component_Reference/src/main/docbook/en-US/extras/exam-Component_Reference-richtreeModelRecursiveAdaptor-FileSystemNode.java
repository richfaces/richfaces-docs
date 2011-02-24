public class FileSystemNode {
   ...
   public synchronized List<FileSystemNode> getDirectories() {
      if (directories == null) {
         directories = Lists.newArrayList();
         Iterables.addAll(directories, transform(filter(getResourcePaths(), containsPattern("/$")), FACTORY));
      }
      return directories;
   }

   public synchronized List<String> getFiles() {
      if (files == null) {
         files = new ArrayList<String>();
         Iterables.addAll(files, transform(filter(getResourcePaths(), not(containsPattern("/$"))), TO_SHORT_PATH));
      }
      return files;
   }

   private Iterable<String> getResourcePaths() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Set<String> resourcePaths = externalContext.getResourcePaths(this.path);
        
      if (resourcePaths == null) {
         resourcePaths = Collections.emptySet();
      }
      return resourcePaths;
   }   
   ...
}
