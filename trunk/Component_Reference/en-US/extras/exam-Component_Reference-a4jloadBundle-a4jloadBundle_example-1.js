package demo;


 import java.util.Locale;

 import javax.faces.context.FacesContext;


 public class ChangeLocale {

 	public String germanAction() {

		 FacesContext context = FacesContext.getCurrentInstance();

		 context.getViewRoot().setLocale(Locale.GERMAN);

		 return null;

	 }


	 public String englishAction() {

		 FacesContext context = FacesContext.getCurrentInstance();

		 context.getViewRoot().setLocale(Locale.ENGLISH);

		 return null;

	 }

	 

	 public String italianAction() {

		  FacesContext context = FacesContext.getCurrentInstance();

		  context.getViewRoot().setLocale(Locale.ITALIAN);

		  return null;

	  }

}
