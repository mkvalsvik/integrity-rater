<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<head>
	<#include "includes/head.ftl" />
</head>
<body>
<#include "includes/top-nav.ftl" />

<div id="bodyContent">
	<h3>${challenge.title}</h3>
	<p>${challenge.body}</p>
	
	<a href="challenge-vote?challengeId=${challenge.id?c}">Support</a> 

<h2>In response to:</h2>
	<h3>${challenge.complaint.title}</h3>
	<p>${challenge.complaint.body}</p>


</div> <!-- bodyContent -->
</body>
</html>