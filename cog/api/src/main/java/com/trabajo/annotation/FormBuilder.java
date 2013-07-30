package com.trabajo.annotation;

import com.trabajo.ValidationException;
import com.trabajo.utils.XMLSBHelper;

public class FormBuilder {
	
	//taskid=[nodeId]
	private String taskCorrelator;
	
	public String buildStartForm(Form f) throws ValidationException {

		StringBuilder sb = new StringBuilder();

		sb.append("<items>");
		sb.append("<item type=\"settings\" position=\"label-left\" labelAlign=\"right\" labelWidth=\"150\" inputWidth=\"300\"/>");
		sb.append("<item type=\"fieldset\" name=\"data\" label=\""+f.title()+"\" inputWidth=\"700\">");
		
		for (FormItem item : f.dataItems()) {
			buildItem(sb, item);
		}
		sb.append("<item type=\"input\" label=\"Start Note\" name=\"_cog_Note\" value=\"Start Note\"/>");
		sb.append("</item></items>");
		return sb.toString();
	}

	public String buildForm(Form f, String taskCorrelator) throws ValidationException {
		this.taskCorrelator=taskCorrelator;
		
		StringBuilder sb = new StringBuilder();

		//sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><items>");
		sb.append("<items>");
		sb.append("<item type=\"settings\" position=\"label-left\" labelAlign=\"right\" labelWidth=\"150\" inputWidth=\"120\"/>");
		sb.append("<item type=\"fieldset\" name=\"data\" label=\""+f.title()+"\" inputWidth=\"auto\">");
		
		for (FormItem item : f.dataItems()) {
			buildItem(sb, item);
		}
		for (String action: f.actionButtons()) {
			buildActionButton(sb, action);
		}
		sb.append("</item></items>");

		return sb.toString();
	}

	private  void buildActionButton(StringBuilder sb, String action) {
		sb.append("<item type=\"button\" name=\"");
		sb.append(action);
		sb.append("\" value=\"Start Process\"/>");
	}

	private  void buildItem(StringBuilder sb, FormItem item)
			throws ValidationException {
		switch (item.type()) {
		case CHECKBOX:
			buildCheckBox(sb, item);
			break;
		case INPUT:
			buildInput(sb, item);
			break;
		case PASSWORD:
			buildPassword(sb, item);
			break;
		case RADIO:
			buildRadio(sb, item);
			break;
		case SELECT:
			buildSelect(sb, item);
			break;
		case UPLOAD:
			buildUpload(sb, item);
			break;
		default:
			throw new ValidationException("Control type not permitted");
		}
	}

	private  void buildUpload(StringBuilder sb, FormItem item) throws ValidationException {

		XMLSBHelper xhelp = new XMLSBHelper(sb);
		//{ type : "upload", url : "process", name : "uploader", mode : "html4", inputWidth : 300}
			
			
	
		xhelp.elm("item")
		.attr("type", "upload")
		.attr("mode", "html4")
		.attr("url", "upload?type=task&" + this.taskCorrelator + "&name=" + item.name())
		.attr("name", item.name())
		.attr("label", item.label())
		.attr("inputWidth", "200")
		.closeTag();

		DHXSelectOptionsParser cop = new DHXSelectOptionsParser(item.value());
		xhelp.append(cop.toString());

		xhelp.closeElm("item");
	}
	
	private  void buildSelect(StringBuilder sb, FormItem item) throws ValidationException {

		XMLSBHelper xhelp = new XMLSBHelper(sb);

		xhelp.elm("item").attr("type", "select").attr("name", item.name())
				.attr("label", item.label()).attr("inpuWidth", "120")
				.closeTag();

		DHXSelectOptionsParser cop = new DHXSelectOptionsParser(item.value());
		xhelp.append(cop.toString());

		xhelp.closeElm("item");
	}

	private  void buildPassword(StringBuilder sb, FormItem item) throws ValidationException {
		XMLSBHelper xhelp = new XMLSBHelper(sb);

		xhelp.elm("item").attr("type", "password").attr("name", item.name())
		.attr("label", item.label()).attr("value", item.value())
		.closeEmptyTag();
	}

	private  void buildRadio(StringBuilder sb, FormItem item) throws ValidationException {
		XMLSBHelper xhelp = new XMLSBHelper(sb);
		xhelp.elm("item").attr("type", "radio").attr("name", item.name())
				.attr("label", item.label()).attr("value", item.value())
				.closeEmptyTag();
	}

	private  void buildInput(StringBuilder sb, FormItem item) throws ValidationException {
		XMLSBHelper xhelp = new XMLSBHelper(sb);
		xhelp.elm("item").attr("type", "input").attr("name", item.name())
				.attr("label", item.label()).attr("value", item.value())
				.closeEmptyTag();
	}

	private  void buildCheckBox(StringBuilder sb, FormItem item) {
		XMLSBHelper xhelp = new XMLSBHelper(sb);
		xhelp.elm("item").attr("type", "checkbox").attr("name", item.name()).attr("label", item.label());
		
		if(item.value().equalsIgnoreCase("checked"))
			xhelp.attr("checked", "true");
		
		xhelp.closeEmptyTag();
	}
}
