<#import "/common/application.ftl" as layout>
<@layout.dmLayout page='matches'; section>
<#if section = "content">
<div class="container">
    <h4>Matches</h4>
    <div class="row">
        <div class="col-lg-6">
            <h5>${proc.name1}'s matches with ${proc.name2}</h5>
            <input id="processId" type="hidden" value="${proc.id}" />
        </div>
    </div>
    <div class="progress progress-striped active">
        <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
        </div>
        <div class="progress-value">Loading</div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <div id="matchCount"></div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <div id="matches" class="panel-group"></div>
        </div>
    </div>
</div>
</#if>
</@layout.dmLayout>