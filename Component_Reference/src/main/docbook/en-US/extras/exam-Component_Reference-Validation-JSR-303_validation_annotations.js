import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ManagedBean
@RequestScoped
public class UserBean {

   @Size(min=3, max=12)
   private String name = null;
   
   @Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$" , message="Bad email")
   private String email = null;
   
   @Min(value = 18)
   @Max(value = 99)
   private Integer age;
   
//...
//Getters and Setters
}
