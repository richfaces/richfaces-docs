@ManagedBean
@RequestScoped
public class UserBean {

   @Size(min = 5, max = 15, message="Wrong size for password")
   private String password;
   @Size(min = 5, max = 15, message="Wrong size for confirmation")
   private String confirm;
   private String status = "";
   
   @AssertTrue(message = "Different passwords entered!")
   public boolean isPasswordsEquals() {
      return password.equals(confirm);
   }

   public void storeNewPassword() {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully changed!", "Succesfully changed!"));
   }

   ...
}
