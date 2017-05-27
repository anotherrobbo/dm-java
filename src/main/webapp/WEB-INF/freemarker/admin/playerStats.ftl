<#import "/common/application.ftl" as layout>
<@layout.dmLayout page='playerStats'; section>
<#if section = "content">
<div class="container">
    <h4>Admin - Player Stats</h4>
    <div class="row">
        <div class="col-lg-6">
            <div class="panel-group">
                <table class="table table-striped players">
                    <thead><tr>
                        <th>Name / Sys</th>
                        <th class="text-right">O</th>
                        <th class="text-right">M</th>
                        <th class="text-right">Created</th>
                        <th class="text-right">Updated</th>
                        <th class="text-right">ARs</th>
                    </tr></thead>
                    <tbody>
                    <#list records as p_rec>
                        <tr>
                            <td>${p_rec.name}<br/>${p_rec.system}</td>
                            <td class="text-right">${p_rec.overviewCount}</td>
                            <td class="text-right">${p_rec.matchesCount}</td>
                            <td class="text-right dateCell">${p_rec.createdAt}</td>
                            <td class="text-right dateCell">${p_rec.updatedAt}</td>
                            <td class="text-right">${p_rec.activityRecords?size}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</#if>
</@layout.dmLayout>