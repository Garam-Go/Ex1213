<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>list</title>
<style>
	body{background:#BEF781;}
	#page{width:960px; margin: 0 auto; background:white; box-shadow: 0.5px 0.5px 3px gray; text-align:center;}
	.box, .dbox{width:100px; padding:10px; margin:10px; float:left; background:skyblue;}
	#con, #naver, #daum{width:560px;border:1px solid; overflow: hidden; margin:0 auto;}
	.nbox{border:1px solid;}
</style>
</head>
<body>
	<div id="page">
	<h1>LISTLISTLITSLITLSITLISLTITLSIT</h1>
	
		<div id="con"></div>
		<script id="temp" type="text/x-handlebars-templete">
			{{#each .}}
				<div class="box">
					<div>{{rank}}</div>
					<img src={{image}} width=100>
					<div>{{title}}</div>
				</div>
			{{/each}}
		</script>
		<h1>네이버 실시간 검색</h1>
		<div id="naver"></div>
		<script id="ntemp" type="text/x-handlebars-templete">
			{{#each .}}
				<div class="nbox">
					<div>{{rank}}</div>
					<div>{{title}}</div>
				</div>
			{{/each}}
		</script>
		
		<h1>다음 실시간 날씨</h1>
		<div id="daum"></div>
		<script id="dtemp" type="text/x-handlebars-templete">
			{{#each .}}
				<div class="dbox">
					<div>{{region}}</div>
					<div>{{condition}}</div>
					<div>{{temp}}ºC</div>
				</div>
			{{/each}}
		</script>
	</div>
</body>
<script>
getMovie();
getNaver();
getDaum();

function getDaum(){
	$.ajax({
		type:"get",
		url:"daum.json",
		success:function(data){
			var temp=Handlebars.compile($("#dtemp").html());
			$("#daum").html(temp(data));
		}
	});
}

function getNaver(){
	$.ajax({
		type:"get",
		url:"naver.json",
		success:function(data){
			var temp=Handlebars.compile($("#ntemp").html());
			$("#naver").html(temp(data));
		}
	});
}

function getMovie(){
	$.ajax({
		type:"get",
		url:"movie.json",
		success:function(data){
			var temp=Handlebars.compile($("#temp").html());
			$("#con").html(temp(data));

		}
	});
}
</script>
</html>