<#function max x y>
    <#if (x<y)><#return y><#else><#return x></#if>
</#function>

<#function min x y>
    <#if (x<y)><#return x><#else><#return y></#if>
</#function>

<#macro pages totalPages p baseUrl >
    <#assign size = totalPages?size>
    <#if (p<=5)> <#-- p among first 5 pages -->
        <#assign interval = 1..(min(5,size))>
    <#elseif ((size-p)<5)> <#-- p among last 5 pages -->
        <#assign interval = (max(1,(size-4)))..size >
    <#else>
        <#assign interval = (p-2)..(p+2)>
    </#if>
    
    <#if ((p-1) > 0) >
    	<a href="${baseUrl?html}${p-1}">Previous</a>
    </#if>
    
    <#if !(interval?seq_contains(1))>
     1 ... <#rt>
    </#if>
    <#list interval as page>
        <#if page=p>
         <b>${page}</b> <#t>
        <#else>
         <a href="${baseUrl?html}${page}">${page}</a> <#t>
        </#if>
    </#list>
    <#if !(interval?seq_contains(size))>
     ... ${size}<#lt>
    </#if>
    
    <#if ((p+1) <= size) >
    	<a href="${baseUrl?html}${p+1}">Next</a>
    </#if>
    
</#macro>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>FOSP Search</title>
	
    <link rel="stylesheet" href="/resources/css/search.css" type="text/css" />
    <link rel="stylesheet" href="/resources/css/ui-lightness/jquery-ui-1.8.18.css" type="text/css" />
    
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery-ui/jquery-ui-1.8.18.min.js"></script>
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
							<input type="text" placeholder="Nhập từ khóa" name="q" id="queryInput" value="${search_results.query?html}"  />
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
		<h3>Kết quả Tìm kiếm</h3>
		<div role="main" id="res" class="med">
			<div id="topstuff">				
				<span id="resultStats">Khoảng ${search_results.total} kết quả</span>
			</div>		
			
			<div id="search">

				<div id="ires">
					<ul id="rso" class="results">						
						<#assign listResults = search_results.listResults >
						<#list listResults as listResult >
							<li class="g">
								<div>
									<h3 class="r">
										<a class="l" target="_blank" href="${listResult["share_url"]}" >${listResult["title"]}</a>
									</h3>
									<div class="s">
										<div class="f kv">
											<cite>${listResult["share_url"]}</cite>
										</div>
										<span class="st">${listResult["content"]}</span>
									</div>
								</div>
							</li>
						</#list>
					</ul>					
				</div>
				<div class="pagination">
					<#assign totalPages = (search_results.total / search_results.rows) + 1 >					
					<#assign currentPage = requestParams["p"]?number >
					<#assign startPage = requestParams["p"]?number >
					<#assign q = requestParams["q"] >
					<#assign baseUrl =  UrlUtil.buildUrl(request, "/search/form/string?q=") + q + "&p=" >					
					<@pages 1..totalPages currentPage baseUrl />
				</div>
			</div>
		</div>
	</div>

	<div id="right">
		<h3>Liên kết tài trợ</h3>
		<p><a href="http://fyi.vn/category/may-anh-kts/532.html">FYI Việt Nam</a>Máy ảnh KTS - Siêu thị điện máy online FYI Việt Nam</p>
		<p><a href="http://www.shipcd.com/shipcdshop">ShipCD.com </a>Đĩa CD, DVD, Sách, Game, và các mặt hàng order theo yêu cầu từ USA </p>		
		<br>		
	</div>

	<script type="text/javascript">		
		jQuery( "#queryInput" ).autocomplete({
			source: "http://localhost:10001/search/suggest/json",
			select: function( event, ui ) {
				setTimeout(function(){
					jQuery("#queryInput").val('"' + ui.item.value + '"');	
				},90);				
			}
		});
	</script>
</body>
</html>