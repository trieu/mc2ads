<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="/resources/css/search.css" type="text/css" />
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    
	<title>FOSP Search</title>	
		
</head>
<body>

	<div id="logo">
		<table class="header" >
			<tr>
				<td width="20%">
				<div class="logo_holder" >
					<a href="${UrlUtil.buildUrl(request, "/search/form/string")}"><img src="http://www.fptonline.net/imgs/logo.gif" alt="" /></a>
				</div>
				</td>
				<td width="80%">
					<div class="query" >						
						<form id="query-form" action="${UrlUtil.buildUrl(request, "/search/form/string")}" method="GET" accept-charset="utf-8" >
							<input type="text" placeholder="Nhập từ khóa" name="q" id="qStrTxt" value=""  />
							<input type="hidden" name="p" value="1" >
							<input type="submit" value="Tìm kiếm" />
						</form>
					</div>
				</td>
			</tr>			
		</table>
	</div>

	<div id="left">
		<h3>Tìm kiếm</h3>
		<ul class="nav">
			<li class="mitem msel"><div class="nav_item cur">Tin tức</div></li>			
			<li class="mitem msel"><div class="nav_item ">Hình ảnh</div></li>
			<li class="mitem msel"><div class="nav_item">Videos</div></li>
			<li class="mitem msel"><div class="nav_item">Các chủ đề</div></li>
		</ul>
		<br /> <br />
	</div>
	
	

	<div id="content">		
		<div role="main" id="res" class="med">			
		</div>
	</div>

	<div id="right">
		<h3>Liên kết tài trợ</h3>
		<p><a href="http://fyi.vn/category/may-anh-kts/532.html">FYI Việt Nam</a>Máy ảnh KTS - Siêu thị điện máy online FYI Việt Nam</p>
		<p><a href="http://www.shipcd.com/shipcdshop">ShipCD.com </a>Đĩa CD, DVD, Sách, Game, và các mặt hàng order theo yêu cầu từ USA </p>		
		<br>
		<br>
	</div>

</body>
</html>