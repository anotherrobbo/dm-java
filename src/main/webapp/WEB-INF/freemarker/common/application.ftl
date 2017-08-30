<#macro dmLayout page="">
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Destiny Matches</title>
	<!-- Latest compiled and minified CSS -->
	<link href="${url.resourcePath + 'css/'}bootstrap.css" rel="stylesheet" />
	<link href="${url.resourcePath + 'css/'}application.css" rel="stylesheet" />
	<link href="${url.resourcePath + 'css/' + page}.css" rel="stylesheet" />
	<script>var rootUrl = "${url.rootUrl}";</script>
	<script src="https://code.jquery.com/jquery-3.2.1.min.js"
	  integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
	  crossorigin="anonymous"></script>
	<script src="${url.resourcePath + 'scripts/'}bootstrap.min.js" type="text/javascript"></script>
	<script src="${url.resourcePath + 'scripts/'}application.js" type="text/javascript"></script>
	<script src="${url.resourcePath + 'scripts/' + page}.js" type="text/javascript"></script>
    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-15725595-4', 'auto');
      ga('send', 'pageview');
    </script>
</head>
<body>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="${url.rootUrl}/">Destiny Matches</a>
        </div>
    </div>
</div>

<#nested "content">

<footer class="footer">
    <div class="container">
        <p class="text-muted"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> admin(at)anotherrobbo.com | <a href="https://www.reddit.com/message/compose/?to=another_robbo">/u/another_robbo</a></p>
    </div>
</footer>

</body>
</html>
</#macro>