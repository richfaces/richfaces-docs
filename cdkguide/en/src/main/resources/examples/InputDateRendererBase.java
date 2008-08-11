package org.mycompany.renderkit;

import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

import org.ajax4jsf.renderkit.HeaderResourcesRendererBase;
import org.mycompany.component.UIInputDate;


public abstract class InputDateRendererBase  extends HeaderResourcesRendererBase{

	public void decode(FacesContext context, UIComponent component){
		ExternalContext external = context.getExternalContext();
		Map requestParams = external.getRequestParameterMap();
		UIInputDate inputDate = (UIInputDate)component;
		String clientId = inputDate.getClientId(context);
		String submittedValue = (String)requestParams.get(clientId);
		
		if (submittedValue != null) {
			inputDate.setSubmittedValue(submittedValue);
		}
	}	

	protected String getValueAsString(FacesContext context,
			UIComponent component) throws IOException {

		UIInputDate inputDate = (UIInputDate) component;
		String valueString = (String) inputDate.getSubmittedValue();

		if (valueString == null) {
			Object value = inputDate.getValue();
			if (value != null) {
				Converter converter = getConverter(context, inputDate);
				valueString = converter.getAsString(context, component, value);
			}
		}
		return valueString;
	}
			
	
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException
			{
			UIInputDate inputDate = (UIInputDate)component;
			Converter converter = getConverter(context, inputDate);
			String valueString = (String)submittedValue;
			return converter.getAsObject(context, component, valueString);
			}
	
	private Converter getConverter(FacesContext context, UIInputDate inputDate)
			{
			Converter converter = inputDate.getConverter();
			if (converter == null)
			{
			DateTimeConverter datetime = new DateTimeConverter();
			datetime.setLocale(context.getViewRoot().getLocale());
			datetime.setTimeZone(TimeZone.getDefault());
			datetime.setType("date");
			datetime.setDateStyle("medium");
			datetime.setPattern("d/m/y");
			converter = datetime;
			}
			return converter;
			}
}
