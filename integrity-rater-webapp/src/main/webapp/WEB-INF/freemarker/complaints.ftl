<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<head>
	<#include "includes/head.ftl" />
</head>
<body>
<#include "includes/top-nav.ftl" />

<div id="bodyContent">
	<h1>Complaints</h1>
	
	<div id="complaintBox">
		<ul>	
		<#list complaints as complaint>
		<li>
			<a href="complaint?complaint=${complaint.id?c}">${complaint.title}</a><br/>
			<div class="complaintAbout">About: <a href="person?person=${complaint.person.id?c}">${complaint.person.name}</a></div>
			<div>${complaint.body}</div>
		</li>
		</#list>
		</ul>			
	</div>

</div> <!-- bodyContent -->
</body>
</html>