<#import "/common/application.ftl" as layout>
<@layout.dmLayout page='single'; section>
<#if section = "content">
<div class="container">
    <h4>Single Match</h4>
    <input id="activityId" type="hidden" value="#{activityId}">
    <div class="row">
        <div class="col-lg-6">
            <div id="match" class="panel-group">
                <span class="loading-spinner glyphicon glyphicon-refresh glyphicon-spin"></span>
            </div>
        </div>
    </div>
</div>
</#if>
</@layout.dmLayout>