<#import "/common/application.ftl" as layout>
<@layout.dmLayout page='show'; section>
<#if section = "content">
<div class="container">
    <h4>Player Overview</h4>
    <div class="row">
        <div class="col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">${player.name}<#if player.clan??> - ${player.clan!''} [${player.clanTag!''}]</#if> - (${player.grimoire!''})</div>
                <#if player.chars??>
	                <#list player.chars as char>
	                    <div class="panel-body char-panel" style="background-image:url(${char.bg})">
	                        <img class="char-emblem" src="${char.emblem}">
	                        <div>
	                            <div>${char.charClass} / ${char.race} / ${char.gender}</div>
	                            <div class="levels">${char.level} - ${char.light}</div>
	                        </div>
	                    </div>
	                </#list>
                </#if>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <form id="search-form">    
            <div class="form-group">
                <input type="hidden" id="system" value="${player.system}" />
                <input type="hidden" id="name" value="${player.name}" />
                <label class="control-label">Find matches with:</label>
                <div class="input-group">
                    <input type="text" class="form-control" id="un2" placeholder="Username" required>
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="submit" id="submit">Search</button>
                    </span>
                </div>
            </div>
            </form>
        </div>
    </div>
</div>
</#if>
</@layout.dmLayout>