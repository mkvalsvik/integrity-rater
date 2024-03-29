<#import "spring.ftl" as spring />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<#assign page = "notFound">
<head>
	<#include "includes/head.ftl" />
</head>
<#flush>
<body>
<div id="bodyContent">
<div id="content">
	<h1>Sorry, but there has been some sort of error.</h1>				
	<#if logExceptionMessage??>
		<p>${logExceptionMessage}</p>
	</#if>			
	<p>If you receive this error message a second time, please <a href="mailto:support@integrityrater.org?subject=IntegrityRater.org error code: ${logExceptionID}">contact our support staff</a> and mention error code ${logExceptionID}.</p>
	
</div>

</div> <!-- bodyContent -->
</body>
</html>