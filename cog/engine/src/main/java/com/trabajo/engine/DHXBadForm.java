package com.trabajo.engine;

public class DHXBadForm {

	public static final String DEFAULT = 
			
			"<items>\r\n" + 
			"	<item type=\"settings\" position=\"label-left\" labelWidth=\"100\" inputWidth=\"120\" />\r\n" + 
			"   <item type=\"fieldset\" label=\"Error\" inputWidth=\"auto\">" +
			"	<item type=\"label\" value=\"\" label=\"An error occurred\" />\r\n" + 
			"	<item type=\"button\" value=\"Proceed\" />\r\n" + 
			"</item></items>";
	
	public static final String PROCESS_STARTUP = 
			
			"<items>\r\n" + 
			"	<item type=\"settings\" position=\"label-left\" labelWidth=\"100\" inputWidth=\"120\" />\r\n" + 
			"   <item type=\"fieldset\" label=\"Error\" inputWidth=\"auto\">" +
			"	<item type=\"label\" value=\"\" label=\"An error occurred. Review the process Start form options on the process class\" />\r\n" + 
			"	<item type=\"button\" value=\"Proceed\" />\r\n" + 
			"</item></items>";
}
